package ru.samwanderman.island.common.core;

import java.util.HashMap;

/**
 * Game resources
 */
public class GameResources {
	private HashMap<String, Integer> resources = new HashMap<>();
	
	public GameResources() { }
	
	public GameResources(final HashMap<String, Integer> resources) {
		this.resources = resources;
	}
	
	public final void add(final String name) {
		resources.put(name, 0);
	}
	
	public final boolean append(final String name, final int amount) {
		Integer currentAmount = resources.get(name);
		
		if (currentAmount == null) {
			currentAmount = 0;
		}
		
		if (currentAmount + amount < 0) {
			return false;
		}
		
		resources.put(name, currentAmount + amount);
		
		return true;
	}
	
	public final int get(final String name) {
		return resources.get(name);
	}
}
