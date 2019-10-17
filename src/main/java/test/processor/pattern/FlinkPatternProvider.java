package test.processor.pattern;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.processor.cep.EventCEPData;
import test.processor.rule.Rule;
import test.processor.rule.RuleProvider;

public class FlinkPatternProvider implements PatternProvider {
	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private RuleProvider provider;

	private ArrayList<Rule> rules;

	public FlinkPatternProvider(RuleProvider provider) {
		this.provider = provider;
		this.rules = this.provider.fetch();
	}

	@Override
	public ArrayList<EventCEPPattern> provide() {
		ArrayList<EventCEPPattern> patterns = new ArrayList<>();
		for (Rule rule : this.rules) {
			patterns.add(new EventCEPPattern(rule, create(rule)));
		}
		return patterns;
	}
	
	private Pattern<EventCEPData, ?> create(final Rule rule) {

		SimpleCondition<EventCEPData> condition = new SimpleCondition<EventCEPData>() {
			private static final long serialVersionUID = 7059243663917563682L;

			@Override
			public boolean filter(EventCEPData data) throws Exception {
				if (!rule.getCondition().getCompany().equals(data.getCompany())) {
					return false;
				}
				
				int index = 0;
				ArrayList<String> products = rule.getCondition().getProducts();
				while (index < products.size() && data.getProduct().contains(products.get(index))) {
					index++;
				}
				
				if (index == products.size()) {
					LOG.debug("Pattern send: " + data.toString());
					return true;
				}
				return false;
			}
		};

		return Pattern.<EventCEPData>begin("filter").where(condition);
	}

}
