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
package be.ceau.querymapper.enums;

public enum Comparison {
	
	EQUALS("="),
	DOES_NOT_EQUAL("<>"),
	LIKE("LIKE") {

		@Override
		public Object transform(Object value, FieldType fieldType) {
			return "%".concat(Comparison.toString(value, fieldType)).concat("%");
		}

	},
	STARTS_WITH("LIKE") {

		@Override
		public Object transform(Object value, FieldType fieldType) {
			return Comparison.toString(value, fieldType).concat("%");
		}

	},
	ENDS_WITH("LIKE") {

		@Override
		public Object transform(Object value, FieldType fieldType) {
			return "%".concat(Comparison.toString(value, fieldType));
		}

	},
	LESS("<"),
	GREATER(">"),
	LESS_OR_EQUAL("<="),
	GREATER_OR_EQUAL(">="),
	IN("IN");

	private final String string;
	private Comparison(String string) {
		this.string = string;
	}

	public String toString() {
		return string;
	}

	/**
	 * Transforms the value of a JavaBean field so it can be used in an SQL query with <code>this</code> comparison.<br/>
	 * As an example, a value that is to be used with a LIKE query will need to have percentage characters added in front and at the end of it.
	 * @throws NullPointerException if Object <code>value</code> argument or FieldType argument is <strong><code>null</code></strong>. 
	 */
	public Object transform(Object value, FieldType fieldType) {
		switch (fieldType) {
			case COLLECTION:
			case NUMBER:
				return value;
			case ENUM:
			case OTHER:
			default:
				return toString(value, fieldType);
		}
	}

	private static String toString(Object value, FieldType fieldType) {
		return fieldType == FieldType.ENUM ? ((Enum<?>) value).name() : String.valueOf(value);
	}
	
}
