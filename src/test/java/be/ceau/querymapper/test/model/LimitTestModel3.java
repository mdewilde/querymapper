package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Limit;
import be.ceau.querymapper.enums.LimitType;


public class LimitTestModel3 {

	@Limit(LimitType.ROWS)
	private int field1;

	public int getField1() {
		return field1;
	}

	public void setField1(int field1) {
		this.field1 = field1;
	}
	
}
