package com.gordoncaleb.client.shapes.animation.transitions;

import java.util.LinkedList;

import com.gordoncaleb.client.shapes.animations.AnimationFinishedEvent;
import com.gordoncaleb.client.shapes.animations.EventHandler;

public class SequentialTransition extends Transition {

	private LinkedList<Transition> transitions = new LinkedList<Transition>();

	public SequentialTransition() {
		super();
	}

	public void play() {
		if (!transitions.isEmpty()) {
			if (!transitions.peek().isRunning()) {
				transitions.peek().setOnFinshed(new EventHandler<AnimationFinishedEvent>() {
					@Override
					public void handle(AnimationFinishedEvent event) {
						transitions.pop();
					}
				});
				transitions.peek().play();
			}
		}
	}

	@Override
	public void animate() {
		for (Transition t : transitions) {
			t.animate();
		}

	}
}
