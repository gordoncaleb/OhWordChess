package com.gordoncaleb.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;

public class Layer implements Drawable {

	private List<Drawable> drawables = new ArrayList<Drawable>();

	public List<Drawable> getDrawables() {
		return drawables;
	}

	public void addDrawable(Drawable d) {
		this.drawables.add(d);
	}

	@Override
	public void draw(Context2d context) {
		for (Drawable d : drawables) {
			d.draw(context);
		}
	}

}
