package com.gordoncaleb.client;

import com.google.gwt.canvas.dom.client.CssColor;

public class CanvasUtils {

	public static CssColor getRandomCssColor() {
		int r = (int) (Math.random() * 256);
		int g = (int) (Math.random() * 256);
		int b = (int) (Math.random() * 256);

		return CssColor.make(r, g, b);
	}
}
