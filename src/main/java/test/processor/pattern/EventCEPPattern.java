package test.processor.pattern;

import org.apache.flink.cep.pattern.Pattern;

import test.processor.cep.EventCEPData;
import test.processor.rule.Rule;

public class EventCEPPattern {


	private Rule rule;

	private Pattern<EventCEPData, ?> pattern;

	public EventCEPPattern(Rule rule, Pattern<EventCEPData, ?> pattern) {
		super();
		this.rule = rule;
		this.pattern = pattern;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Pattern<EventCEPData, ?> getPattern() {
		return pattern;
	}

	public void setPattern(Pattern<EventCEPData, ?> pattern) {
		this.pattern = pattern;
	}

}
