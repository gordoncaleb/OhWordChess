package com.gordoncaleb.client.shapes.animation.transitions;

public class PathTransition extends Transition {

	private Path path;

	public PathTransition(Path path) {
		super();
		this.path = path;
	}

	@Override
	public void animate() {
		node.setPosition(path.interpolatePoint(interpolator.curve(getProgress())));
	}

}
