/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.core.object.ObjectTile;
import ru.samwanderman.wheel.animation.IAnimatedObject;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.event.event.ISyncEvent;
import ru.samwanderman.wheel.io.Resources;
import ru.samwanderman.wheel.log.Log;
import ru.samwanderman.wheel.view.Graphics;
import ru.samwanderman.wheel.view.Image;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Gui object tile
 */
public class GuiObjectTile extends GuiTile implements ISyncEvent, IAnimatedObject {
	final private HashMap<String, IAnimation> animations = new HashMap<>();
	private IAnimation currentAnimation = null;
	
	public GuiObjectTile(final ObjectTile tile) 
			throws IOException {
		super(tile);
		setupAnimations(tile.getAnimations());
	}

	/**
	 * Constructor
	 * 
	 * @param tile
	 * @param point
	 */
	public GuiObjectTile(final ObjectTile tile, final Point2D point) 
			throws IOException {
		super(tile, point);
		setupAnimations(tile.getAnimations());
	}
	
	@Override
	public void paint(final Graphics graphics) {
		if ((currentAnimation != null) && currentAnimation.isRunning()) {
			final Image image = currentAnimation.getImage(); 
			graphics.drawImage(image, getAbsoluteX() + (Const.TILE_WIDTH - image.getWidth()) / 2, getAbsoluteY() + (Const.TILE_HEIGHT - image.getHeight()) / 2);
			paintSelection(graphics);
			return;
		}
		
		super.paint(graphics);
	}

	@Override
	public void sync() {
		if (currentAnimation != null) {
			currentAnimation.sync();
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
			Log.info("play ObjectTile");
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
}
