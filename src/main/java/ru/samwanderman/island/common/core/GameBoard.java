/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.core;

import java.io.IOException;

import ru.samwanderman.island.common.core.object.Level;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.island.common.view.GuiTile;
import ru.samwanderman.wheel.event.event.KeyEvent;
import ru.samwanderman.wheel.event.event.MouseEvent;
import ru.samwanderman.wheel.event.interfaces.KeyEventInterface;
import ru.samwanderman.wheel.event.interfaces.MouseEventInterface;
import ru.samwanderman.wheel.view.DisplayContainer;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Simple GameBoard
 */
public class GameBoard extends DisplayContainer implements MouseEventInterface, KeyEventInterface {		
	private GuiLevel guiLevel;

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
