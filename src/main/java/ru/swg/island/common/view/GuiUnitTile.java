/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.io.IOException;
import java.util.LinkedList;

import ru.swg.island.common.animation.SimpleChangePositionAnimation;
import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.UnitTile;
import ru.swg.wheelframework.ai.Logic;
import ru.swg.wheelframework.animation.ClipAnimation;
import ru.swg.wheelframework.core.Config;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.view.Graphics;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * Gui Unit tile
 */
public class GuiUnitTile extends GuiObjectTile {
	private final GuiUnitTile self = this;
	private SimpleChangePositionAnimation animChangePos = null;
	private ClipAnimation clipAnimation = new ClipAnimation(this, null, Config.GLOBAL_TIMER_STEP * 100);
	
	private final ObjectListener<Point2D> onAnimationError = new ObjectListener<Point2D>() {
		@Override
		public final void on(final Point2D finalPoint) {
			final int[][] map = ((GuiLevel) getParent()).getPathMap();
			final Point2D newEndPoint = Logic.getFindPathAlgorithm().findNewEndPoint(map, getPoint(), finalPoint);
			final LinkedList<Point2D> newPath = Logic.getFindPathAlgorithm().find(map, getPoint(), newEndPoint);
			animChangePos = new SimpleChangePositionAnimation(self, newPath, (newPath.size() - 1) * Config.GLOBAL_TIMER_STEP * 100, null, onAnimationError);
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
		if (animChangePos != null) {
			animChangePos.run();
		}

		if (clipAnimation.isRunning()) {
			clipAnimation.paint(graphics);
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
		
		animChangePos = new SimpleChangePositionAnimation(this, path, (path.size() - 1) * Config.GLOBAL_TIMER_STEP * 100, null, onAnimationError);
		animChangePos.start();
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
	}
}
