package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

public class Sprite extends UIObject2D {

	private ImageElement sprite;

	public Sprite(final ImageElement image, double x, double y, double width, double height) {

		this.setPosition(x, y);
		this.width = width;
		this.height = height;

		sprite = image;
	}

	public double getScaleFactor() {

		double hratio = height / sprite.getHeight();
		double wratio = width / sprite.getWidth();

		double scale = 1;
		if (hratio < 1 || wratio < 1) {
			if (hratio < 1 && wratio < 1) {
				scale = Math.min(hratio, wratio);
			} else {
				scale = (hratio < 1) ? hratio : wratio;
			}
		}

		return scale;
	}

	@Override
	public void draw(Context2d context) {
		if (sprite != null) {
			context.save();

			double scale = getScaleFactor();
			double scaledWidth = sprite.getWidth() * scale;
			double scaledHeight = sprite.getHeight() * scale;
			double x = position.getX() + width / 2 - scaledWidth / 2;
			context.drawImage(sprite, x, position.getY(), scaledWidth, scaledHeight);
			context.restore();
		}
	}
}
