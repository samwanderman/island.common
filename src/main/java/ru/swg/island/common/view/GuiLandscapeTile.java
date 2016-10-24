/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import ru.swg.island.common.core.object.LandscapeTile;
import ru.swg.wheelframework.view.figure.Point2D;

/**
 * LandscapeTile
 */
public class GuiLandscapeTile extends GuiTile {
	public GuiLandscapeTile(final LandscapeTile tile) {
		super(tile);
	}
	
	public GuiLandscapeTile(final LandscapeTile tile, final Point2D point) {
		super(tile, point);
	}
}