/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.io.IOException;

import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.wheelframework.event.interfaces.SyncEventInterface;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * Gui object tile
 */
public class GuiObjectTile extends GuiTile implements SyncEventInterface {
	public GuiObjectTile(final ObjectTile tile) 
			throws IOException {
		super(tile);
	}

	/**
	 * Constructor
	 * 
	 * @param tile
	 * @param point
	 */
	public GuiObjectTile(final ObjectTile tile, final Point2D point) 
			throws IOException {
		super(tile, point);
	}
	
	@Override
	public void sync() { }
}
