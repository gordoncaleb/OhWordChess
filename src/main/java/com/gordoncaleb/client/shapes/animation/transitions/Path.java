package com.gordoncaleb.client.shapes.animation.transitions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gordoncaleb.client.shapes.Vector2D;

public class Path {

	private List<PathElement> segments = new ArrayList<PathElement>();
	private double length;

	public Path() {
		super();
	}

	public void add(PathElement segment) {

		if (!segment.isAbsolute() && !segments.isEmpty()) {
			PathElement lastSegment = getLastSegment();
			segment.setStartPoint(lastSegment.getEndPoint());
			segment.setEndPoint(lastSegment.getEndPoint().add(segment.getRelativeVector()));
		}

		segments.add(segment);
		length += segment.getLength();
	}

	public boolean insert(int i, PathElement segment) {

		if (i < segments.size() && i >= 0) {

			segments.add(i, segment);
			recompilePath();

			return true;
		}

		return false;
	}

	public boolean remove(PathElement segment) {
		if (segments.remove(segment)) {
			recompilePath();
			return true;
		}
		return false;
	}

	public boolean remove(int i) {

		if (i < segments.size() && i >= 0) {
			segments.remove(i);
			recompilePath();
			return true;
		}

		return false;
	}

	private void recompilePath() {
		List<PathElement> buffer = new ArrayList<PathElement>();
		buffer.addAll(segments);

		segments.clear();
		length = 0;

		for (PathElement seg : buffer) {
			this.add(seg);
		}
	}

	public PathElement getLastSegment() {
		return segments.get(segments.size() - 1);
	}

	public double getLength() {
		return length;
	}

	public Vector2D interpolatePoint(double fraction) {
		double pointLength = getLength() * fraction;
		
		Logger.getLogger("").log(Level.INFO, "Path Length= " + getLength());
		Logger.getLogger("").log(Level.INFO, "Point Length= " + pointLength);
		
		PathElement segment = null;

		for (PathElement s : segments) {
			if ((pointLength - s.getLength()) <= 0) {
				segment = s;
				break;
			} else {
				pointLength -= s.getLength();
			}
		}

		if (segment != null) {
			Logger.getLogger("").log(Level.INFO, "Segment Length= " + segment.getLength());
			return segment.interpolatePoint(pointLength / segment.getLength());
		} else {
			return getLastSegment().getEndPoint();
		}

	}
}
