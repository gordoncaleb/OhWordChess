package com.gordoncaleb.client.shapes;

public class Vector2D {

	private double x, y;

	public Vector2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(Vector2D vector) {
		setX(vector.getX());
		setY(vector.getY());
	}

	public Vector2D subtract(Vector2D vector) {
		return new Vector2D(x - vector.getX(), y - vector.getY());
	}

	public Vector2D add(Vector2D vector) {
		return new Vector2D(x + vector.getX(), y + vector.getY());
	}

	public Vector2D scale(double scale) {
		return new Vector2D(x * scale, y * scale);
	}

	public double distance(Vector2D vector) {
		return Math.sqrt(Math.pow(x - vector.getX(), 2) + Math.pow(y - vector.getY(), 2));
	}
}
