package com.gordoncaleb.client.shapes.animations;

public class AnimationFinishedEvent implements Event {

	private Animation source;

	public AnimationFinishedEvent(Animation source) {
		this.source = source;
	}

}
