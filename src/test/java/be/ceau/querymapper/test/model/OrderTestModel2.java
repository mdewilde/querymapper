package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Order;


public class OrderTestModel2 {

	@Order(index = 1)
	private String fieldOrder1;
	
	@Order(index = 0)
	private String fieldOrder0;

	public void setFieldOrder1(String fieldOrder1) {
		this.fieldOrder1 = fieldOrder1;
	}

	public void setFieldOrder0(String fieldOrder0) {
		this.fieldOrder0 = fieldOrder0;
	}

}
