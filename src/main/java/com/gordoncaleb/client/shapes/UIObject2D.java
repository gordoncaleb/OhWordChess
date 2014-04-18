package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;

public abstract class UIObject2D implements Drawable {

	protected Vector2D position = new Vector2D(0, 0);
	protected Vector2D vel;
	protected Vector2D accel;

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public void setPosition(double x, double y) {
		this.position.setX(x);
		this.position.setY(y);
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D vel) {
		this.vel = vel;
	}

	public void setVel(double x, double y) {
		this.vel.setX(x);
		this.vel.setY(y);
	}

	public Vector2D getAccel() {
		return accel;
	}

	public void setAccel(Vector2D accel) {
		this.accel = accel;
	}

	public void setAccel(double x, double y) {
		this.accel.setX(x);
		this.accel.setY(y);
	}

	public abstract void draw(Context2d context);
}
