package com.eeepbot.bots;

import java.util.List;

public abstract class TwitterBot implements Bot, Runnable {
	
	private String twitterClientToken;
	private List<String> tweets;
	
	public abstract void generateTweetList() throws Exception;
	
	public abstract void run();
	
	public void action() {
		System.out.println("I am sending a tweet");
	}
}
