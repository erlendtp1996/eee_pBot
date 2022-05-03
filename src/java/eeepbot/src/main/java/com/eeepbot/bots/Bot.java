package com.eeepbot.bots;

import java.util.Map;

public interface Bot extends Runnable {
	public void action();
	public void setProperties(Map<String, String> properties);
}
