package be.ceau.querymapper.test;

import org.junit.Assert;

import org.junit.Test;

import be.ceau.querymapper.IllegalAnnotationException;
import be.ceau.querymapper.QueryMapper;
import be.ceau.querymapper.test.model.LimitTestModel1;
import be.ceau.querymapper.test.model.LimitTestModel2;
import be.ceau.querymapper.test.model.LimitTestModel3;
import be.ceau.querymapper.test.model.LimitTestModel4;
import be.ceau.querymapper.test.model.LimitTestModel5;

public class LimitTester {

	@Test(expected = IllegalAnnotationException.class)
	public void limitAnnotationOnNonNumberClassCauseIllegalAnnotationException() {
		QueryMapper.forClass(LimitTestModel1.class);
	}
	
	@Test(expected = IllegalAnnotationException.class)
	public void twoLimitAnnotationsMustHaveDifferentLimitType() {
		QueryMapper.forClass(LimitTestModel2.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void offsetValueMustBePositiveInteger() {
		LimitTestModel3 model = new LimitTestModel3();
		model.setField1(-1);
		QueryMapper.forClass(LimitTestModel3.class).toLimitClause(model);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void partValueMustBePositiveInteger() {
		LimitTestModel4 model = new LimitTestModel4();
		model.setField1(-1L);
		model.setField2(10L);
		QueryMapper.forClass(LimitTestModel4.class).toLimitClause(model);
	}

	@Test
	public void rowsAndPartFieldsCreateLimitClause() {
		LimitTestModel5 model = new LimitTestModel5();
		model.setPart(3L);
		model.setRows(20L);
		String clause = QueryMapper.forClass(LimitTestModel5.class).toLimitClause(model);
		Assert.assertEquals(" LIMIT 60, 20 ", clause);
	}

}
