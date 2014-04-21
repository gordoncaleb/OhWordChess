package com.gordoncaleb.client.shapes.animations.interpolator;

public abstract class Interpolator {

	public static final Interpolator LINEAR = new Interpolator() {
		@Override
		public double curve(double fraction) {
			return fraction;
		}
	};

	public static final Interpolator EASE_BOTH = new Interpolator() {
		@Override
		public double curve(double fraction) {
			return fraction;
		}
	};

	public static final Interpolator EASE_IN = new Interpolator() {
		@Override
		public double curve(double fraction) {
			return fraction;
		}
	};

	public static final Interpolator EASE_OUT = new Interpolator() {
		@Override
		public double curve(double fraction) {
			return fraction;
		}
	};

	public double interpolate(double startValue, double endValue, double fraction) {
		return startValue + (endValue - startValue) * curve(fraction);
	}

	public abstract double curve(double fraction);
}
