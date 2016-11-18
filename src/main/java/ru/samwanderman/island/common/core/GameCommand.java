package ru.samwanderman.island.common.core;

/**
 * Game command settings
 */
public final class GameCommand {
	private final int id;
	private final String name;
	private final GameResources resources = new GameResources();
	
	public GameCommand(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public final int getId() {
		return id;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void addResource(final String name) {
		resources.add(name);
	}
	
	public final void addResource(final String name, final int amount) {
		resources.append(name, amount);
	}
	
	public final int getResource(final String name) {
		return resources.get(name);
	}
}
