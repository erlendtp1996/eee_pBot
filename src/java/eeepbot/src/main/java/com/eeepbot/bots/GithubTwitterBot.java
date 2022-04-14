package com.eeepbot.bots;

import java.io.*;
import java.net.*;

public class GithubTwitterBot extends TwitterBot {
	
	private HttpURLConnection connection;
	private String token;
	private String requestBody = "{ \"query\":\"query { viewer { login } } \" }";
	
	public GithubTwitterBot() {
		super();
	}
	
	public GithubTwitterBot(String token) {
		super();
		this.token = token;
	}	

	@Override
	public void generateTweetList() throws Exception {
		System.out.println("Generating tweet list");
		URL url = new URL("https://api.github.com/graphql");
		connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + token);
		connection.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(requestBody);
		wr.close();
		
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append("\r");
		}
		rd.close();
		System.out.println(response.toString());
	}

	public void run() {
		try {
			generateTweetList();
			action();
		} catch (Exception e) {
			System.out.println("This bot thread has failed....");
			System.out.println(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
