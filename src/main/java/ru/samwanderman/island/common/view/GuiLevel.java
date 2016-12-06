/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.core.object.LandscapeTile;
import ru.samwanderman.island.common.core.object.Level;
import ru.samwanderman.island.common.core.object.ObjectTile;
import ru.samwanderman.island.common.core.object.UnitTile;
import ru.samwanderman.island.common.view.tile.GuiLandscapeTile;
import ru.samwanderman.island.common.view.tile.GuiObjectTile;
import ru.samwanderman.island.common.view.tile.GuiTile;
import ru.samwanderman.island.common.view.tile.GuiUnitTile;
import ru.samwanderman.island.common.view.tile.TileComparator;
import ru.samwanderman.wheel.ai.Logic;
import ru.samwanderman.wheel.core.Config;
import ru.samwanderman.wheel.event.Events;
import ru.samwanderman.wheel.event.event.GuiEvent;
import ru.samwanderman.wheel.event.event.IKeyEvent;
import ru.samwanderman.wheel.event.event.IMouseEvent;
import ru.samwanderman.wheel.event.event.ISyncEvent;
import ru.samwanderman.wheel.event.event.KeyEvent;
import ru.samwanderman.wheel.event.event.MouseEvent;
import ru.samwanderman.wheel.log.Log;
import ru.samwanderman.wheel.view.Color;
import ru.samwanderman.wheel.view.DisplayObject;
import ru.samwanderman.wheel.view.Graphics;
import ru.samwanderman.wheel.view.figure.Point2D;
import ru.samwanderman.wheel.view.figure.Rectangle;

/**
 * Gui level
 */
public class GuiLevel extends DisplayObject implements IMouseEvent, IKeyEvent, ISyncEvent {
	private static final int PLAYER_COMMAND = 1;
	// level base info
	private final Level level;
	
	// tiles
	private final List<GuiLandscapeTile> landscapeTiles = new ArrayList<>();
	private final List<GuiObjectTile> objectTiles = new ArrayList<>();
	
	// edit mode
	private boolean editMode = false;
	private boolean showCoords = false;

	private GuiTile intendedTile;
	private List<GuiTile> selectedTiles = new ArrayList<>();
	
	private Rectangle selection;
	
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
	public final void paint(final Graphics graphics) {
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
				graphics.drawLine(getAbsoluteX() + i * Const.TILE_WIDTH, getAbsoluteY(), getAbsoluteX() + i * Const.TILE_WIDTH, getAbsoluteY() + Const.TILE_HEIGHT * level.getHeight());	
			}
			
			for (int i = 0; i <= level.getHeight(); i++) {
				graphics.drawLine(getAbsoluteX(), getAbsoluteY() + i * Const.TILE_HEIGHT, getAbsoluteX() + Const.TILE_WIDTH * level.getWidth(), getAbsoluteY() + i * Const.TILE_HEIGHT);
			}
		}
		
		if (intendedTile != null) {
			graphics.drawImage(intendedTile.getImage(), intendedTile.getAbsoluteX(), intendedTile.getAbsoluteY());
		}
		
		if (selection != null) {
			graphics.setColor(Color.GREEN);
			graphics.draw(selection);
		}
	}
	
	/**
	 * Get path map
	 * 
	 * @return
	 */
	public final int[][] getPathMap() {
		final int[][] map = new int[level.getWidth()][level.getHeight()];
		
		for (final GuiTile tile: landscapeTiles) {
			map[tile.getPoint().getX()][tile.getPoint().getY()] = (tile.getTile().getWeight() >= 0 ? 0 : Config.CELL_UNAVAILABLE);
		}
		
		for (final GuiTile tile: objectTiles) {
			if (tile instanceof GuiUnitTile) {
				map[tile.getPoint().getX()][tile.getPoint().getY()] = ((GuiUnitTile) tile).isChangePositionAnimationRunning() ? Config.CELL_BUSY : Config.CELL_TEMPORARILY_UNAVAILABLE;				
			}
			map[tile.getPoint().getX()][tile.getPoint().getY()] = Config.CELL_UNAVAILABLE;
		}
		
		return map;
	}
	
	/**
	 * Check point status
	 * 
	 * @param point
	 * @return
	 */
	public final int getPointStatus(final Point2D point) {
		for (final GuiTile tile: landscapeTiles) {
			if (tile.getPoint().equals(point)) {
				if (tile.getTile().getWeight() < 0) {
					return Config.CELL_UNAVAILABLE;
				}
			}
		}
		
		for (final GuiTile tile: objectTiles) {
			if (tile.getPoint().equals(point)) {
				if (tile instanceof GuiUnitTile) {
					return ((GuiUnitTile) tile).isChangePositionAnimationRunning() ? Config.CELL_BUSY : Config.CELL_TEMPORARILY_UNAVAILABLE;				
				}
				return Config.CELL_UNAVAILABLE;
			}
		}
		
		return Config.CELL_AVAILABLE;
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
					intendedTile.setPoint(point);
					addTile(intendedTile);
					setIntentTile(null);
				} catch (final IOException err) { }
				return;
			}
			
			intendedTile = unbindTileAtPoint(point);
		} else {
			switch (event.getNum()) {
			case 1:
				clearSelectedTiles();
				final GuiTile _tile = getObjectAtPoint(point);
				if (_tile != null) {
					if (((ObjectTile) _tile.getTile()).getGameCommand() == PLAYER_COMMAND) {
						selectedTiles.add(_tile);
					}
				}
				break;
			case 3:
				if (!selectedTiles.isEmpty()) {
					for (final GuiTile tile: selectedTiles) {
						if (tile instanceof GuiUnitTile) {
							((GuiUnitTile) tile).setPath(Logic.getFindPathAlgorithm().find(getPathMap(), tile.getPoint(), point));		
						}
					}
				}
				break;
			default:
			}
		}
	}

	@Override
	public final void mousePressed(final MouseEvent event) {
		if (event.getNum() == 1) {
			selection = new Rectangle(event.getX(), event.getY(), 0, 0);
		}
	}

	@Override
	public final void mouseReleased(final MouseEvent event) {
		if (selection != null) {
			clearSelectedTiles();
			setSelectedTilesBySelection();
			selection = null;	
		}
	}
	
	@Override
	public final void mouseMoved(final MouseEvent event) {		
		if (intendedTile != null) {
			intendedTile.setX(event.getX() - getAbsoluteX() - intendedTile.getWidth() / 2);
			intendedTile.setY(event.getY() - getAbsoluteY() - intendedTile.getHeight() / 2);
		}
		
		if (selection != null) {
			selection = new Rectangle((int) selection.getX(), (int) selection.getY(), (int)(event.getX() - selection.getX()), (int)(event.getY() - selection.getY()));
		}
	}

	@Override
	public final void mouseExited(final MouseEvent event) { }

	@Override
	public final void keyTyped(final KeyEvent event) {
		switch (event.getCode()) {
		case 27: // ESC
			clearSelectedTiles();
			break;
		case 109: // M
			showCoords = !showCoords;
			break;
		case 127: // DELETE
			setIntentTile(null);
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
	public final <T extends GuiTile> void addTile(final T tile) 
			throws IOException {
		if (tile instanceof GuiLandscapeTile) {
			level.setLandscapeTile((LandscapeTile) tile.getTile());
		} else if (tile instanceof GuiUnitTile) {
			level.setUnitTile((UnitTile) tile.getTile());
		} else if (tile instanceof GuiObjectTile) {
			level.setObjectTile((ObjectTile) tile.getTile());
		}
		
		update();
	}
	
	@Override
	protected final void update() {
		landscapeTiles.clear();
		for (final LandscapeTile landscapeTile: level.getLandscapeTiles()) {
			final GuiLandscapeTile tile = new GuiLandscapeTile(this, landscapeTile);
			tile.setParent(this);
			landscapeTiles.add(tile);
		}
		Collections.sort(landscapeTiles, new TileComparator<GuiLandscapeTile>());
		
		objectTiles.clear();
		for (final ObjectTile objectTile: level.getObjectTiles()) {
			try {
				final GuiObjectTile tile = new GuiObjectTile(this, objectTile);
				tile.setParent(this);
				objectTiles.add(tile);
			} catch (final IOException e) {
				Log.error(e.getLocalizedMessage());
				Log.error("Can't load object tile " + objectTile.getName());
			}
		}
		
		for (final UnitTile unitTile: level.getUnitTiles()) {
			try {
				final GuiUnitTile tile = new GuiUnitTile(this, unitTile);
				tile.setParent(this);
				objectTiles.add(tile);
			} catch (final IOException e) {
				Log.error(e.getLocalizedMessage());
				Log.error("Can't load unit tile " + unitTile.getName());
			}
		}
		Collections.sort(objectTiles, new TileComparator<GuiObjectTile>());

		super.update();
	}
	
	public final <T extends GuiTile> void setIntentTile(final T tile) {
		intendedTile = tile;
	}
		
	public final Level getLevel() {
		return level;
	}
	
	/**
	 * Unbind tile at point
	 * 
	 * @param point
	 * @return
	 */
	private final GuiTile unbindTileAtPoint(final Point2D point) {
		GuiTile tile = null;
		
		for (final GuiObjectTile objectTile: objectTiles) {
			if (objectTile.getPoint().equals(point)) {
				objectTile.setSelected(true);
				objectTiles.remove(objectTile);
				return objectTile;
			}
		}
		
		for (final GuiLandscapeTile landscapeTile: landscapeTiles) {
			if (landscapeTile.getPoint().equals(point)) {
				landscapeTile.setSelected(true);
				landscapeTiles.remove(landscapeTile);
				return landscapeTile;
			}			
		}
		
		return tile;
	}

	/**
	 * Get tile at point
	 * 
	 * @param point
	 * @return
	 */
	public final GuiObjectTile getObjectAtPoint(final Point2D point) {
		for (final GuiObjectTile objectTile: objectTiles) {
			if (objectTile.getPoint().equals(point)) {
				objectTile.setSelected(true);
				return objectTile;
			}
		}
		
		return null;
	}
	
	private final void clearSelectedTiles() {
		for (final GuiTile tile: selectedTiles) {
			tile.setSelected(false);
		}
		selectedTiles.clear();
	}
	
	private final void setSelectedTilesBySelection() {
		for (final GuiObjectTile tile: objectTiles) {
			if ((tile instanceof GuiUnitTile) && selection.contains(tile.getBoundRect())) {
				if (((ObjectTile) tile.getTile()).getGameCommand() == PLAYER_COMMAND) {
					tile.setSelected(true);
					selectedTiles.add(tile);
				}
			}
		}
	}

	@Override
	public final void sync() {
		for (final GuiObjectTile tiles: objectTiles) {
			tiles.sync();
		}
	}
}
