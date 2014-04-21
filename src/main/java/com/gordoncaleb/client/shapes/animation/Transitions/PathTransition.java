package com.gordoncaleb.client.shapes.animation.Transitions;

import com.gordoncaleb.client.shapes.UIObject2D;

public class PathTransition extends Transition {

	private Path path;

	public PathTransition(Path path) {
		super();
		this.path = path;
	}

	@Override
	public void animate(UIObject2D node) {
		node.setPosition(path.interpolatePoint(this.getProgress()));
	}

}
