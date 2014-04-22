package com.gordoncaleb.client.shapes.animation.transitions;

import com.gordoncaleb.client.shapes.Vector2D;

public class MoveTo extends PathElement {

	public MoveTo(double x, double y) {
		super(x, y, x, y);
		this.setAbsolute(true);
	}

	@Override
	public double getLength() {
		return 0;
	}

	@Override
	public Vector2D interpolatePoint(double fraction) {
		return getStartPoint();
	}

}
