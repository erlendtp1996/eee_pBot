package com.eeepbot.bots;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth1;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.CreateTweetRequest;
import com.twitter.clientlib.model.TweetCreateResponse;

public abstract class TwitterBot implements Bot {
	
	HttpURLConnection connection;
	URL url;
	String tweets;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String tokenSecret;
	
	TwitterBot (String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.tokenSecret = tokenSecret;
	}
	
	public abstract void generateTweetList() throws Exception;
	
	public abstract void run();
	
	public void action(){
		TwitterApi apiInstance = new TwitterApi();
		TwitterCredentialsOAuth1 creds = new TwitterCredentialsOAuth1(consumerKey, consumerSecret, accessToken, tokenSecret);
		
		apiInstance.setTwitterCredentials(creds);
	    CreateTweetRequest createTweetRequest = new CreateTweetRequest(); // CreateTweetRequest |
	    createTweetRequest.setText("Hello, this is another automated tweet");

	    try {
	           TweetCreateResponse result = apiInstance.tweets().createTweet(createTweetRequest);
	            System.out.println(result);
	    } catch (ApiException e) {
	      System.err.println("Exception when calling TweetsApi#createTweet");
	      System.err.println("Status code: " + e.getCode());
	      System.err.println("Reason: " + e.getResponseBody());
	      System.err.println("Response headers: " + e.getResponseHeaders());
	      e.printStackTrace();
	    }
	    
		/**
		try {
		String requestBody = "{\"text\": \"" + tweets + "\"}";
		
		System.out.println("I am sending a tweet");
		url = new URL("https://api.twitter.com/2/tweets");
		connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Authorization", "Bearer " + twitterToken);
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
		
		System.out.println(line);
		
		} catch (Exception e) {
			System.out.println("Error Tweeting");
			System.out.println(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		**/
		
	}
}
