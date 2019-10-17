package test.processor.rule.condition;

import java.io.Serializable;
import java.util.ArrayList;

public class Condition implements Serializable {

	private static final long serialVersionUID = -6359063688366713357L;
	
	private String company;
	
	private ArrayList<String> products = new ArrayList<String>();

	public Condition(String company, ArrayList<String> products) {
		this.setCompany(company);
		this.setProducts(products);
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public ArrayList<String> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<String> products) {
		this.products = products;
	}
	
	
}
