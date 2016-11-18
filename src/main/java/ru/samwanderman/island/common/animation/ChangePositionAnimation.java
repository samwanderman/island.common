/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.animation;

import java.util.LinkedList;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.island.common.view.GuiTile;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.core.Config;
import ru.samwanderman.wheel.event.Events;
import ru.samwanderman.wheel.event.event.GuiRepaintEvent;
import ru.samwanderman.wheel.event.listener.ObjectListener;
import ru.samwanderman.wheel.view.Image;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Change position animation 
 */
public final class ChangePositionAnimation implements IAnimation {
	private boolean running = false;
	private final GuiTile target;
	private final IAnimation animation;
	private ObjectListener<Object> successCallback;
	private ObjectListener<Object> errorCallback;
	private LinkedList<Point2D> path;
	private int speed;
	private Point2D prevPoint, nextPoint;
	private int step;
	
	public ChangePositionAnimation(final GuiTile target, final IAnimation animation) {
		this.target = target;
		this.animation = animation;
	}
	
	public final void setSuccessCallback(final ObjectListener<Object> successCallback) {
		this.successCallback = successCallback;
	}
	
	public final void setErrorCallback(final ObjectListener<Object> errorCallback) {
		this.errorCallback = errorCallback;
	}

	@Override
	public final void sync() {
		if (!running && (prevPoint != null)) {
			return;
		}
		
		animation.sync();
		
		// only in 1st time and when next step
		if ((nextPoint == null) || (step >= speed)) {
			// detect end of path
			if (path.isEmpty() && (nextPoint != null)) {
				target.setX(nextPoint.getX() * Const.TILE_WIDTH);
				target.setY(nextPoint.getY() * Const.TILE_HEIGHT);
				pause();
				
				// animation ended successfully
				if (successCallback != null) {
					successCallback.on(0);
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
				pause();
				
				if (errorCallback != null) {
					errorCallback.on(path.peekFirst());
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
	public final boolean isRunning() {
		return running;
	}

	@Override
	public final void play() {
		running = true;
		animation.play();
	}

	@Override
	public final void pause() {
		running = false;
		animation.pause();
	}

	@Override
	public final void stop() {
		running = false;
		animation.stop();
		if (successCallback != null) {
			successCallback.on(null);
		}
	}
	
	public final void setPath(final LinkedList<Point2D> path, final int speed) {
		animation.stop();
		this.path = path;
		this.speed = speed / (path.size() - 1);
		prevPoint = null;
		nextPoint = null;
		step = 0;
		
		if (!path.isEmpty()) {
			prevPoint = path.pop();
		}
	}

	@Override
	public final Image getImage() {
		return animation.getImage();
	}
}
