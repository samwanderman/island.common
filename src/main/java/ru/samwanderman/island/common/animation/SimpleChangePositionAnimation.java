/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.animation;

import java.io.IOException;
import java.util.LinkedList;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.island.common.view.GuiUnitTile;
import ru.samwanderman.rtwf.animation.Animation;
import ru.samwanderman.rtwf.core.Config;
import ru.samwanderman.rtwf.event.Events;
import ru.samwanderman.rtwf.event.event.GuiRepaintEvent;
import ru.samwanderman.rtwf.event.interfaces.GuiEventInterface;
import ru.samwanderman.rtwf.event.listener.ObjectListener;
import ru.samwanderman.rtwf.io.Resources;
import ru.samwanderman.rtwf.view.Graphics;
import ru.samwanderman.rtwf.view.figure.Point2D;

/**
 * Animation for simple change position - just coordinates
 */
public final class SimpleChangePositionAnimation extends Animation implements GuiEventInterface {
	private SimpleClipAnimation clipAnimation = null;
	private final GuiUnitTile target;
	private final LinkedList<Point2D> path;
	private final ObjectListener<Integer> onSuccess;
	private final ObjectListener<Point2D> onError;
	private Point2D prevPoint, nextPoint;
	private final int speed;
	private int step;
	
	public SimpleChangePositionAnimation(
			final GuiUnitTile target, 
			final LinkedList<Point2D> path, 
			final int speed, 
			final ObjectListener<Integer> onSuccess, 
			final ObjectListener<Point2D> onError) {
		
		try {
			clipAnimation = new SimpleClipAnimation(target, Resources.loadAnimation("units/unit1/walk"), Config.GLOBAL_TIMER_STEP * 100);
		} catch (final IOException e) { }
		
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
	public final void start() {
		super.start();
		clipAnimation.start();
	}
	
	@Override
	public final void stop() {
		super.stop();
		clipAnimation.stop();
	}
	
	@Override
	public final void reset() {
		stop();
		step = 0;
	}
	
	@Override
	public final void run() {
		// skip if not running
		if (!isRunning() && (prevPoint != null)) {
			return;
		}
		
		clipAnimation.run();
		
		// only in 1st time and when next step
		if ((nextPoint == null) || (step >= speed)) {
			// detect end of path
			if (path.isEmpty() && (nextPoint != null)) {
				target.setX(nextPoint.getX() * Const.TILE_WIDTH);
				target.setY(nextPoint.getY() * Const.TILE_HEIGHT);
				reset();
				
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
				reset();
				
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

	@Override
	public final void paint(final Graphics graphics) {
		if (isRunning() && (clipAnimation != null)) {
			clipAnimation.paint(graphics);
		}
	}
}
