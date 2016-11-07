package ru.swg.island.common.animation;

import java.util.List;

import ru.swg.island.common.core.Const;
import ru.swg.wheelframework.animation.ClipAnimation;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.view.DisplayObject;
import ru.swg.wheelframework.view.Graphics;
import ru.swg.wheelframework.view.Image;

public class SimpleClipAnimation extends ClipAnimation {
	public SimpleClipAnimation(final DisplayObject target, final List<Image> images, final int speed, final ObjectListener<Boolean> successCallback, final ObjectListener<Object> errorCallback) {
		super(target, images, speed, successCallback, errorCallback);
	}
	
	@Override
	protected void paintImage(final Graphics graphics, final Image image) {
		graphics.drawImage(image, getTarget().getAbsoluteX() - (image.getWidth() - Const.TILE_WIDTH) / 2, getTarget().getAbsoluteY() - (image.getHeight() - Const.TILE_HEIGHT) / 2);		
	}
}
