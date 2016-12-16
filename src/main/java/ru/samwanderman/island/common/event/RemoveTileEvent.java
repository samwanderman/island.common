package ru.samwanderman.island.common.event;

import ru.samwanderman.island.common.view.tile.GuiObjectTile;
import ru.samwanderman.wheel.event.event.Event;
import ru.samwanderman.wheel.view.DisplayObject;

public final class RemoveTileEvent implements Event {
	private final DisplayObject target;
	private final GuiObjectTile<?, ?> tile;
	
	public RemoveTileEvent(final DisplayObject target, final GuiObjectTile<?, ?> tile) {
		this.target = target;
		this.tile = tile;
	}
	
	public final GuiObjectTile<?, ?> getTile() {
		return tile;
	}
	
	public final DisplayObject getTarget() {
		return target;
	}
}
