/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.core.object;

/**
 * Unit tile
 */
public final class UnitTile extends ObjectTile {
	private int attack;
	
	public final void setAttack(final int attack) {
		this.attack = attack;
	}
	
	public final int getAttack() {
		return attack;
	}
}
