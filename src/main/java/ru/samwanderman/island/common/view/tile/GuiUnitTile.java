/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view.tile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ru.samwanderman.island.common.ai.UnitAI;
import ru.samwanderman.island.common.animation.ChangePositionAnimation;
import ru.samwanderman.island.common.core.object.UnitTile;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.wheel.ai.Logic;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.core.Config;
import ru.samwanderman.wheel.event.listener.ObjectListener;
import ru.samwanderman.wheel.io.Resources;
import ru.samwanderman.wheel.log.Log;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Gui Unit tile
 */
public class GuiUnitTile extends GuiObjectTile<UnitAI> {
	public GuiUnitTile(final GuiLevel guiLevel, final UnitTile tile) 
			throws IOException {
		super(guiLevel, tile);
		setAI(new UnitAI(this));
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

		final ChangePositionAnimation animation = (ChangePositionAnimation) getAnimation("walk");
		animation.setPath(path, (path.size() - 1) * Config.GLOBAL_TIMER_STEP * 100);
		playAnimation("walk");
	}
	
	@Override
	protected void setupAnimations(final List<String> names) {
		final GuiUnitTile self = this;
		
		for (final String name: names) {
			try {
				final IAnimation animation = Resources.loadAnimation("/units/" + getTile().getId() + "/" + name + ".json");
				if (name.equals("walk")) {
					final ChangePositionAnimation changePositionAnimation = new ChangePositionAnimation(self, animation);
					changePositionAnimation.setErrorCallback(new ChangePositionAnimationErrorCallback(changePositionAnimation));
					addAnimation(name, changePositionAnimation);
				} else {
					addAnimation(name, animation);
				}
			} catch (final IOException e) { 
				Log.error(e.getMessage());
			}
		}
	}
	
	private final class ChangePositionAnimationErrorCallback implements ObjectListener<Object> {
		private final ChangePositionAnimation animation;
		
		public ChangePositionAnimationErrorCallback(final ChangePositionAnimation animation) {
			this.animation = animation;
		}
		
		@Override
		public final void on(final Object finalPoint) {
			final int[][] map = ((GuiLevel) getParent()).getPathMap();
			final Point2D newEndPoint = Logic.getFindPathAlgorithm().findNewEndPoint(map, getPoint(), (Point2D) finalPoint);
			final LinkedList<Point2D> newPath = Logic.getFindPathAlgorithm().find(map, getPoint(), newEndPoint);
			animation.setPath(newPath, (newPath.size() - 1) * Config.GLOBAL_TIMER_STEP * 100);
		}
	}
}
