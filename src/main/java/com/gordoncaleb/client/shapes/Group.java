package com.gordoncaleb.client.shapes;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;

public class Group extends UIObject2D {

	private List<UIObject2D> uiObjects = new ArrayList<UIObject2D>();

	public List<UIObject2D> getUiObjects() {
		return uiObjects;
	}

	public void add(UIObject2D d) {
		this.uiObjects.add(d);
	}

	public void remove(UIObject2D d) {
		this.uiObjects.remove(d);
	}

	@Override
	public void propagate(double elapsedTime) {
		super.propagate(elapsedTime);
		for (UIObject2D d : uiObjects) {
			d.propagate(elapsedTime);
		}
	}

	@Override
	public void draw(Context2d context) {
		for (UIObject2D d : uiObjects) {
			d.draw(context);
		}
	}

}
