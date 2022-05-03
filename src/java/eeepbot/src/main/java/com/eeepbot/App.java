package com.eeepbot;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
		BotXMLParser xmlParser = new BotXMLParser(new File("bots.xml"));
		xmlParser.parseXMLDefinition();
		executeThreads(xmlParser.getBots());
	}

	public static void executeThreads(List<? extends Runnable> runners) {
		if (runners != null && !runners.isEmpty()) {
			for (Runnable runner : runners) {
				if (runner != null) {
					runner.run();
				}
			}
		}
	}
}
