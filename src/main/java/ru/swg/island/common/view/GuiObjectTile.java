/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;

import ru.swg.island.common.animation.SimpleChangePositionAnimation;
import ru.swg.island.common.core.object.Tile;
import ru.swg.wheelframework.core.Config;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.SyncEvent;
import ru.swg.wheelframework.event.interfaces.SyncEventInterface;
import ru.swg.wheelframework.event.listener.SyncEventListener;
import ru.swg.wheelframework.view.Point2D;

/**
 * Gui object tile
 */
public class GuiObjectTile extends GuiTile implements SyncEventInterface {
	private SimpleChangePositionAnimation animChangePos = null;
	private boolean selected = false;
	
	// sync event listener
	private final SyncEventListener syncEventListener = new SyncEventListener(this);
	
	/**
	 * Set element selected
	 * 
	 * @param selected
	 */
	protected final void setSelected(final boolean selected) {
		this.selected = selected;
	}

	@Override
	protected final void registerListeners() {
		super.registerListeners();
		Events.addListener(SyncEvent.class, syncEventListener);
	}
	
	@Override
	protected final void unregisterListeners() {
		super.unregisterListeners();
		Events.removeListener(SyncEvent.class, syncEventListener);
	}
	
	@Override
	public void paint(final Graphics2D graphics) {
		if (animChangePos != null) {
			animChangePos.run();
		}
		super.paint(graphics);
		graphics.setColor(Color.GREEN);
		graphics.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
	}
	
	/**
	 * Constructor
	 * 
	 * @param tile
	 * @param point
	 */
	public GuiObjectTile(final Tile tile, final Point2D point) 
			throws IOException {
		super(tile, point);
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
		
		animChangePos = new SimpleChangePositionAnimation(this, path, (path.size() - 1) * Config.GLOBAL_TIMER_STEP * 100);
		animChangePos.start();
	}
	
	@Override
	public final void sync() {
		if (animChangePos != null) {
			animChangePos.run();
		}
	}
}
