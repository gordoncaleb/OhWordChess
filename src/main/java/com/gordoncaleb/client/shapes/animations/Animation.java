package com.gordoncaleb.client.shapes.animations;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gordoncaleb.client.shapes.UIObject2D;

public abstract class Animation {

	public static enum Status {
		PAUSED, RUNNING, STOPPED
	}

	private static final double DEFAULT_RATE = 1.0;
	private static final Status DEFAULT_STATUS = Status.STOPPED;
	private static final boolean DEFAULT_AUTO_REVERSE = false;
	private static final Double INITIAL_PLAY_HEAD = null;
	private static final double INFINITE_CYCLES = Double.POSITIVE_INFINITY;

	protected Status status = DEFAULT_STATUS;
	protected double rate = DEFAULT_RATE;
	protected boolean autoReverse = DEFAULT_AUTO_REVERSE;
	protected Double playHead = INITIAL_PLAY_HEAD;
	protected double cycles = 1;

	protected UIObject2D node;

	protected double cycleCount = 0;
	protected double duration = 1000;

	protected EventHandler<AnimationFinishedEvent> onFinished;

	public Animation() {
	}

	public Animation play() {
		status = Status.RUNNING;
		playHead = INITIAL_PLAY_HEAD;
		cycleCount = 0;
		return this;
	}

	public Animation replay() {
		stop();
		play();
		return this;
	}

	public Animation playFrom(double playHead) {
		this.playHead = playHead;
		play();
		return this;
	}

	public Animation stop() {
		status = Status.STOPPED;
		Logger.getLogger("").log(Level.INFO, "Animation stopped");
		return this;
	}

	public Animation pause() {
		status = Status.PAUSED;
		return this;
	}

	public void propagate(double elapsedTime) {
		if (status == Status.RUNNING) {
			if (playHead != INITIAL_PLAY_HEAD) {
				playHead = Math.max(0.0, Math.min(duration, playHead + rate * elapsedTime));

				Logger.getLogger("").log(Level.INFO, "Elapsed Time" + elapsedTime);

				if (playHead >= duration || playHead <= 0.0) {

					if (cycles == INFINITE_CYCLES || cycleCount < (cycles - 1)) {

						if (autoReverse) {
							rate = -rate;
						} else {
							playHead = (rate > 0) ? 0.0 : 1.0;
						}

						cycleCount++;
					} else {
						stop();
						if (onFinished != null) {
							onFinished.handle(new AnimationFinishedEvent(this));
						}

					}
				}

				Logger.getLogger("").log(Level.INFO, "PlayHead set to " + playHead);
			} else {
				playHead = 0.0;
				Logger.getLogger("").log(Level.INFO, "PlayHead set to 0");
			}

		}

	}

	public Double getProgress() {
		if (playHead != null) {
			return playHead / duration;
		} else {
			return null;
		}
	}

	public Status getStatus() {
		return status;
	}

	public boolean isRunning() {
		return (status == Status.RUNNING);
	}

	public boolean isPaused() {
		return (status == Status.PAUSED);
	}

	public boolean isStopped() {
		return (status == Status.STOPPED);
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isAutoReverse() {
		return autoReverse;
	}

	public void setAutoReverse(boolean autoReverse) {
		this.autoReverse = autoReverse;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getPlayHead() {
		return playHead;
	}

	public void setPlayHead(double playHead) {
		this.playHead = playHead;
	}

	public EventHandler<AnimationFinishedEvent> getOnFinshed() {
		return onFinished;
	}

	public void setOnFinshed(EventHandler<AnimationFinishedEvent> onFinshed) {
		this.onFinished = onFinshed;
	}

	public UIObject2D getNode() {
		return node;
	}

	public void setNode(UIObject2D node) {
		this.node = node;
	}

	public void animate() {
		if (isRunning()) {
			animateImpl();
		}
	}

	public abstract void animateImpl();

}
