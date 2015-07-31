/*
	Copyright 2015 Marceau Dewilde <m@ceau.be>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.querymapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryMapper<T> {

	private static final Logger logger = LoggerFactory.getLogger(QueryMapper.class);
	
	static final String ESCAPE = "`";
	
	/**
	 * A list of WhereField objects, allowing quick mapping of JavaBean properties to SQL queries and parameter maps
	 */
	private final List<WhereField> whereFields;

	/**
	 * A list of OrderField objects, to allow for quick creation of syntactically correct SQL ORDER BY clauses
	 */
	private final List<OrderField> orderFields;

	/**
	 * A Limits object which contains all information necessary to construct a LIMIT clause
	 */
	private final Limits limits;

	/**
	 * Static factory for creation of a new, reusable QueryMapper for a class.
	 * @return a new, reusable and threadsafe instance of the QueryMapper
	 * @throws IllegalAnnotationException if the annotations for the given class are not applied in a valid manner.
	 */
	public static <T> QueryMapper<T> forClass(Class<T> clazz) {
		List<WhereField> whereFields = new ArrayList<WhereField>();
		List<OrderField> orderFields = new ArrayList<OrderField>();
		List<NestedField> limitFields = new ArrayList<NestedField>();
		for (Field field : clazz.getDeclaredFields()) {
			WhereField where = WhereField.extract(field);
			if (where != null) {
				whereFields.add(where);
			}
			OrderField order = OrderField.extract(field);
			if (order != null) {
				orderFields.add(order);
			}
			limitFields.addAll(Limits.parse(field));
		}
		Collections.sort(orderFields);
		return new QueryMapper<T>(whereFields, orderFields, Limits.from(limitFields));
	}

	/**
	 * Private constructor for static factory only
	 */
	private QueryMapper(List<WhereField> whereFields, List<OrderField> orderFields, Limits limits) {
		this.whereFields = Collections.unmodifiableList(whereFields);
		this.orderFields = Collections.unmodifiableList(orderFields);
		this.limits = limits;
	}

	/**
	 * Converts objects of the target class to a full and complete where clause.<br/>
	 * @return a new, possibly empty, String on every invocation.
	 */
	public String toWhereClause(T t) {
		if (t == null) {
			return "";
		}
		StringBuilder where = new StringBuilder();
		for (WhereField whereField : whereFields) {
			try {
				Object o = whereField.getField().get(t);
				if (o != null) {
					if (where.length() > 0) {
						where.append(" AND ");
					} else {
						where.append(" WHERE ");
					}
					where.append(whereField.toStringBuilder());
				}
			} catch (IllegalArgumentException e) {
				logger.error("toWhereClause({} t)", t.getClass().getSimpleName(), e);
			} catch (IllegalAccessException e) {
				logger.error("toWhereClause({} t)", t.getClass().getSimpleName(), e);
			}
		}
		if (where.length() > 0) {
			where.append(" ");
		}
		logger.trace("toWhereClause({} t) output: {}", t.getClass().getSimpleName(), where);
		return where.toString();
	}

	/**
	 * Converts objects of the target class to parameter map.<br/>
	 * This conversion process works on the basis of the mapping applied with annotation {@link be.ceau.querymapper.annotations.Where}.<br>
	 * Any annotated property will be included. Values for those properties will be mapped to the determined column name for that property.
	 * @return a new, modifiable Map on every invocation.
	 */
	public Map<String,Object> toParameterMap(T t) {
		Map<String,Object> map = new HashMap<String,Object>();
		if (t == null) {
			return map;
		}
		for (WhereField queryField : whereFields) {
			try {
				Object o = queryField.getField().get(t);
				if (o != null) {
					map.put(queryField.getColumn(), queryField.getComparison().transform(o, queryField.getFieldType()));
					for (WhereField nestedQueryField : queryField.getNestedQueryFields()) {
						map.put(nestedQueryField.getColumn(), nestedQueryField.getComparison().transform(o, queryField.getFieldType()));
					}
				}
			} catch (IllegalArgumentException e) {
				logger.error("toParameterMap({} t)", t.getClass().getSimpleName(), e);
			} catch (IllegalAccessException e) {
				logger.error("toParameterMap({} t)", t.getClass().getSimpleName(), e);
			}
		}
		logger.trace("toParameterMap({} t) output: {}", t.getClass().getSimpleName(), map);
		return map;
	}

	/**
	 * Converts objects of the target class to a full and complete order clause.<br>
	 * This conversion process works on the basis of the mapping applied with annotation {@link be.ceau.querymapper.annotations.Order}.<br>
	 * This method does not accept arguments, as the syntax of the order clause does not depend on the instance values of the mapped class.
	 * @return a new, possibly zero-length, String, never <code>null</code>.
	 */
	public String toOrderClause() {
		StringBuilder order = new StringBuilder();
		for (OrderField orderField : orderFields) {
			if (order.length() > 0) {
				order.append(", ");
			} else {
				order.append(" ORDER BY ");
			}
			order.append(orderField.toStringBuilder());
		}
		if (order.length() > 0) {
			order.append(" ");
		}
		logger.trace("toOrderClause() output: {}", order);
		return order.toString();
	}

	/**
	 * Converts objects of the target class to a full and complete limit clause.<br/>
	 * This conversion process works on the basis of the mapping applied with annotation {@link be.ceau.querymapper.annotations.Limit}.<br>
	 * @return a new, modifiable Map on every invocation.
	 */
	public String toLimitClause(T t) {
		if (t == null) {
			return "";
		} else if (limits == null) {
			return "";
		} else {
			try {
				return limits.toLimitClause(t);
			} catch (IllegalAccessException e) {
				logger.error("toLimitClause({} t)", t.getClass().getSimpleName(), e);
				return "";
			}
		}
	}

}
