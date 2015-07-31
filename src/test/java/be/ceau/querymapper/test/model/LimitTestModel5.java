package be.ceau.querymapper.test.model;

import be.ceau.querymapper.annotations.Limit;
import be.ceau.querymapper.enums.LimitType;


public class LimitTestModel5 {

	@Limit(LimitType.PART)
	private Long part;

	@Limit(LimitType.ROWS)
	private Long rows;

	public void setPart(Long part) {
		this.part = part;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}
		
}
