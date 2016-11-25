/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.core.object;

import java.util.ArrayList;
import java.util.List;

import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Class for tiles
 */
public class Tile {
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
	private List<String> animations = new ArrayList<>();
	private Point2D point;
	
	public Tile() { }
	
	public Tile(final Point2D point) {
		this.point = point;
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
	 * Set id
	 * 
	 * @param id
	 */
	public final void setId(final String id) {
		this.id = id;
	}
	
	/**
	 * Get image path
	 * 
	 * @return
	 */
	public final String getImage() {
		return image;
	}
	
	/**
	 * Set image path
	 * 
	 * @param image
	 */
	public final void setImage(final String image) {
		this.image = image;
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
	 * Set weight
	 * 
	 * @param weight
	 */
	public final void setWeight(final int weight) {
		this.weight = weight;
	}
	
	/**
	 * Get weight
	 * 
	 * @return
	 */
	public final int getWeight() {
		return weight;
	}
	
	/**
	 * Set z
	 * 
	 * @param z
	 */
	public final void setZ(final int z) {
		this.z = z;
	}
	
	/**
	 * Get z
	 * 
	 * @return
	 */
	public final int getZ() {
		return z;
	}
	
	public final List<String> getAnimations() {
		return animations;
	}
	
	public final void setPoint(final Point2D point) {
		this.point = point;
	}
	
	public final Point2D getPoint() {
		return point;
	}
}
