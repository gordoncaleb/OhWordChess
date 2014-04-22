package com.gordoncaleb.client.shapes.animation.transitions;

public class PathTransition extends Transition {

	private Path path;

	public PathTransition(Path path) {
		super();
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public void animateImpl() {
		node.setPosition(path.interpolatePoint(interpolator.curve(getProgress())));
	}

	public static PathTransition linear(double... v) {

		Path p = new Path();
		for (int i = 0; (i + 1) < v.length; i += 2) {
			if (i == 0) {
				p.add(new MoveTo(v[i], v[i + 1]));
			} else {
				p.add(new LineTo(v[i], v[i + 1]));
			}
		}

		return new PathTransition(p);
	}

}
