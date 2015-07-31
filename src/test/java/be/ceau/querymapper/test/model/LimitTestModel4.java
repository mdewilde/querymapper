package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Limit;
import be.ceau.querymapper.enums.LimitType;


public class LimitTestModel4 {

	@Limit(LimitType.PART)
	private Long field1;
	
	@Limit(LimitType.ROWS)
	private Long field2;

	public void setField1(Long field1) {
		this.field1 = field1;
	}
	
	public void setField2(Long field2) {
		this.field2 = field2;
	}

}
