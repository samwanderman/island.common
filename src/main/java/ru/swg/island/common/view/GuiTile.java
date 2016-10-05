/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Graphics2D;
import java.awt.Image;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.Tile;
import ru.swg.wheelframework.view.Point2D;
import ru.swg.wheelframework.view.ui.GuiImage;

public class GuiTile extends GuiImage {
	private final Tile tile;
	private Point2D point;
	
	public GuiTile(final Tile tile) {
		super(tile.getImage());
		this.tile = tile;
	}
	
	public GuiTile(final Tile tile, final Point2D point) {
		this(tile);
		this.point = point;
		setX(point.getX() * Const.TILE_WIDTH);
		setY(point.getY() * Const.TILE_HEIGHT);
		setZ(tile.getZ());
		setWidth(Const.TILE_WIDTH);
		setHeight(Const.TILE_HEIGHT);
	}
	
	@Override
	public void paint(final Graphics2D graphics) {
		final Image img = getImage();
		if (img != null) {
			graphics.drawImage(img, getAbsoluteX() - (img.getWidth(null) - Const.TILE_WIDTH) / 2, getAbsoluteY() - (img.getHeight(null) - Const.TILE_HEIGHT) / 2, null);
		}
	}
	
	public final Tile getTile() {
		return tile;
	}

	public final Point2D getPoint() {
		return point;
	}
	
	public final void setPoint(final Point2D point) {
		this.point = point;
	}
}
