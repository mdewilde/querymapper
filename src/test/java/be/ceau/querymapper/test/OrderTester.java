package be.ceau.querymapper.test;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.querymapper.IllegalAnnotationException;
import be.ceau.querymapper.QueryMapper;
import be.ceau.querymapper.test.model.OrderTestModel1;
import be.ceau.querymapper.test.model.OrderTestModel2;
import be.ceau.querymapper.test.model.OrderTestModel3;

public class OrderTester {

	@Test
	public void orderAnnotationOnNullFieldProducesOrderClause() {
		OrderTestModel1 model = new OrderTestModel1();
		model.setField1(null);
		QueryMapper<OrderTestModel1> mapper = QueryMapper.forClass(OrderTestModel1.class);
		Assert.assertEquals(" ORDER BY `field1` ASC ", mapper.toOrderClause());
	}
	
	@Test
	public void orderClauseIsOrderedAccordingToIndexValue() {
		OrderTestModel2 model = new OrderTestModel2();
		model.setFieldOrder1("");
		model.setFieldOrder0(null);
		QueryMapper<OrderTestModel2> mapper = QueryMapper.forClass(OrderTestModel2.class);
		Assert.assertEquals(" ORDER BY `fieldOrder0` ASC, `fieldOrder1` ASC ", mapper.toOrderClause());
	}

	@Test(expected = IllegalAnnotationException.class)
	public void indexValueMustBePositiveInteger() {
		QueryMapper.forClass(OrderTestModel3.class).toOrderClause();
	}

}
