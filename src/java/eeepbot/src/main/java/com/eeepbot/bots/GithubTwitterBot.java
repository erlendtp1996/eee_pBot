package com.eeepbot.bots;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubTwitterBot extends TwitterBot {
	
	private URL url;
	private HttpURLConnection connection;
	
	private String githubToken;
	private String requestBody = "{ \"query\":\"query { viewer { login } } \" }";
	
	public GithubTwitterBot(String githubToken, String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
		super(consumerKey, consumerSecret, accessToken, tokenSecret);
		this.githubToken = githubToken;
	}	

	@Override
	public void generateTweetList() {
		try {
			url = new URL("https://api.github.com/graphql");
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + githubToken);
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
			
			tweets = response.toString();	
		}
		catch (Exception e) {
			
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
