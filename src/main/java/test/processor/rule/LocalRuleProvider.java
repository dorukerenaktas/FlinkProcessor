package test.processor.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import test.processor.rule.action.Action;
import test.processor.rule.condition.Condition;

public class LocalRuleProvider implements RuleProvider {

	private static final String NODE_ATTRIBUTE_WINDOW = "window";
	private static final String NODE_RULE = "rule";
	private static final String NODE_RULE_NAME = "name";
	private static final String NODE_RULE_CONDITION = "condition";
	private static final String NODE_RULE_CONDITION_COMPANY = "company";
	private static final String NODE_RULE_CONDITION_PRODUCT = "product";
	private static final String NODE_RULE_ACTION = "action";
	private static final String NODE_RULE_ACTION_OPERATION = "operation";

	public ArrayList<Rule> fetch() {
		ArrayList<Rule> rules = new ArrayList<Rule>();

		try {
			File file = new File("rules.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList rulesNode = doc.getElementsByTagName(NODE_RULE);

			for (int i = 0; i < rulesNode.getLength(); i++) {
				Node ruleNode = rulesNode.item(i);
				if (ruleNode.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) ruleNode;
					long time = Long.parseLong((element.getAttribute(NODE_ATTRIBUTE_WINDOW) != "") ? element.getAttribute(NODE_ATTRIBUTE_WINDOW) : "0");
					String name = getChild(element, NODE_RULE_NAME).getTextContent();

					Element conditionNode = getChild(element, NODE_RULE_CONDITION);
					String company = getChild(conditionNode, NODE_RULE_CONDITION_COMPANY).getTextContent();
					ArrayList<String> products = new ArrayList<>(Arrays.asList(getChild(conditionNode, NODE_RULE_CONDITION_PRODUCT).getTextContent().split(",")));
					Condition condition = new Condition(company, products);

					Element actionNode = getChild(element, NODE_RULE_ACTION);
					String operation = getChild(actionNode, NODE_RULE_ACTION_OPERATION).getTextContent();
					Action action = new Action(operation);

					// Save rule
					rules.add(new Rule(time, name, condition, action));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rules;
	}
	
	 public static Element getChild(Element parent, String name) {
		    for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
		      if (child instanceof Element && name.equals(child.getNodeName())) {
		        return (Element) child;
		      }
		    }
		    return null;
		  }

}
