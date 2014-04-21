package com.gordoncaleb.client.shapes.animation;

public interface EventHandler<T extends Event> {
	public void handle(T event);
}
