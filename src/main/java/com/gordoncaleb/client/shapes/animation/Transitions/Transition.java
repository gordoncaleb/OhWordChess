package com.gordoncaleb.client.shapes.animation.transitions;

import com.gordoncaleb.client.shapes.animations.Animation;
import com.gordoncaleb.client.shapes.animations.interpolator.Interpolator;

public abstract class Transition extends Animation {

	protected Interpolator interpolator = Interpolator.LINEAR;

	public Interpolator getInterpolator() {
		return interpolator;
	}

	public void setInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
	}

}
