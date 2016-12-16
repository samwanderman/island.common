package ru.samwanderman.island.common.ai;

import java.util.concurrent.ConcurrentLinkedQueue;

import ru.samwanderman.island.common.event.RemoveTileEvent;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.wheel.ai.IAI;
import ru.samwanderman.wheel.event.Events;
import ru.samwanderman.wheel.event.event.Event;
import ru.samwanderman.wheel.event.listener.Listener;
import ru.samwanderman.wheel.view.DisplayObject;

public final class LevelAI implements IAI {
	private final GuiLevel level;
	private final ConcurrentLinkedQueue<Event> messageQueue = new ConcurrentLinkedQueue<>();
	
	public LevelAI(final GuiLevel level) {
		this.level = level;
		Events.addListener(RemoveTileEvent.class, new Listener<RemoveTileEvent>() {
			@Override
			public final void notify(final RemoveTileEvent event) {
				messageQueue.add(event);
			}

			@Override
			public final boolean checkTarget(final DisplayObject target) {
				return (DisplayObject) level == target;
			}			
		});
	}

	@Override
	public final void sync() {
		while (!messageQueue.isEmpty()) {
			final Event event = messageQueue.poll();
			if (event == null) {
				return;
			}
			
			if (event instanceof RemoveTileEvent) {
				level.removeChild(((RemoveTileEvent) event).getTile());
			}
		}
	}
}
