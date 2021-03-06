package com.gordoncaleb.client.shapes.animation.transitions;

public class FadeTransition extends Transition {

	private double start;
	private double end;

	@Override
	public void animate() {
		node.setOpacity(interpolator.interpolate(start, end, getProgress()));
	}

}
