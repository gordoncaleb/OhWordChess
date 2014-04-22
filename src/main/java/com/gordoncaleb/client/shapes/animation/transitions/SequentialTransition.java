package com.gordoncaleb.client.shapes.animation.transitions;

import java.util.LinkedList;

import com.gordoncaleb.client.shapes.animations.Animation;

public class SequentialTransition extends Transition {

	private LinkedList<Transition> transitions = new LinkedList<Transition>();
	private Transition currentTransition;

	public SequentialTransition() {
		super();
	}

	public Animation play() {
		for (Transition t : transitions) {
			t.play();
		}
		return super.play();
	}

	public Animation pause() {
		for (Transition t : transitions) {
			t.pause();
		}
		return super.pause();
	}

	public Animation stop() {
		for (Transition t : transitions) {
			t.stop();
		}
		return super.stop();
	}

	public void add(Transition t) {
		transitions.add(t);
		duration += t.getDuration();
		t.setNode(getNode());
	}

	public void insert(int i, Transition t) {
		transitions.add(i, t);
		duration += t.getDuration();
		t.setNode(getNode());
	}

	public void remove(Transition t) {
		if (transitions.remove(t)) {
			duration -= t.getDuration();
		}
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
	}

	public void propagate(double elapsedTime) {
		super.propagate(elapsedTime);

		double tempPlayHead = this.playHead;

		for (Transition t : transitions) {
			if ((tempPlayHead - t.getDuration()) < 0) {
				currentTransition = t;
				currentTransition.setPlayHead(tempPlayHead);
				break;
			} else {
				tempPlayHead -= t.getDuration();
			}
		}
	}

	@Override
	public void animateImpl() {
		currentTransition.animate();
	}
}
