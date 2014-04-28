package com.gordoncaleb.client.util;

public interface ResourceLoadEventHandler {

	public void loadComplete();

	public void loadProgress(double progress);

}
