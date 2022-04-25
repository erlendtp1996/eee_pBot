package com.eeepbot.main;

import java.io.File;
import java.util.*;
import com.eeepbot.bots.*;

public class Driver {

	private static Map<String, String> props;
	
	public static void main(String[] args) {
		System.out.println("Let's get started!");
		System.out.println("Reading property file");
		try {
			readProps();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println("Executing threads...");
		
//		Will come back to this via properties implementation
//		Bot test = null;
//		try {
//			Class<?> c = Class.forName("com.eeepbot.bots.GithubTwitterBot");
//			test = (Bot) c.newInstance();
//		} catch (Exception e) {
//			System.out.println(e);
//		}
		
		
		List<Bot> bots = new ArrayList<Bot>();
		
		bots.add(new GithubTwitterBot(props.get("githubToken"), 
				props.get("twitterConsumerKey"),  
				props.get("twitterConsumerSecret"),  
				props.get("twitterAccessToken"),  
				props.get("twitterTokenSecret")));
		
		for (Bot bot: bots) {
			bot.run();
		}
	}
	
	public static void readProps() throws Exception {
		//tmp until properties schema is defined
		props = new HashMap<String, String>();
		File propsFile = new File("properties.txt");
		Scanner scan = new Scanner(propsFile);
		while (scan.hasNextLine()) {
			String propertyDefinition = scan.nextLine();
			String[] propertyKeyValuePair = propertyDefinition.split("=");
			props.put(propertyKeyValuePair[0], propertyKeyValuePair[1]);
			System.out.println("Just read in " + propertyKeyValuePair[0] + "=" + propertyKeyValuePair[1]);
		}
		scan.close();
	}
}
