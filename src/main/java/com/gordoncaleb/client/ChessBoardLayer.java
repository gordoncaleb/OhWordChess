package com.gordoncaleb.client;

import com.google.gwt.canvas.dom.client.CssColor;

public class ChessBoardLayer extends Layer {

	public ChessBoardLayer(int width, int height, CssColor lightColor,
			CssColor darkColor) {

		int w = width / 8;
		int h = height / 8;

		boolean lightSquare;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				lightSquare = ((r % 2) == (c % 2));
				this.addDrawable(new Rectangle(lightSquare ? lightColor
						: darkColor, r * w, c * h, w, h));
			}
		}

	}
}
