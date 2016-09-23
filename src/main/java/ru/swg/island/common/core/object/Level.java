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
	private String name;
	private String description;
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
