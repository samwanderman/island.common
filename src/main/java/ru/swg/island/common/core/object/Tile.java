/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.core.object;

/**
 * Class for tiles
 */
public final class Tile {
	// Tile id
	private String id;
	// Short tile description
	private String name;
	// Path to image
	private String image;
	// Tile weight
	private int weight;
	// z-index of tile
	private int z;
	
	/**
	 * Get image path
	 * 
	 * @return
	 */
	public final String getImage() {
		return image;
	}
	
	/**
	 * Get tile name
	 * 
	 * @return
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Get tile weight
	 * 
	 * @return
	 */
	public final int getWeight() {
		return weight;
	}
	
	/**
	 * Get z
	 * 
	 * @return
	 */
	public final int getZ() {
		return z;
	}
}
