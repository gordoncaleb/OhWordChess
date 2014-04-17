package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.gordoncaleb.client.Drawable;
import com.gordoncaleb.client.util.CanvasUtils;

public class Rectangle implements Drawable {

	private CssColor fillColor = CanvasUtils.getRandomCssColor();
	private CssColor strokeColor = CanvasUtils.getRandomCssColor();
	private int x, y, width, height;

	public Rectangle(CssColor fillColor, CssColor strokeColor, int x, int y,
			int width, int height) {
		super();
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public CssColor getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(CssColor strokeColor) {
		this.strokeColor = strokeColor;
	}

	public CssColor getFillColor() {
		return fillColor;
	}

	public void setFillColor(CssColor fillColor) {
		this.fillColor = fillColor;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void draw(Context2d context) {
		context.save();
		context.setFillStyle(fillColor);
		context.fillRect(x, y, width, height);
		context.setStrokeStyle(strokeColor);
		context.strokeRect(x, y, width, height);
		context.restore();
	}
}
