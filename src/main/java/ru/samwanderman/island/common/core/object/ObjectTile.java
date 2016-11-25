/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.core.object;

/**
 * Object tile
 */
public class ObjectTile extends Tile {
	private int gameCommand;
	private int health;
	private int maxHealth;
	
	public final void setGameCommand(final int gameCommand) {
		this.gameCommand = gameCommand;
	}
	
	public final int getGameCommand() {
		return gameCommand;
	}
	
	public final void setHealth(final int health) {
		this.health = health > maxHealth ? maxHealth : health;
	}
	
	public final int getHealth() {
		return health;
	}
	
	public final void setMaxHealth(final int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public final int getMaxHealth() {
		return maxHealth;
	}
}
