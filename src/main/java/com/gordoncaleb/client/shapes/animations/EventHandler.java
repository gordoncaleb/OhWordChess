package com.gordoncaleb.client.shapes.animations;

public interface EventHandler<T extends Event> {
	public void handle(T event);
}
