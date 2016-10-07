/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.core.object;

import java.util.ArrayList;
import java.util.List;

import ru.swg.wheelframework.view.Point2D;

/**
 * Level
 */
public final class Level {
	// id
	private String id;
	private String name;
	private String description;
	// width in cells
	private int width;
	// height in cells
	private int height;
	// landscape tiles list
	private List<TilePoint> landscapeTiles = new ArrayList<>();
	// object tiles
	private List<TilePoint> objectTiles = new ArrayList<>();
	// units tiles
	private List<TilePoint> unitTiles = new ArrayList<>();
	
	/**
	 * Set id
	 * 
	 * @param id
	 */
	public final void setId(final String id) {
		this.id = id;
	}
	
	/**
	 * Get id
	 * 
	 * @return
	 */
	public final String getId() {
		return id;
	}
	
	/**
	 * Set name
	 * 
	 * @param name
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Get name
	 * 
	 * @return
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Set description
	 * 
	 * @param description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * Get description
	 * 
	 * @return
	 */
	public final String getDescription() {
		return description;
	}
	
	/**
	 * Get width
	 * 
	 * @return
	 */
	public final int getWidth() {
		return width;
	}
	
	/**
	 * Set width
	 * 
	 * @param width
	 */
	public final void setWidth(final int width) {
		this.width = width;
	}
	
	/**
	 * Get height
	 * 
	 * @return
	 */
	public final int getHeight() {
		return height;
	}
	
	/**
	 * Set height
	 * 
	 * @param height
	 */
	public final void setHeight(final int height) {
		this.height = height;
	}
	
	/**
	 * Get landscape tiles
	 * 
	 * @return
	 */
	public final List<TilePoint> getLandscapeTiles() {
		return landscapeTiles;
	}

	/**
	 * Set landscape tiles
	 * 
	 * @param tiles
	 */
	public final void setLandscapeTiles(final List<TilePoint> tiles) {
		if (tiles != null) {
			landscapeTiles = tiles;
		} else {
			landscapeTiles.clear();
		}
	}	
	
	/**
	 * Add new landscape tile
	 * 
	 * @param tile
	 */
	public final void setLandscapeTile(final TilePoint tile) {
		for (int i = 0; i < landscapeTiles.size(); i++) {
			if (landscapeTiles.get(i).getPoint().equals(tile.getPoint())) {
				landscapeTiles.set(i, tile);
				return;
			}
		}
		
		landscapeTiles.add(tile);
	}
	
	public final boolean removeLandscapeTile(final Point2D point) {
		for (final TilePoint tile: landscapeTiles) {
			if (tile.getPoint().equals(point)) {
				final boolean res = landscapeTiles.remove(tile);
				landscapeTiles.add(new TilePoint("empty", point));
				return res;
			}
		}
		
		return false;
	}
	
	public final boolean hasLandscapeTile(final Point2D point) {
		for (final TilePoint tile: landscapeTiles) {
			if (tile.getPoint().equals(point)) {
				return true;
			}
		}		
		
		return false;
	}
	
	/**
	 * Get object tiles
	 * 
	 * @return
	 */
	public final List<TilePoint> getObjectTiles() {
		return objectTiles;
	}
	
	/**
	 * Set object tiles
	 * 
	 * @param tiles
	 */
	public final void setObjectTiles(final List<TilePoint> tiles) {
		if (tiles != null) {
			objectTiles = tiles;
		} else {
			objectTiles.clear();
		}
	}
	
	/**
	 * Add object tile
	 * 
	 * @param tile
	 */
	public final void setObjectTile(final TilePoint tile) {
		for (int i = 0; i < objectTiles.size(); i++) {
			if (objectTiles.get(i).getPoint().equals(tile.getPoint())) {
				objectTiles.set(i, tile);
				return;
			}
		}
		
		objectTiles.add(tile);
	}
	
	public final boolean removeObjectTile(final Point2D point) {
		for (final TilePoint tile: objectTiles) {
			if (tile.getPoint().equals(point)) {
				return objectTiles.remove(tile);
			}
		}
		
		return false;
	}
	
	public final boolean hasObjectTile(final Point2D point) {
		for (final TilePoint tile: objectTiles) {
			if (tile.getPoint().equals(point)) {
				return true;
			}
		}		
		
		return false;
	}
	
	public final List<TilePoint> getUnitTiles() {
		return unitTiles;
	}
	
	public final void setUnitTiles(final List<TilePoint> tiles) {
		if (tiles != null) {
			unitTiles = tiles;
		} else {
			unitTiles = new ArrayList<>();
		}
	}
	
	public final void setUnitTile(final TilePoint tile) {
		for (int i = 0; i < unitTiles.size(); i++) {
			if (unitTiles.get(i).getPoint().equals(tile.getPoint())) {
				unitTiles.set(i, tile);
				return;
			}
		}
		
		unitTiles.add(tile);
	}
	
	public final boolean removeUnitTile(final Point2D point) {
		for (final TilePoint tile: unitTiles) {
			if (tile.getPoint().equals(point)) {
				return unitTiles.remove(tile);
			}
		}
		
		return false;
	}
	
	protected final boolean hasUnitTile(final Point2D point) {
		for (final TilePoint tile: unitTiles) {
			if (tile.getPoint().equals(point)) {
				return true;
			}
		}		
		
		return false;
	}
	
	public final void build() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final Point2D point = new Point2D(i, j);
				if (!hasLandscapeTile(point)) {
					landscapeTiles.add(new TilePoint("empty", point));
				}
			}
		}
	}
}
