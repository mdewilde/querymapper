package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Order;


public class OrderTestModel1 {

	@Order
	private String field1;

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
	
}
