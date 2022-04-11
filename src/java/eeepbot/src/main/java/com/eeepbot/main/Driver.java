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
		List<TwitterBot> bots = new ArrayList<TwitterBot>();
		
		bots.add(new GithubTwitterBot());
		
		for (TwitterBot bot: bots) {
			bot.run();
		}
	}
	
	public static void readProps() throws Exception {
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
	
	/**
	 *  1. Integrate with S3 bucket to get active bots
	 *  2. Get github oauth token & send graph ql request
	 *  3. Authenticate with 
	 */
}
