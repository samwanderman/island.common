/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.core.object;

import java.util.List;

/**
 * Level
 */
public final class Level {
	// id
	private String id;
	// width in cells
	private int width;
	// height in cells
	private int height;
	// landscape tiles list
	private List<TilePoint> landscapeTiles;
	// object tiles
	private List<TilePoint> objectTiles;
	
	/**
	 * Set id
	 * 
	 * @param id
	 */
	public final void setId(final String id) {
		this.id = id;
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
		landscapeTiles = tiles;
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
		objectTiles = tiles;
	}
}
