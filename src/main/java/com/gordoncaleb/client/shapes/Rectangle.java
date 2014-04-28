package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

public class Rectangle extends UIObject2D {

	private CssColor fillColor;
	private CssColor strokeColor;

	public Rectangle() {

	}

	public Rectangle(CssColor fillColor, CssColor strokeColor, int x, int y, int width, int height) {
		super();

		super.setPosition(x, y);
		super.setWidth(width);
		super.setHeight(height);

		this.fillColor = fillColor;
		this.strokeColor = strokeColor;

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

	@Override
	public void draw(Context2d context) {
		context.save();
		context.setFillStyle(fillColor);
		context.fillRect(position.getX(), position.getY(), width, height);
		context.setStrokeStyle(strokeColor);
		context.strokeRect(position.getX(), position.getY(), width, height);
		context.restore();
	}

}
