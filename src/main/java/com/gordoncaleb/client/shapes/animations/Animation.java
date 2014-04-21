package com.gordoncaleb.client.shapes.animations;

import com.gordoncaleb.client.shapes.UIObject2D;

public abstract class Animation {

	public static enum Status {
		PAUSED, RUNNING, STOPPED
	}

	private static final double DEFAULT_RATE = 1.0;
	private static final Status DEFAULT_STATUS = Status.STOPPED;
	private static final boolean DEFAULT_AUTO_REVERSE = false;
	private static final double INITIAL_PLAY_HEAD = Double.NaN;
	private static final double INFINITE_CYCLES = Double.POSITIVE_INFINITY;

	protected Status status = DEFAULT_STATUS;
	protected double rate = DEFAULT_RATE;
	protected boolean autoReverse = DEFAULT_AUTO_REVERSE;
	protected double playHead = INITIAL_PLAY_HEAD;
	protected double cycles = INFINITE_CYCLES;

	protected double cycleCount = 0;
	protected double duration;

	protected EventHandler<AnimationFinishedEvent> onFinshed;

	public Animation() {
	}

	public void play() {
		status = Status.RUNNING;
	}

	public void replay() {
		stop();
		play();
	}

	public void playFrom(double playHead) {
		this.playHead = playHead;
		play();
	}

	public void stop() {
		status = Status.STOPPED;
		playHead = INITIAL_PLAY_HEAD;
		cycleCount = 0;
	}

	public void pause() {
		status = Status.PAUSED;
	}

	public void propagate(double elapsedTime) {
		if (status == Status.RUNNING) {
			if (playHead != INITIAL_PLAY_HEAD) {
				playHead = playHead + rate * elapsedTime;

				if (playHead >= duration || playHead <= 0.0) {

					if (cycles == INFINITE_CYCLES || cycleCount < cycles) {

						if (autoReverse) {
							rate = -rate;
						} else {
							playHead = (rate > 0) ? 0.0 : 1.0;
						}

						cycleCount++;
					} else {
						stop();
					}
				}

				playHead = Math.max(0.0, Math.min(duration, playHead));
			} else {
				playHead = 0.0;
			}
		}

	}

	public double getProgress() {
		return playHead / duration;
	}

	public Status getStatus() {
		return status;
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
		return onFinshed;
	}

	public void setOnFinshed(EventHandler<AnimationFinishedEvent> onFinshed) {
		this.onFinshed = onFinshed;
	}

	public abstract void animate(UIObject2D node);

}
