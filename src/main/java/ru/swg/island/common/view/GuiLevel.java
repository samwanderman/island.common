/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.LandscapeTile;
import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.TilePoint;
import ru.swg.island.common.core.object.UnitTile;
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
import ru.swg.wheelframework.log.Log;
import ru.swg.wheelframework.view.DisplayObject;
import ru.swg.wheelframework.view.Point2D;

/**
 * Gui level
 */
public class GuiLevel extends DisplayObject implements MouseEventInterface, KeyEventInterface {
	// level base info
	private final Level level;
	
	// tiles
	private final List<GuiLandscapeTile> landscapeTiles = new ArrayList<>();
	private final List<GuiObjectTile> objectTiles = new ArrayList<>();

	// listeners
	private final MouseEventListener mouseEventListener = new MouseEventListener(this);
	private final KeyEventListener keyEventListener = new KeyEventListener(this); 
	
	// edit mode
	private boolean editMode = false;
	private boolean showCoords = false;

	private GuiTile intendedTile;
	
	public GuiLevel(final Level level) {
		this.level = level;
		showCoords = false;
		update();
	}
	
	public GuiLevel(final Level level, final boolean editMode) {
		this.level = level;
		showCoords = false;
		this.editMode = editMode;
		update();
	}

	/**
	 * Custom tile painting
	 */
	@Override
	public final void paint(final Graphics2D graphics) {
		for (final GuiLandscapeTile tile: landscapeTiles) {
			final GuiEvent event = new GuiEvent(tile, graphics);
			Events.dispatch(event);
		}
		
		for (final GuiObjectTile tile: objectTiles) {
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
		
		if (intendedTile != null) {
			graphics.drawImage(intendedTile.getImage(), intendedTile.getAbsoluteX(), intendedTile.getAbsoluteY(), null);
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
	public Rectangle getBoundRect() {
		return new Rectangle(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
	}
	
	@Override
	public final void mouseClick(final MouseEvent event) {
		final Point2D point = extractPointFromEvent(event);
		if ((point.getX() < 0) || (point.getX() >= level.getWidth()) || (point.getY() < 0) || (point.getY() >= level.getHeight())) {
			return;
		}
		
		if (editMode) {
			if (intendedTile != null) {
				try {
					addTile(intendedTile, point);
				} catch (final IOException err) { }
				return;
			}
		} else {
			final GuiObjectTile tile = objectTiles.get(0);
			tile.setPath(Logic.findPath(getPathMap(), tile.getPoint(), point));
		}
	}

	@Override
	public final void mousePressed(final MouseEvent event) { }

	@Override
	public final void mouseReleased(final MouseEvent event) { }
	
	@Override
	public final void mouseMoved(final MouseEvent event) {
		if (intendedTile != null) {
			intendedTile.setX(event.getX() - getAbsoluteX() - intendedTile.getWidth() / 2);
			intendedTile.setY(event.getY() - getAbsoluteY() - intendedTile.getHeight() / 2);
		}
	}

	@Override
	public final void mouseExited(final MouseEvent event) { }

	@Override
	public final void keyTyped(final KeyEvent event) {
		switch (event.getCode()) {
		case 27: // ESC
			break;
		case 109: // M
			showCoords = !showCoords;
			break;
		case 127: // DELETE
			break;
		default:
		}
	}

	@Override
	public final void keyPressed(final KeyEvent event) { }

	@Override
	public final void keyReleased(final KeyEvent event) { }
	
	/**
	 * Add tile to board
	 * 
	 * @param tile
	 * @param point
	 * @throws IOException
	 */
	public final <T extends GuiTile> void addTile(final T tile, final Point2D point) 
			throws IOException {
		if (tile instanceof GuiLandscapeTile) {
			level.setLandscapeTile(new TilePoint(tile.getTile().getId(), point));
		} else if (tile instanceof GuiUnitTile) {
			level.setUnitTile(new TilePoint(tile.getTile().getId(), point));
		} else if (tile instanceof GuiObjectTile) {
			level.setObjectTile(new TilePoint(tile.getTile().getId(), point));
		}
		
		update();
	}
	
	@Override
	protected final void update() {
		landscapeTiles.clear();
		for (final TilePoint tilePoint: level.getLandscapeTiles()) {
			try {
				final GuiLandscapeTile tile = new GuiLandscapeTile(IO.loadTile("./landscape/" + tilePoint.getTile(), LandscapeTile.class), tilePoint.getPoint());
				tile.setParent(this);
				landscapeTiles.add(tile);
			} catch (final IOException e) {
				Log.error("Can't load landscape tile " + tilePoint.getTile());
			}
		}
		Collections.sort(landscapeTiles, new TileComparator<GuiLandscapeTile>());
		
		objectTiles.clear();
		for (final TilePoint tilePoint: level.getObjectTiles()) {
			try {
				final GuiObjectTile tile = new GuiObjectTile(IO.loadTile("./objects/" + tilePoint.getTile(), ObjectTile.class), tilePoint.getPoint());
				tile.setParent(this);
				objectTiles.add(tile);
			} catch (final IOException e) {
				Log.error(e.getLocalizedMessage());
				Log.error("Can't load object tile " + tilePoint.getTile());
			}
		}
		
		for (final TilePoint tilePoint: level.getUnitTiles()) {
			try {
				final GuiUnitTile tile = new GuiUnitTile(IO.loadTile("./units/" + tilePoint.getTile(), UnitTile.class), tilePoint.getPoint());
				tile.setParent(this);
				objectTiles.add(tile);
			} catch (final IOException e) {
				Log.error(e.getLocalizedMessage());
				Log.error("Can't load unit tile " + tilePoint.getTile());
			}
		}
		Collections.sort(objectTiles, new TileComparator<GuiObjectTile>());

		super.update();
	}
	
	public final <T extends GuiTile> void setIntentTile(final T tile) {
		intendedTile = tile;
	}
	
	private final boolean hasTileAtPoint(final Point2D point) {
		for (final GuiLandscapeTile tile: landscapeTiles) {
			if (tile.getPoint().equals(point)) {
				return true;
			}
		}
		
		for (final GuiObjectTile tile: objectTiles) {
			if (tile.getPoint().equals(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	private final boolean removeLandscapeTile(final Point2D point) {
		for (final GuiLandscapeTile tile: landscapeTiles) {
			if (tile.getPoint().equals(point)) {
				return landscapeTiles.remove(tile);
			}
		}
		
		return false;
	}
	
	private final boolean removeObjectTile(final Point2D point) {
		for (final GuiObjectTile tile: objectTiles) {
			if (tile.getPoint().equals(point)) {
				return objectTiles.remove(tile);
			}
		}	
		
		return false;
	}
	
	private final void removeTilesAtPoints(final List<Point2D> points) {
		for (final Point2D point: points) {
			level.removeObjectTile(point);
			removeObjectTile(point);
			level.removeLandscapeTile(point);
			removeLandscapeTile(point);
			level.removeUnitTile(point);
			removeObjectTile(point);
		}
	}
	
	public final Level getLevel() {
		return level;
	}
}
