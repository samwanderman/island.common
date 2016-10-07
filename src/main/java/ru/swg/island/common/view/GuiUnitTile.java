/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.io.IOException;

import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.UnitTile;
import ru.swg.wheelframework.view.Point2D;

/**
 *
 */
public class GuiUnitTile extends GuiObjectTile {
	public GuiUnitTile(final UnitTile tile) 
			throws IOException {
		super(tile);
	}
	
	public GuiUnitTile(final ObjectTile tile, final Point2D point) 
			throws IOException {
		super(tile, point);
	}
}
