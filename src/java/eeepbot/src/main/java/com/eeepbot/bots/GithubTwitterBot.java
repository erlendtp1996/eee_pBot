package com.eeepbot.bots;

import java.net.*;

public class GithubTwitterBot extends TwitterBot {
	
	public GithubTwitterBot() {
		super();
	}

	@Override
	public void generateTweetList() throws Exception {
		System.out.println("Generating tweet list");
		//URL url = new URL("https://api.github.com/graphql");
		//HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		//connection.setRequestMethod("POST");
	}

	public void run() {
		try {
			generateTweetList();
			action();
		} catch (Exception e) {
			System.out.println("This bot thread has failed....");
			System.out.println(e);
		}
	}
}
