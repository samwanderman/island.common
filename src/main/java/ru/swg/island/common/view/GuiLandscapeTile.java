/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.io.IOException;

import ru.swg.island.common.core.object.LandscapeTile;
import ru.swg.wheelframework.view.Point2D;

/**
 * LandscapeTile
 */
public class GuiLandscapeTile extends GuiTile {
	public GuiLandscapeTile(final LandscapeTile tile) 
			throws IOException {
		super(tile);
	}
	
	/**
	 * Constructor
	 * 
	 * @param tile
	 * @param point
	 * @throws IOException
	 */
	public GuiLandscapeTile(final LandscapeTile tile, final Point2D point)
			throws IOException {
		super(tile, point);
	}
}
