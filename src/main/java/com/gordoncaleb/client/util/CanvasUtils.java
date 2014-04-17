package com.gordoncaleb.client.util;

import com.google.gwt.canvas.dom.client.CssColor;

public class CanvasUtils {

	public static final CssColor BLACK = CssColor.make(0, 0, 0);
	public static final CssColor RED = CssColor.make(255, 0, 0);
	public static final CssColor GREEN = CssColor.make(0, 255, 0);
	public static final CssColor YELLOW = CssColor.make(255, 255, 0);
	public static final CssColor BLUE = CssColor.make(0, 0, 255);
	public static final CssColor WHITE = CssColor.make(255, 255, 255);
	public static final CssColor GRAY = CssColor.make(0xC0, 0xC0, 0xC0);

	public static final CssColor LIGHTBLUE = CssColor.make(0, 0x99, 0xFF);
	public static final CssColor DARKBLUE = CssColor.make(0, 0, 0x99);

	public static final CssColor DARKBROWN = CssColor.make(181, 136, 99);
	public static final CssColor LIGHTBROWN = CssColor.make(240, 217, 181);

	public static CssColor getRandomCssColor() {
		int r = (int) (Math.random() * 256);
		int g = (int) (Math.random() * 256);
		int b = (int) (Math.random() * 256);

		return CssColor.make(r, g, b);
	}
}
