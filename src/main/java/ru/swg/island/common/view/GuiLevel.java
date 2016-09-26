/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.core.object.TilePoint;
import ru.swg.island.common.io.IO;
import ru.swg.wheelframework.ai.Logic;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.GuiEvent;
import ru.swg.wheelframework.event.event.MouseEvent;
import ru.swg.wheelframework.event.interfaces.MouseEventInterface;
import ru.swg.wheelframework.event.listener.MouseEventListener;
import ru.swg.wheelframework.view.DisplayObject;
import ru.swg.wheelframework.view.Point2D;

/**
 * Gui level
 */
public class GuiLevel extends DisplayObject implements MouseEventInterface {
	// level base info
	private final Level level;
	// gui representation for landscape tiles
	private final List<GuiTile> landscapeTiles = new ArrayList<>();
	// gui representation for object tiles
	private final List<GuiObjectTile> objectTiles = new ArrayList<>();
	// mouse event listener
	private final MouseEventListener mouseEventListener = new MouseEventListener(this);
	
	/**
	 * Constructor
	 * 
	 * @param level
	 */
	public GuiLevel(final Level level) 
			throws IOException {
		this.level = level;
		
		for (final TilePoint tilePoint: level.getLandscapeTiles()) {
			final GuiTile tile = new GuiTile(IO.loadTile(tilePoint.getTile()), tilePoint.getPoint());
			tile.setParent(this);
			landscapeTiles.add(tile);
		}
		Collections.sort(landscapeTiles, new GuiTileComparator());
		
		for (final TilePoint tilePoint: level.getObjectTiles()) {
			final GuiObjectTile tile = new GuiObjectTile(IO.loadTile(tilePoint.getTile()), tilePoint.getPoint());
			tile.setParent(this);
			tile.setSelected(true);
			objectTiles.add(tile);
		}
		Collections.sort(objectTiles, new GuiTileComparator());
	}

	/**
	 * Custom tile painting
	 * FIXME
	 */
	@Override
	public final void paint(final Graphics2D graphics) {
		for (final GuiTile tile: landscapeTiles) {
			final GuiEvent event = new GuiEvent(tile, graphics);
			Events.dispatch(event);
		}
		
		for (final GuiTile tile: objectTiles) {
			final GuiEvent event = new GuiEvent(tile, graphics);
			Events.dispatch(event);
		}
	}
	
	@Override
	protected final void registerListeners() {
		super.registerListeners();
		Events.addListener(MouseEvent.class, mouseEventListener);
	}
	
	@Override
	protected final void unregisterListeners() {
		super.unregisterListeners();
	}
	
	/**
	 * Get path map
	 * 
	 * @return
	 */
	private final int[][] getPathMap() {
		final int[][] map = new int[level.getWidth()][level.getHeight()];
		
		for (final GuiTile tile: landscapeTiles) {
			map[tile.getPoint().getX()][tile.getPoint().getY()] = (tile.getTile().getWeight() >= 0 ? 0 : -1);
		}
		
		return map;
	}
	
	/**
	 * Extract axes point from mouse event
	 * 
	 * @param event
	 * @return
	 */
	private final Point2D extractPointFromEvent(final MouseEvent event) {
		return new Point2D((event.getX() - getAbsoluteX()) / Const.TILE_WIDTH, (event.getY() - getAbsoluteY()) / Const.TILE_HEIGHT);
	}

	@Override
	public final void mouseClick(final MouseEvent event) {
		final Point2D point = extractPointFromEvent(event);
		if ((point.getX() < 0) || (point.getX() >= level.getWidth()) || (point.getY() < 0) || (point.getY() >= level.getHeight())) {
			return;
		}
		final GuiObjectTile tile = objectTiles.get(0);
		tile.setPath(Logic.findPath(getPathMap(), tile.getPoint(), point));
	}

	@Override
	public final void mousePressed(final MouseEvent event) { }

	@Override
	public final void mouseReleased(final MouseEvent event) { }
	
	@Override
	public final void mouseMoved(final MouseEvent event) {}

	@Override
	public final void mouseExited(final MouseEvent event) { }
}
