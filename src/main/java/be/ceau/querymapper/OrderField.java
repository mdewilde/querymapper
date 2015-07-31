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

import be.ceau.querymapper.annotations.Order;
import be.ceau.querymapper.enums.SortOrder;

public class OrderField implements Comparable<OrderField> {

	private final String column;
	private final String table;
	private final SortOrder sortOrder;
	private final int index;

	static OrderField extract(Field field) {
		Order order = field.getAnnotation(Order.class);
		if (order != null) {
			String column;
			if (order.column() != null && !order.column().isEmpty()) {
				column = order.column();
			} else {
				column = field.getName();
			}
			return new OrderField(column, order.table(), order.order(), order.index());
		}
		return null;
	}
	
	private OrderField(String column, String table, SortOrder sortOrder, int index) {
		if (column == null || column.trim().length() == 0) {
			throw new IllegalArgumentException("column argument may not be blank");
		}
		if (table.trim().length() == 0) {
			table = null;
		}
		if (sortOrder == null) {
			throw new IllegalArgumentException("SortOrder argument may not be blank");
		}
		if (index < 0) {
			throw new IllegalAnnotationException("index must be equal to or greater than zero");
		}
		this.column = column;
		this.table = table;
		this.sortOrder = sortOrder;
		this.index = index;
	}
	
	String getColumn() {
		return column;
	}

	String getTable() {
		return table;
	}

	SortOrder getSortOrder() {
		return sortOrder;
	}

	int getIndex() {
		return index;
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
		sb.append(sortOrder.getCode());
		return sb;
	}
	
	@Override
	public int compareTo(OrderField o) {
		return this.index - o.index;
	}

}
