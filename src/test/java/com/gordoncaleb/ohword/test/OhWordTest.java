package com.gordoncaleb.ohword.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gordoncaleb.client.shapes.Rectangle;
import com.gordoncaleb.client.shapes.animation.transitions.LineTo;
import com.gordoncaleb.client.shapes.animation.transitions.MoveTo;
import com.gordoncaleb.client.shapes.animation.transitions.Path;
import com.gordoncaleb.client.shapes.animation.transitions.PathTransition;
import com.gordoncaleb.client.shapes.animations.Animation;
import com.gordoncaleb.client.shapes.animations.Animation.Status;
import com.gordoncaleb.client.shapes.animations.interpolator.Interpolator;

public class OhWordTest {

	public static void main(String[] args) {
		OhWordTest t = new OhWordTest();

		t.pathTest();
		t.animationProgressTest();
	}

	@Test
	public void pathTest() {

		Path p = new Path();

		p.add(new MoveTo(0, 0));

		p.add(new LineTo(100, 100));

		p.add(new LineTo(200, 0));

		System.out.println("Length:" + p.getLength());

		System.out.println(p.interpolatePoint(Interpolator.LINEAR.curve(0.5)));

	}

	@Test
	public void animationProgressTest() {

		Rectangle r = new Rectangle();
		r.setPosition(0, 0);
		r.setWidth(100);
		r.setHeight(100);

		Animation a = PathTransition.linear(100, 100, 200, 0);
		a.setDuration(5000);
		a.setAutoReverse(true);
		a.setCycles(Animation.INFINITE_CYCLES);
		a.play();

		r.setAnimation(a);

		r.propagateAndAnimate(2500);
		assertTrue(a.getProgress() == 0.5);
		assertTrue(a.getStatus() == Status.RUNNING);

		System.out.println("Progress: " + a.getProgress());
		System.out.println("Progress: " + a.getStatus());
		System.out.println("Position: " + r.getPosition());

		r.propagateAndAnimate(1000);
		assertTrue(a.getProgress() == 0.7);
		assertTrue(a.getStatus() == Status.RUNNING);
		System.out.println("Progress: " + a.getProgress());
		System.out.println("Progress: " + a.getStatus());
		System.out.println("Position: " + r.getPosition());

		r.propagateAndAnimate(1000);
		assertTrue(a.getProgress() == 0.9);
		assertTrue(a.getStatus() == Status.RUNNING);
		System.out.println("Progress: " + a.getProgress());
		System.out.println("Progress: " + a.getStatus());
		System.out.println("Position: " + r.getPosition());

		r.propagateAndAnimate(1000);
		assertTrue(a.getProgress() == 0.9);
		assertTrue(a.getStatus() == Status.RUNNING);
		System.out.println("Progress: " + a.getProgress());
		System.out.println("Progress: " + a.getStatus());
		System.out.println("Position: " + r.getPosition());

		r.propagateAndAnimate(1000);
		assertTrue(a.getProgress() == 0.7);
		assertTrue(a.getStatus() == Status.RUNNING);
		System.out.println("Progress: " + a.getProgress());
		System.out.println("Progress: " + a.getStatus());
		System.out.println("Position: " + r.getPosition());

	}

}
