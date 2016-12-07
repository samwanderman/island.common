package ru.samwanderman.island.common.ai;

public final class AI {
	public static final boolean isCommandsHostile(final int command1Id, final int command2Id) {
		if ((command1Id == 0) || (command2Id == 0)) {
			return false;
		}
		
		return true;
	}
}
