package test.processor.rule;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import test.processor.rule.action.Action;
import test.processor.rule.condition.Condition;

public class Rule implements Serializable {

	private static final long serialVersionUID = 4613900875144490002L;
	
	private long time;

	private String name;

	private Condition condition;

	private Action action;

	public Rule() {
		super();
	}

	public Rule(long time, String name, Condition condition, Action action) {
		super();
		this.setTime(time);
		this.setName(name);
		this.setCondition(condition);
		this.setAction(action);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		
		JSONObject ruleCondition = new JSONObject();
		ruleCondition.put("company", this.getCondition().getCompany());
		ruleCondition.put("products", this.getCondition().getProducts());
		json.put("condition", ruleCondition);

		JSONObject ruleAction = new JSONObject();
		ruleAction.put("operation", this.getAction().getOperation());
		json.put("action", ruleAction);

		return json;
	}
}
