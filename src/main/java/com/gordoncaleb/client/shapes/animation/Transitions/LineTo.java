package com.gordoncaleb.client.shapes.animation.Transitions;

import com.gordoncaleb.client.shapes.Vector2D;

public class LineTo extends PathElement {

	public LineTo(double x, double y) {
		super(x, y, x, y);
		this.setAbsolute(false);
	}

	@Override
	public double getLength() {
		return getStartPoint().distance(getEndPoint());
	}

	@Override
	public Vector2D interpolatePoint(double fraction) {
		return getStartPoint().add(getRelativeVector().scale(fraction));
	}

}
