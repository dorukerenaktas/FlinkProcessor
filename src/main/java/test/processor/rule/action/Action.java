package test.processor.rule.action;

import java.io.Serializable;

public class Action implements Serializable {

	private static final long serialVersionUID = -6941797320868169400L;

	private String operation;

	public Action(String operation) {
		this.setOperation(operation);
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	
}
