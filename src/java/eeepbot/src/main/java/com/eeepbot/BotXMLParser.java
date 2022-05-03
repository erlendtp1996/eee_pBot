package com.eeepbot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eeepbot.bots.Bot;

public class BotXMLParser {
	
	public List<Bot> bots;
	private NodeList botNodes;
	private File source;
	
	public BotXMLParser() {
		this.bots = new ArrayList<Bot>();
	}	
	
	public BotXMLParser(File source) {
		this.source = source;
		this.bots = new ArrayList<Bot>();
	}
	
	public List<Bot> getBots() {
		return this.bots;
	}
	
	// interact with this
	public void parseXMLDefinition() {
		try {
			generateBotNodes();	
			iterateThroughBotNodes();
		} catch (Exception e) {
			
		}
	}
	
	private void addBot(Bot bot) {
		if (bot != null && bots != null) {
			bots.add(bot);
		}
	}
	
	private Bot createBot(Element element) {
		String className = resolveBotName(element);
		Map<String, String> propertyMapping = generateProperties(element.getElementsByTagName("property"));
		return createBot(className, propertyMapping);
	}
	
	private Bot createBot(String className, Map<String, String> propertyMapping) {
		Bot bot = null;
		try {
			bot = (Bot) createClassInstance(className);
			bot.setProperties(propertyMapping);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Cannot create bot: " + className);
		} 
		return bot;
	}
	
	private Object createClassInstance(String className) throws Exception {
		return Class.forName(className).newInstance();
	}
	
	private void generateBotNodes() throws Exception { 
		Document xmlDocument = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(source);
		xmlDocument.getDocumentElement().normalize();
		botNodes = xmlDocument.getElementsByTagName("bot");
	}

	private Map<String, String> generateProperties(NodeList properties) {
		Map<String, String> propertyMapping = new HashMap<String, String>();
		for (int i = 0; i < properties.getLength(); i++) {
			Node currentProperty = properties.item(i);
			NamedNodeMap attributeMap = currentProperty.getAttributes();
			propertyMapping.put(attributeMap.getNamedItem("id").getTextContent(), currentProperty.getTextContent().trim()); 
		}
		return propertyMapping;
	}
	
	private boolean isBotActive(Element element) {
		Node node = element.getElementsByTagName("isActive").item(0);
		return node != null ? Boolean.parseBoolean(element.getElementsByTagName("isActive").item(0).getTextContent()) : false;
	}
	
	private boolean isNodeTypeElement(Node node) {
		return node.getNodeType() == Node.ELEMENT_NODE;
	}
	
	private void iterateThroughBotNodes() throws Exception {
		Bot parsedBot;
		for (int i = 0; i < botNodes.getLength(); i++) {
			Node currentBotNode = botNodes.item(i);
			
			if (isNodeTypeElement(currentBotNode)) {
				Element element = (Element) currentBotNode;
				
				if (isBotActive(element)) {
					parsedBot = createBot(element); 
					addBot(parsedBot);
				}
			}
		}	
	}
	
	private String resolveBotName(Element element) {
		return element.getElementsByTagName("package").item(0).getTextContent() + "." +
				element.getElementsByTagName("name").item(0).getTextContent();
	}

}
