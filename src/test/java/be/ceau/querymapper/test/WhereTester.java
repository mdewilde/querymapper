package be.ceau.querymapper.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import be.ceau.querymapper.QueryMapper;
import be.ceau.querymapper.test.model.WhereFilter;

public class WhereTester {

	private QueryMapper<WhereFilter> whereFilterMapper;
	private WhereFilter filter;

	@Before
	public void testPreparation() {
		whereFilterMapper = QueryMapper.forClass(WhereFilter.class);
		filter = new WhereFilter();
	}
	
	@Test
	public void nullFieldReturnsEmptyString() {
		filter.setField1(null);
		Assert.assertEquals("", whereFilterMapper.toWhereClause(filter));
	}
	
	@Test
	public void nullFieldReturnsEmptyMap() {
		filter.setField1(null);
		Assert.assertTrue(whereFilterMapper.toParameterMap(filter).isEmpty());
	}

	@Test
	public void blankFieldReturnsWhereClause() {
		filter.setField1("");
		Assert.assertEquals(" WHERE `field1Name` = :field1Name ", whereFilterMapper.toWhereClause(filter));
	}
	
	@Test
	public void blankFieldReturnsMapWithOneEntry() {
		filter.setField1("");
		Assert.assertTrue(whereFilterMapper.toParameterMap(filter).size() == 1 && whereFilterMapper.toParameterMap(filter).get("field1Name").equals(""));
	}

	@Test
	public void notBlankFieldReturnsWhereClause() {
		filter.setField1("test");
		Assert.assertEquals(" WHERE `field1Name` = :field1Name ", whereFilterMapper.toWhereClause(filter));
	}

	@Test
	public void notBlankFieldReturnsMapWithOneEntry() {
		filter.setField1("test");
		Assert.assertTrue(whereFilterMapper.toParameterMap(filter).size() == 1 && whereFilterMapper.toParameterMap(filter).get("field1Name").equals("test"));
	}

}
