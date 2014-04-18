package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

public class Sprite extends UIObject2D {

	private ImageElement spriteSheet;
	private Image img;
	private boolean imageLoaded;

	public Sprite(String imagePath) {

		img = new Image(imagePath);
		img.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent e) {
				spriteSheet = (ImageElement) img.getElement().cast();
				imageLoaded = true;
			}
		});
	}

	@Override
	public void draw(Context2d context) {
		if (imageLoaded) {
			context.save();
			context.drawImage(spriteSheet, position.getX(), position.getY());
			context.restore();
		}
	}

}
