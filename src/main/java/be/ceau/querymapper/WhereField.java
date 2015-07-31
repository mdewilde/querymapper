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

import static be.ceau.querymapper.QueryMapper.ESCAPE;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import be.ceau.querymapper.annotations.Where;
import be.ceau.querymapper.enums.Comparison;
import be.ceau.querymapper.enums.FieldType;

public class WhereField {
	
	private final Field field;
	private final FieldType fieldType;
	private final String column;
	private final String table;
	private final Comparison comparison;
	private final List<WhereField> nestedQueryFields;
	
	static WhereField extract(Field field) {
		Where where = field.getAnnotation(Where.class);
		if (where != null) {
			field.setAccessible(true);
			String column;
			if (where.column() != null && !where.column().isEmpty()) {
				column = where.column();
			} else {
				column = field.getName();
			}
			List<WhereField> nestedQueryFields = new ArrayList<WhereField>();
			// TODO
			// do not scan java.* classes for our annotation
			//			if (!field.getType().getCanonicalName().startsWith("java.")) {
			//				for (Field nestedfield : field.getType().getDeclaredFields()) {
			//					WhereField nestedQueryField = extractQueryField(nestedfield);
			//					if (nestedQueryField != null) {
			//						nestedQueryFields.add(nestedQueryField);
			//					}
			//				}
			//			}
			return new WhereField(field, column, where.table(), where.comparison(), nestedQueryFields);
		}
		return null;
	}
	
	private WhereField(Field field, String column, String table, Comparison comparison, Collection<WhereField> nestedQueryFields) {
		if (field == null) {
			throw new IllegalArgumentException("Field argument may not be null");
		}
		if (comparison == null) {
			throw new IllegalArgumentException("Comparison argument may not be null");
		}
		this.field = field;
		this.fieldType = FieldType.lookup(field.getType());
		this.column = column;
		this.table = table;
		this.comparison = comparison;
		if (nestedQueryFields != null) {
			this.nestedQueryFields = Collections.unmodifiableList(new ArrayList<WhereField>(nestedQueryFields));
		} else {
			this.nestedQueryFields = Collections.emptyList();
		}
	}

	Field getField() {
		return field;
	}

	FieldType getFieldType() {
		return fieldType;
	}

	String getColumn() {
		return column;
	}

	String getTable() {
		return table;
	}

	Comparison getComparison() {
		return comparison;
	}

	List<WhereField> getNestedQueryFields() {
		return nestedQueryFields;
	}

	StringBuilder toStringBuilder() {
		StringBuilder sb = new StringBuilder();
		if (table != null && !table.isEmpty()) {
			sb.append(ESCAPE);
			sb.append(table);
			sb.append(ESCAPE);
			sb.append(".");
		}
		sb.append(ESCAPE);
		sb.append(column);
		sb.append(ESCAPE);
		sb.append(" ");
		sb.append(comparison.toString());
		sb.append(" ");
		if (fieldType == FieldType.COLLECTION) {
			sb.append("(");
		}
		sb.append(":");
		sb.append(column);
		if (fieldType == FieldType.COLLECTION) {
			sb.append(")");
		}
		return sb;
	}

}
