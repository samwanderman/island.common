/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view.tile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.core.object.ObjectTile;
import ru.samwanderman.island.common.event.RemoveTileEvent;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.wheel.ai.IAI;
import ru.samwanderman.wheel.animation.IAnimatedObject;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.event.Events;
import ru.samwanderman.wheel.event.event.ISyncEvent;
import ru.samwanderman.wheel.event.listener.ObjectListener;
import ru.samwanderman.wheel.io.Resources;
import ru.samwanderman.wheel.log.Log;
import ru.samwanderman.wheel.view.Color;
import ru.samwanderman.wheel.view.DisplayObject;
import ru.samwanderman.wheel.view.Graphics;
import ru.samwanderman.wheel.view.Image;

/**
 * Gui object tile
 */
public class GuiObjectTile<T1 extends IAI, T2 extends ObjectTile> extends GuiTile<GuiLevel, T2> implements ISyncEvent, IAnimatedObject {
	private final GuiObjectTile<T1, T2> self = this;
	final private HashMap<String, IAnimation> animations = new HashMap<>();
	private T1 ai;
	private IAnimation currentAnimation = null;
	
	public GuiObjectTile(final GuiLevel guiLevel, final T2 tile) 
			throws IOException {
		super(guiLevel, tile);
		setupAnimations(tile.getAnimations());
	}
	
	@Override
	public void paint(final Graphics graphics) {
		if ((currentAnimation != null) && currentAnimation.isRunning()) {
			final Image image = currentAnimation.getImage(); 
			graphics.drawImage(image, getAbsoluteX() + (Const.TILE_WIDTH - image.getWidth()) / 2, getAbsoluteY() + (Const.TILE_HEIGHT - image.getHeight()) / 2);
			paintHealth(graphics);
			paintSelection(graphics);
			return;
		}

		super.paint(graphics);
		paintHealth(graphics);
	}

	@Override
	public void sync() {
		if (currentAnimation != null) {
			currentAnimation.sync();
		}
		
		if (ai != null) {
			ai.sync();
		}
	}
	
	@Override
	protected final void paintSelection(final Graphics graphics) {
		if (isSelected()) {
			Color color = Color.GRAY;
			switch (getTile().getGameCommand()) {
			case 1:
				color = Color.GREEN;
				break;
			case 2:
				color = Color.RED;
				break;
			}
			
			graphics.setColor(color);
			graphics.drawRect(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight());
		}	
	}
	
	private final void paintHealth(final Graphics graphics) {
		// avoid neutral objects
		if (getGameCommand() == 0) {
			return;
		}
		
		final float perc = (float) ((ObjectTile) getTile()).getHealth() / ((ObjectTile) getTile()).getMaxHealth();
		if (perc <= 0.33) {
			graphics.setColor(Color.RED);	
		} else if (perc <= 0.66) {
			graphics.setColor(Color.YELLOW);
		} else {
			graphics.setColor(Color.GREEN);	
		}
		graphics.drawRect(getAbsoluteX(), getAbsoluteY() - 10, (int) (Const.TILE_WIDTH * perc), 2);		
	}
	
	protected void setupAnimations(final List<String> names) {
		for (final String name: names) {
			try {
				addAnimation(name, Resources.loadAnimation("/units/" + getTile().getId() + "/" + name + ".json"));
			} catch (final IOException e) { 
				Log.error(e.getMessage());
			}
		}
	}

	@Override
	public final void playAnimation(final String name) {
		stopCurrentAnimation();
		
		currentAnimation = animations.get(name);
		if (currentAnimation != null) {
			currentAnimation.play();
		}
	}
	
	@Override
	public final void playAnimation(final String name, final ObjectListener<Object> callback) {
		stopCurrentAnimation();
		
		currentAnimation = animations.get(name);
		if (currentAnimation != null) {
			currentAnimation.play(callback);
		}
	}

	@Override
	public final void stopCurrentAnimation() {
		if (currentAnimation != null) {
			currentAnimation.stop();
		}		
	}
	
	protected final IAnimation getAnimation(final String name) {
		return animations.get(name);
	}

	@Override
	public final IAnimation getCurrentAnimation() {
		return currentAnimation;
	}

	@Override
	public final void addAnimation(final String name, final IAnimation animation) {
		animations.put(name, animation);
	}
	
	public final int getGameCommand() {
		return ((ObjectTile) getTile()).getGameCommand();
	}
	
	protected final void setAI (final T1 ai) {
		this.ai = ai;
	}
	
	public final void addHealth(final int value) {
		final ObjectTile tile = (ObjectTile) getTile(); 
		int resultHealth = tile.getHealth() + value;
		if (resultHealth > tile.getMaxHealth()) {
			resultHealth = tile.getMaxHealth();
		} else if (resultHealth < 0) {
			resultHealth = 0;
		}
		tile.setHealth(resultHealth);
		
		if (resultHealth == 0) {
			playAnimation("dead", new ObjectListener<Object>() {
				@Override
				public final void on(final Object object) {
					Events.dispatch(new RemoveTileEvent((DisplayObject) getParent(), self));
				}
			});
		}
	}
	
	public final int getHealth() {
		return ((ObjectTile) getTile()).getHealth();
	}
}
