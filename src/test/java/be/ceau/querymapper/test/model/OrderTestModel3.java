package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Order;


public class OrderTestModel3 {

	@Order(index = -1)
	private String field1;

	public void setField1(String field1) {
		this.field1 = field1;
	}

}
