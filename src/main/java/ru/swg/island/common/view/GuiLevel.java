/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.LandscapeTile;
import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.Tile;
import ru.swg.island.common.core.object.TilePoint;
import ru.swg.island.common.io.IO;
import ru.swg.wheelframework.ai.Logic;
import ru.swg.wheelframework.event.Events;
import ru.swg.wheelframework.event.event.GuiEvent;
import ru.swg.wheelframework.event.event.KeyEvent;
import ru.swg.wheelframework.event.event.MouseEvent;
import ru.swg.wheelframework.event.interfaces.KeyEventInterface;
import ru.swg.wheelframework.event.interfaces.MouseEventInterface;
import ru.swg.wheelframework.event.listener.KeyEventListener;
import ru.swg.wheelframework.event.listener.MouseEventListener;
import ru.swg.wheelframework.view.DisplayObject;
import ru.swg.wheelframework.view.Point2D;

/**
 * Gui level
 */
public class GuiLevel extends DisplayObject implements MouseEventInterface, KeyEventInterface {
	// level base info
	private final Level level;
	
	// tiles
	private final List<GuiTile> landscapeTiles = new ArrayList<>();
	private final List<GuiObjectTile> objectTiles = new ArrayList<>();

	// listeners
	private final MouseEventListener mouseEventListener = new MouseEventListener(this);
	private final KeyEventListener keyEventListener = new KeyEventListener(this); 
	
	// edit mode
	private boolean editMode = false;
	private boolean showCoords = false;
	
	private Image flowImage;
	private int flowImageX, flowImageY;
	
	/**
	 * Constructor
	 * 
	 * @param level
	 */
	public GuiLevel(final Level level) 
			throws IOException {
		this.level = level;
		showCoords = false;
		
		if (level.getLandscapeTiles() != null) {
			for (final TilePoint tilePoint: level.getLandscapeTiles()) {
				final GuiTile tile = new GuiTile(IO.loadTile(tilePoint.getTile(), Tile.class), tilePoint.getPoint());
				tile.setParent(this);
				landscapeTiles.add(tile);
			}
			Collections.sort(landscapeTiles, new GuiTileComparator());
		}
		
		if (level.getObjectTiles() != null) {
			for (final TilePoint tilePoint: level.getObjectTiles()) {
				final GuiObjectTile tile = new GuiObjectTile(IO.loadTile(tilePoint.getTile(), Tile.class), tilePoint.getPoint());
				tile.setParent(this);
				tile.setSelected(true);
				objectTiles.add(tile);
			}
			Collections.sort(objectTiles, new GuiTileComparator());
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param level
	 * @param editMode
	 */
	public GuiLevel(final Level level, final boolean editMode) 
			throws IOException {
		this(level);
		this.editMode = editMode;
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
		
		if (showCoords) {
			for (int i = 0; i <= level.getWidth(); i++) {
				graphics.draw(new Line2D.Float(getAbsoluteX() + i * Const.TILE_WIDTH, getAbsoluteY(), getAbsoluteX() + i * Const.TILE_WIDTH, getAbsoluteY() + Const.TILE_HEIGHT * level.getHeight()));	
			}
			
			for (int i = 0; i <= level.getHeight(); i++) {
				graphics.draw(new Line2D.Float(getAbsoluteX(), getAbsoluteY() + i * Const.TILE_HEIGHT, getAbsoluteX() + Const.TILE_WIDTH * level.getWidth(), getAbsoluteY() + i * Const.TILE_HEIGHT));
			}
		}
		
		if (flowImage != null) {
			graphics.drawImage(flowImage, flowImageX, flowImageY, null);
		}
	}
	
	@Override
	protected final void registerListeners() {
		super.registerListeners();
		Events.addListener(MouseEvent.class, mouseEventListener);
		Events.addListener(KeyEvent.class, keyEventListener);
	}
	
	@Override
	protected final void unregisterListeners() {
		super.unregisterListeners();
		Events.removeListener(MouseEvent.class, mouseEventListener);
		Events.removeListener(KeyEvent.class, keyEventListener);
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
	public final int getWidth() {
		return level.getWidth() * Const.TILE_WIDTH;
	}
	
	@Override
	public final int getHeight() {
		return level.getHeight() * Const.TILE_HEIGHT;
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
	public final void mouseMoved(final MouseEvent event) {
		if (flowImage != null) {
			flowImageX = event.getX();
			flowImageY = event.getY();
		}
	}

	@Override
	public final void mouseExited(final MouseEvent event) { }

	@Override
	public final void keyTyped(final KeyEvent event) { }

	@Override
	public final void keyPressed(final KeyEvent event) { 
		if (event.getCode() == 109) {
			showCoords = !showCoords;
		}
	}

	@Override
	public final void keyReleased(final KeyEvent event) { }
	
	public final void addLandscapeTile(final LandscapeTile tile, final Point2D point) 
			throws IOException {
		landscapeTiles.add(new GuiTile(tile, point));
	}
	
	public final void addObjectTile(final ObjectTile tile, final Point2D point) 
			throws IOException {
		objectTiles.add(new GuiObjectTile(tile, point));
	}
	
	public final void addFlowImage(final Image image, final int x, final int y) {
		flowImage = image;
		flowImageX = x;
		flowImageY = y;
	}
	
	public final void removeFlowImage() {
		flowImage = null;
		flowImageX = 0;
		flowImageY = 0;
	}
}
