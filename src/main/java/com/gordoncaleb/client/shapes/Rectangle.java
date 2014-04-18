package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class Rectangle extends UIObject2D {

	private CssColor fillColor;
	private CssColor strokeColor;
	private int width, height;

	public Rectangle(CssColor fillColor, CssColor strokeColor, int x, int y, int width, int height) {

		super();
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.position = new Vector2D(x, y);
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
		context.fillRect(position.getX(), position.getY(), width, height);
		context.setStrokeStyle(strokeColor);
		context.strokeRect(position.getX(), position.getY(), width, height);
		context.restore();
	}
}
