/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.Tile;
import ru.swg.wheelframework.io.ImageCache;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.view.Point2D;
import ru.swg.wheelframework.view.ui.GuiImage;

/**
 * Gui tile
 */
public class GuiTile extends GuiImage {
	private final Tile tile;
	private Point2D point;
	
	/**
	 * Constructor
	 * 
	 * @param path
	 * @throws IOException
	 */
	public GuiTile(final Tile tile, final Point2D point) 
			throws IOException {
		super(tile.getImage());
		this.tile = tile;
		this.point = point;
		ImageCache.set(tile.getImage(), Resources.loadImage(tile.getImage()));
		setX(point.getX() * Const.TILE_WIDTH);
		setY(point.getY() * Const.TILE_HEIGHT);
		setZ(tile.getZ());
		setWidth(Const.TILE_WIDTH);
		setHeight(Const.TILE_HEIGHT);
	}
	
	@Override
	public void paint(final Graphics2D graphics) {
		final Image img = ImageCache.get(getPath());
		if (img != null) {
			graphics.drawImage(img, getAbsoluteX() - (img.getWidth(null) - Const.TILE_WIDTH) / 2, getAbsoluteY() - (img.getHeight(null) - Const.TILE_HEIGHT) / 2, null);
		}
	}
	
	/**
	 * Get tile
	 * 
	 * @return
	 */
	public final Tile getTile() {
		return tile;
	}

	/**
	 * Get point
	 * 
	 * @return
	 */
	public final Point2D getPoint() {
		return point;
	}
	
	/**
	 * Set point
	 * 
	 * @param point
	 */
	public final void setPoint(final Point2D point) {
		this.point = point;
	}
}
