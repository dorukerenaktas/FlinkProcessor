package test.processor.cep;

import org.apache.flink.api.java.tuple.Tuple4;

public class EventCEPData extends Tuple4<Long, String, String, Long> {

	private static final long serialVersionUID = -4276762997201945857L;
	
	public EventCEPData() {
		super();
	}

	public EventCEPData(Long order, String company, String product, Long price) {
		super(order, company, product, price);
	}
	
	public Long getOrder() {
		return this.f0;
	}

	public String getCompany() {
		return this.f1;
	}

	public String getProduct() {
		return this.f2;
	}
	
	public void setProduct(String product) {
		this.setField(product, 2);;
	}

	public Long getPrice() {
		return this.f3;
	}
}
