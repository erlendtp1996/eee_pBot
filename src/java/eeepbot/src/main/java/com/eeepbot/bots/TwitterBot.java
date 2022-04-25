package com.eeepbot.bots;

import java.util.Map;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth1;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.CreateTweetRequest;
import com.twitter.clientlib.model.TweetCreateResponse;

public abstract class TwitterBot implements Bot {
	
	private String tweets;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String tokenSecret;
	
	public TwitterBot() {
		// TODO Auto-generated constructor stub
	}

	// get something to tweet
	public abstract void generateTweetList();

	public void run() {
		try {
			generateTweetList();
			action();
		} catch (Exception e) {
			System.out.println("This bot thread has failed....");
			System.out.println(e);
		}
	}
	
	public void action() {
		TwitterApi apiInstance = new TwitterApi();
		TwitterCredentialsOAuth1 creds = new TwitterCredentialsOAuth1(consumerKey, consumerSecret, accessToken, tokenSecret);
		
		apiInstance.setTwitterCredentials(creds);
	    CreateTweetRequest createTweetRequest = new CreateTweetRequest();
	    createTweetRequest.setText(tweets);

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
	}
	
	public void setProperties(Map<String, String> props) {
		setConsumerKey(props.get("twitterConsumerKey"));
		setConsumerSecret(props.get("twitterConsumerSecret"));
		setAccessToken(props.get("twitterAccessToken"));
		setTokenSecret(props.get("twitterTokenSecret"));
	}
	
	public void setTweets(String tweets) {
		this.tweets = tweets;
	}
	
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
}
