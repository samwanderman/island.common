/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.io.IOException;
import java.util.LinkedList;

import ru.swg.island.common.animation.SimpleChangePositionAnimation;
import ru.swg.island.common.animation.SimpleClipAnimation;
import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.UnitTile;
import ru.swg.wheelframework.ai.Logic;
import ru.swg.wheelframework.core.Config;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.view.Graphics;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * Gui Unit tile
 */
public class GuiUnitTile extends GuiObjectTile {
	private final GuiUnitTile self = this;
	private SimpleChangePositionAnimation animChangePos = null;
	
	private SimpleClipAnimation clipAnimation = new SimpleClipAnimation(this, Resources.loadAnimation("units/unit1/walk"), Config.GLOBAL_TIMER_STEP * 100, null, null);
	
	private final ObjectListener<Integer> onAnimationEnd = new ObjectListener<Integer>() {
		@Override
		public final void on(final Integer status) {
			clipAnimation.reset();
		}
	};
	
	private final ObjectListener<Integer> onAnimationSuccess = new ObjectListener<Integer>() {
		@Override
		public final void on(final Integer object) {
			clipAnimation.reset();
		}
	};
	
	private final ObjectListener<Point2D> onAnimationError = new ObjectListener<Point2D>() {
		@Override
		public final void on(final Point2D finalPoint) {
			clipAnimation.reset();
			final int[][] map = ((GuiLevel) getParent()).getPathMap();
			final Point2D newEndPoint = Logic.getFindPathAlgorithm().findNewEndPoint(map, getPoint(), finalPoint);
			final LinkedList<Point2D> newPath = Logic.getFindPathAlgorithm().find(map, getPoint(), newEndPoint);
			animChangePos = new SimpleChangePositionAnimation(self, newPath, (newPath.size() - 1) * Config.GLOBAL_TIMER_STEP * 100, onAnimationSuccess, onAnimationError);
		}
	};
	
	public GuiUnitTile(final UnitTile tile) 
			throws IOException {
		super(tile);
	}
	
	public GuiUnitTile(final ObjectTile tile, final Point2D point) 
			throws IOException {
		super(tile, point);
	}
	
	@Override
	public final void paint(final Graphics graphics) {
		if (clipAnimation.isRunning()) {
			clipAnimation.paint(graphics);
			paintSelection(graphics);
			return;
		}
		
		super.paint(graphics);
	}
	
	/**
	 * Set animation path
	 * 
	 * @param path
	 */
	public final void setPath(final LinkedList<Point2D> path) {
		if ((path == null) || (path.size() <= 1)) {
			return;
		}
		
		animChangePos = new SimpleChangePositionAnimation(this, path, (path.size() - 1) * Config.GLOBAL_TIMER_STEP * 100, onAnimationEnd, onAnimationError);
		animChangePos.start();
		clipAnimation.reset();
		clipAnimation.start();
	}
	
	/**
	 * Check if change pos animation running
	 * 
	 * @return
	 */
	public final boolean isChangePositionAnimationRunning() {
		return animChangePos != null ? animChangePos.isRunning() : false; 
	}
	
	@Override
	public final void sync() {
		if (animChangePos != null) {
			animChangePos.run();
		}
		
		if (clipAnimation != null) {
			clipAnimation.run();
		}
	}
}
