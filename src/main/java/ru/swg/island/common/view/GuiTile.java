/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.Tile;
import ru.swg.wheelframework.view.Color;
import ru.swg.wheelframework.view.Graphics;
import ru.swg.wheelframework.view.Image;
import ru.swg.wheelframework.view.figure.Point2D;
import ru.swg.wheelframework.view.ui.GuiImage;

public class GuiTile extends GuiImage {
	private final Tile tile;
	private Point2D point;
	private boolean selected = false;
	
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
	public void paint(final Graphics graphics) {
		final Image img = getImage();
		if (img != null) {
			graphics.drawImage(img, getAbsoluteX() - (img.getWidth() - Const.TILE_WIDTH) / 2, getAbsoluteY() - (img.getHeight() - Const.TILE_HEIGHT) / 2);
		}
		
		paintSelection(graphics);
	}
	
	protected final void paintSelection(final Graphics graphics) {
		if (selected) {
			graphics.setColor(Color.GREEN);
			graphics.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
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
	
	public final void setSelected(final boolean selected) {
		this.selected = selected;
	}
}
