package com.gordoncaleb.client.shapes;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class Sprite extends UIObject2D {

	private ImageElement sprite;

	public Sprite(final String imagePath, double x, double y, double width, double height) {

		this.setPosition(x, y);
		this.width = width;
		this.height = height;

		Image img = new Image(imagePath);
		img.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				sprite = (ImageElement) e.getRelativeElement().cast();
				Logger.getLogger("").log(Level.INFO, "Loaded " + imagePath);
			}
		});

		img.setVisible(false);
		RootPanel.get().add(img);
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
