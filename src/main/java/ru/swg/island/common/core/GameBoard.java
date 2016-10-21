/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.core;

import java.io.IOException;

import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.view.GuiLevel;
import ru.swg.island.common.view.GuiTile;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.KeyEvent;
import ru.swg.wheelframework.event.event.MouseEvent;
import ru.swg.wheelframework.event.interfaces.KeyEventInterface;
import ru.swg.wheelframework.event.interfaces.MouseEventInterface;
import ru.swg.wheelframework.event.listener.KeyEventListener;
import ru.swg.wheelframework.event.listener.MouseEventListener;
import ru.swg.wheelframework.view.DisplayContainer;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * Simple GameBoard
 */
public class GameBoard extends DisplayContainer implements MouseEventInterface, KeyEventInterface {	
	// listeners
	private final MouseEventListener mouseEventListener = new MouseEventListener(this);
	private final KeyEventListener keyEventListener = new KeyEventListener(this);
	
	private GuiLevel guiLevel;

	// listeners
	@Override
	protected final void registerListeners() {
		super.registerListeners();
		Events.addListener(MouseEvent.class, mouseEventListener);
		Events.addListener(KeyEvent.class, keyEventListener);
	};
	
	@Override
	protected final void unregisterListeners() {
		super.unregisterListeners();
		Events.removeListener(MouseEvent.class, mouseEventListener);
		Events.removeListener(KeyEvent.class, keyEventListener);
	};

	// Mouse funcs
	@Override
	public final void mouseClick(final MouseEvent event) {
		if (event.getNum() == 3) {
			if (guiLevel != null) {
				guiLevel.setIntentTile(null);
			}
		}
	}

	@Override
	public final void mousePressed(final MouseEvent event) { }

	@Override
	public final void mouseReleased(final MouseEvent event) { }

	@Override
	public final void mouseMoved(final MouseEvent event) { }
	
	@Override
	public final void mouseExited(final MouseEvent event) { }

	@Override
	public final void keyTyped(final KeyEvent event) {
		switch (event.getCode()) {
		case 27:
			if (guiLevel != null) {
				guiLevel.setIntentTile(null);
			}
			break;
		default:
		}
	}
	
	@Override
	public final void keyPressed(final KeyEvent event) { }

	@Override
	public final void keyReleased(final KeyEvent event) { }
	
	/**
	 * Load level on board
	 * 
	 * @param level
	 * @throws IOException
	 */
	public final void loadLevel(final Level level, final boolean editMode)
			throws IOException {
		removeChild(guiLevel);
		guiLevel = new GuiLevel(level, editMode);
		addChild(guiLevel);
		setWidth(guiLevel.getWidth());
		setHeight(guiLevel.getHeight());
	}
	
	@Override
	public final int getWidth() {
		if (guiLevel == null) {
			return super.getWidth();
		}
		
		return guiLevel.getWidth();
	}
	
	@Override
	public final int getHeight() {
		if (guiLevel == null) {
			return super.getHeight();
		}
		
		return guiLevel.getHeight();
	}
	
	public final <T extends GuiTile> void setIntentTile(final T tile) {
		guiLevel.setIntentTile(tile);
	}
	
	public final <T extends GuiTile> void addTile(final T tile, final Point2D point)
			throws IOException {
		guiLevel.addTile(tile, point);
	}
	
	public final Level getLevel() {
		return guiLevel.getLevel();
	}
}
