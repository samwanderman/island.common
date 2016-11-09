package ru.samwanderman.island.common.animation;

import java.util.List;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.rtwf.animation.ClipAnimation;
import ru.samwanderman.rtwf.event.listener.ObjectListener;
import ru.samwanderman.rtwf.view.DisplayObject;
import ru.samwanderman.rtwf.view.Graphics;
import ru.samwanderman.rtwf.view.Image;

public class SimpleClipAnimation extends ClipAnimation {
	public SimpleClipAnimation(final DisplayObject target, final List<Image> images, final int speed) {
		super(target, images, speed, null, null);
	}

	public SimpleClipAnimation(final DisplayObject target, final List<Image> images, final int speed, final ObjectListener<Boolean> successCallback, final ObjectListener<Object> errorCallback) {
		super(target, images, speed, successCallback, errorCallback);
	}
	
	@Override
	protected void paintImage(final Graphics graphics, final Image image) {
		graphics.drawImage(image, getTarget().getAbsoluteX() - (image.getWidth() - Const.TILE_WIDTH) / 2, getTarget().getAbsoluteY() - (image.getHeight() - Const.TILE_HEIGHT) / 2);		
	}
}
