package com.gordoncaleb.client.shapes.animation.Transitions;

import com.gordoncaleb.client.shapes.Vector2D;

public abstract class PathElement {

	private boolean absolute = false;
	private Vector2D startPoint;
	private Vector2D endPoint;

	public PathElement(double x1, double y1, double x2, double y2) {
		super();
		this.startPoint = new Vector2D(x1, y1);
		this.endPoint = new Vector2D(x2, y2);
	}

	public Vector2D getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector2D startPoint) {
		this.startPoint.set(startPoint);
	}

	public void setStartPoint(double x, double y) {
		this.startPoint.set(x, y);
	}

	public Vector2D getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector2D endPoint) {
		this.endPoint.set(endPoint);
	}

	public void setEndPoint(double x, double y) {
		this.endPoint.set(x, y);
	}

	public Vector2D getRelativeVector() {
		return endPoint.subtract(startPoint);
	}

	public boolean isAbsolute() {
		return absolute;
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}

	public abstract double getLength();

	public abstract Vector2D interpolatePoint(double fraction);

}
