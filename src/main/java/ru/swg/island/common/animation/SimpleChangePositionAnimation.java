/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.animation;

import java.util.List;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.view.GuiTile;
import ru.swg.wheelframework.animation.Animation;
import ru.swg.wheelframework.core.Config;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.GuiRepaintEvent;
import ru.swg.wheelframework.view.Point2D;

/**
 * Animation for simple change position - just coordinates
 */
public final class SimpleChangePositionAnimation implements Animation {
	// Animation target
	private final GuiTile target;
	// animation running
	private boolean running = false;
	// Animation path
	private final List<Point2D> path;
	// Animation speed
	private final int speed;
	// step speed
	private final int stepSpeed;
	// current step
	private int step;
	
	/**
	 * Constructor
	 * 
	 * @param target
	 * @param path
	 * @param speed
	 */
	public SimpleChangePositionAnimation(final GuiTile target, final List<Point2D> path, final int speed) {
		this.target = target;
		this.speed = speed;
		this.path = path;
		step = 0;
		stepSpeed = speed / (path.size() - 1);
	}
	
	@Override
	public final boolean isRunning() {
		return running;
	}
	
	@Override
	public final void start() {
		running = true;
	}

	@Override
	public final void stop() {
		running = false;
	}

	@Override
	public final void restart() {
		start();
		stop();
	}

	@Override
	public final void run() {
		if (!running) {
			return;
		}
		
		if (step >= speed) {
			stop();
			final Point2D endPoint = path.get(path.size() - 1);
			target.setPoint(endPoint);
			target.setX(endPoint.getX() * Const.TILE_WIDTH);
			target.setY(endPoint.getY() * Const.TILE_HEIGHT);
			return;
		}
		
		final int idx = step / stepSpeed, offset = step % stepSpeed;
		final Point2D point = path.get(idx), nextPoint = path.get(idx + 1);
		final float dx = (nextPoint.getX() - point.getX()) * (float) offset / stepSpeed;
		final float dy = (nextPoint.getY() - point.getY()) * (float) offset / stepSpeed; 
		
		target.setPoint(point);
		target.setX(point.getX() * Const.TILE_WIDTH + (int) (dx * Const.TILE_WIDTH));
		target.setY(point.getY() * Const.TILE_HEIGHT + (int) (dy * Const.TILE_HEIGHT));
		Events.dispatch(new GuiRepaintEvent());
		
		step += Config.GLOBAL_TIMER_STEP;
	}
}
