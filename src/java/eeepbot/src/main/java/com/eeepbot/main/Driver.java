package com.eeepbot.main;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eeepbot.bots.*;

public class Driver {
	
	public static void main(String[] args) {
		List<Bot> bots = new ArrayList<Bot>();
		
		try {
			File file = new File("bots.xml");
			
			//prepare XML
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(file);
			document.getDocumentElement().normalize();
			NodeList botNodes = document.getElementsByTagName("bot");
			
			
			// loop through each bot
			for (int i = 0; i < botNodes.getLength(); i++) {
				Bot tmp;
				
				Node botNode = botNodes.item(i);
				if (botNode.getNodeType() == Node.ELEMENT_NODE) {
					//create each bot
					Element eElement = (Element) botNode;
					String botName = eElement.getElementsByTagName("package").item(0).getTextContent() + "." +
							eElement.getElementsByTagName("name").item(0).getTextContent();
					
					
					//create propertyMap
					Map<String, String> propertyMapping = generateProperties(eElement.getElementsByTagName("property"));
					
					
					
					Class<?> c = Class.forName(botName);
					tmp = (Bot) c.newInstance();
					tmp.setProperties(propertyMapping);
					bots.add(tmp);
					
				}
				
				for (Bot bot: bots) {
					bot.run();
				}
			}		
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public static Map<String, String> generateProperties(NodeList properties) {
		Map<String, String> propertyMapping = new HashMap<String, String>();
		for (int i = 0; i < properties.getLength(); i++) {
			Node currentProperty = properties.item(i);
			NamedNodeMap attributeMap = currentProperty.getAttributes();
			propertyMapping.put(attributeMap.getNamedItem("id").getTextContent(),currentProperty.getTextContent().trim()); 
		}
		return propertyMapping;
	}
}
