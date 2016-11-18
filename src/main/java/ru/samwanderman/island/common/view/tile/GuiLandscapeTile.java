/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view.tile;

import ru.samwanderman.island.common.core.object.LandscapeTile;
import ru.samwanderman.wheel.view.figure.Point2D;

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
