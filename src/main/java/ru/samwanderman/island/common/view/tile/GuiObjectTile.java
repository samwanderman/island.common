/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view.tile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.core.object.ObjectTile;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.wheel.ai.IAI;
import ru.samwanderman.wheel.animation.IAnimatedObject;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.event.event.ISyncEvent;
import ru.samwanderman.wheel.io.Resources;
import ru.samwanderman.wheel.log.Log;
import ru.samwanderman.wheel.view.Color;
import ru.samwanderman.wheel.view.Graphics;
import ru.samwanderman.wheel.view.Image;

/**
 * Gui object tile
 */
public class GuiObjectTile extends GuiTile implements ISyncEvent, IAnimatedObject {
	final private HashMap<String, IAnimation> animations = new HashMap<>();
	private IAI ai;
	private IAnimation currentAnimation = null;
	
	public GuiObjectTile(final GuiLevel guiLevel, final ObjectTile tile) 
			throws IOException {
		super(guiLevel, tile);
		setupAnimations(tile.getAnimations());
	}
	
	@Override
	public void paint(final Graphics graphics) {
		if ((currentAnimation != null) && currentAnimation.isRunning()) {
			final Image image = currentAnimation.getImage(); 
			graphics.drawImage(image, getAbsoluteX() + (Const.TILE_WIDTH - image.getWidth()) / 2, getAbsoluteY() + (Const.TILE_HEIGHT - image.getHeight()) / 2);
			paintSelection(graphics);
			graphics.setColor(Color.GREEN);
			graphics.drawRect(getAbsoluteX(), getAbsoluteY() - 10, (int) (Const.TILE_WIDTH * ((float) ((ObjectTile) getTile()).getHealth() / ((ObjectTile) getTile()).getMaxHealth())), 2);
			return;
		}

		super.paint(graphics);
		
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
			final ObjectTile tile = (ObjectTile) getTile();
			Color color = Color.GRAY;
			switch (tile.getGameCommand()) {
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
	
	protected final void setAI (final IAI ai) {
		this.ai = ai;
	}
}
