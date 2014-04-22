package com.gordoncaleb.client.shapes;

import com.google.gwt.canvas.dom.client.Context2d;
import com.gordoncaleb.client.shapes.animations.Animation;
import com.gordoncaleb.client.shapes.animations.Propagatable;

public abstract class UIObject2D implements Drawable, Propagatable {

	protected Animation animation;

	protected double width, height, rotation, scale;
	protected Vector2D position = new Vector2D(0, 0);

	protected Vector2D vel = new Vector2D(0, 0);
	protected Vector2D accel = new Vector2D(0, 0);

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position.setX(position.getX());
		this.position.setY(position.getY());
	}

	public void setPosition(double x, double y) {
		this.position.setX(x);
		this.position.setY(y);
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D vel) {
		this.vel.setX(vel.getX());
		this.vel.setY(vel.getY());
	}

	public void setVel(double x, double y) {
		this.vel.setX(x);
		this.vel.setY(y);
	}

	public Vector2D getAccel() {
		return accel;
	}

	public void setAccel(Vector2D accel) {
		this.accel.setX(accel.getX());
		this.accel.setY(accel.getY());
	}

	public void setAccel(double x, double y) {
		this.accel.setX(x);
		this.accel.setY(y);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
		this.animation.setNode(this);
	}

	public void propagate(double elapsedTime) {
		if (animation != null) {
			animation.propagate(elapsedTime);
			animation.animate();
		}
	}

	public abstract void draw(Context2d context);
}
