package com.gordoncaleb.client.shapes.animations;

public class AnimationEvent implements Event {

	private Animation source;

	public AnimationEvent(Animation source) {
		this.source = source;
	}

}
