/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.animation;

import java.util.LinkedList;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.view.GuiLevel;
import ru.swg.island.common.view.GuiUnitTile;
import ru.swg.wheelframework.animation.Animation;
import ru.swg.wheelframework.core.Config;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.GuiRepaintEvent;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * Animation for simple change position - just coordinates
 */
public final class SimpleChangePositionAnimation implements Animation {
	private final GuiUnitTile target;
	private final LinkedList<Point2D> path;
	private final ObjectListener<Integer> onSuccess;
	private final ObjectListener<Point2D> onError;
	private Point2D prevPoint, nextPoint;
	private final int speed;
	private int step;
	
	private boolean running = false;
	
	public SimpleChangePositionAnimation(
			final GuiUnitTile target, 
			final LinkedList<Point2D> path, 
			final int speed, 
			final ObjectListener<Integer> onSuccess, 
			final ObjectListener<Point2D> onError) {
		this.target = target;
		this.path = path;
		this.speed = speed / (path.size() - 1);
		this.onSuccess = onSuccess;
		this.onError = onError;
		step = 0;
		
		if (!path.isEmpty()) {
			prevPoint = path.pop();
		}
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
		// skip if not running
		if (!running && (prevPoint != null)) {
			return;
		}
		
		// only in 1st time and when next step
		if ((nextPoint == null) || (step >= speed)) {
			// detect end of path
			if (path.isEmpty()) {
				target.setX(nextPoint.getX() * Const.TILE_WIDTH);
				target.setY(nextPoint.getY() * Const.TILE_HEIGHT);
				stop();
				
				// animation ended successfully
				if (onSuccess != null) {
					onSuccess.on(0);
				}
				return;
			}
			
			// check cell status
			switch (((GuiLevel) target.getParent()).getPointStatus(path.peekFirst())) {
			// cell available
			case Config.CELL_AVAILABLE:
				if (nextPoint != null) {
					prevPoint = nextPoint;
				}
				nextPoint = path.pop();
				target.setPoint(nextPoint);
				target.setX(prevPoint.getX() * Const.TILE_WIDTH);
				target.setY(prevPoint.getY() * Const.TILE_HEIGHT);
				step = 0;
				
				break;
				
			// cell busy
			case Config.CELL_BUSY:
				return;
				
			default:
				stop();
				if (onError != null) {
					onError.on(path.peekFirst());
				}
				return;
			}
		}
		
		final float dx = (nextPoint.getX() - prevPoint.getX()) * Const.TILE_WIDTH * ((float) step / speed);
		final float dy = (nextPoint.getY() - prevPoint.getY()) * Const.TILE_HEIGHT * ((float) step / speed);
			
		target.setX(prevPoint.getX() * Const.TILE_WIDTH + (int) dx);
		target.setY(prevPoint.getY() * Const.TILE_HEIGHT + (int) dy);
			
		Events.dispatch(new GuiRepaintEvent());
			
		step += Config.GLOBAL_TIMER_STEP;
	}
}
