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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.ceau.querymapper.annotations.Limit;
import be.ceau.querymapper.enums.LimitType;

/**
 * Instances of this class contain all necessary information 
 * to map instances of a given class to syntactically correct SQL LIMIT clauses.
 */
class Limits {
	
	private static final Set<Class<?>> PERMITTED_CLASSES;
	
	static {
		PERMITTED_CLASSES = new HashSet<Class<?>>();
		PERMITTED_CLASSES.add(java.lang.Byte.TYPE);
		PERMITTED_CLASSES.add(java.lang.Short.TYPE);
		PERMITTED_CLASSES.add(java.lang.Integer.TYPE);
		PERMITTED_CLASSES.add(java.lang.Long.TYPE);
		PERMITTED_CLASSES.add(java.lang.Byte.class);
		PERMITTED_CLASSES.add(java.lang.Short.class);
		PERMITTED_CLASSES.add(java.lang.Integer.class);
		PERMITTED_CLASSES.add(java.lang.Long.class);
	}
	
	private final NestedField rows;
	private final NestedField part;
	private final NestedField offset;

	/**
	 * Ways to specify:
	 * <ol>
	 * <li>annotate a field of type short, byte, int or long (primitive or boxed)
	 * <li>annotate a field of a type that contains an <code>@Limit</code> annotated field of type byte, short, int or long (primitive or boxed)
	 * </ol>
	 */
	static List<NestedField> parse(Field field) {
		Limit limit = field.getAnnotation(Limit.class);
		if (limit != null) {
			if (PERMITTED_CLASSES.contains(field.getType())) {
				field.setAccessible(true);
				return Arrays.asList(new NestedField(field));
			} else {
				// TODO: restrict LimitType for @Limit annotation on nested fields
				List<NestedField> nestedFields = new ArrayList<NestedField>();
				for (Field subfield : field.getType().getDeclaredFields()) {
					Limit sublimit = subfield.getAnnotation(Limit.class);
					if (sublimit != null && PERMITTED_CLASSES.contains(subfield.getType())) {
						NestedField nestedfield = new NestedField(field);
						subfield.setAccessible(true);
						nestedfield.nest(subfield);
						nestedFields.add(nestedfield);
					}
				}
				if (!nestedFields.isEmpty()) {
					return nestedFields;
				}
			}
			throw new IllegalAnnotationException("The @Limit annotation may only be applied to fields that are either a primitive or boxed Byte, Short, Integer or Long, or on a field of a type that contains such fields");
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Validates a List of LimitField objects.<br/>
	 * As SQL LIMIT clauses have a specific, limited syntax, care must be used when allowing certain combinations of @Limit annotations.
	 * @throws IllegalAnnotationException if the List of LimitField objects contains an incompatible combination
	 */
	private static void validate(List<NestedField> limitFields) {
		if (limitFields.size() > 2) {
			throw new IllegalAnnotationException("Having more than 2 @Limit annotations in one class is not valid.");
		}
		if (limitFields.size() == 2) {
			if (limitFields.get(0).last().getAnnotation(Limit.class).value() == limitFields.get(1).last().getAnnotation(Limit.class).value()) {
				throw new IllegalAnnotationException("Having 2 @Limit annotations with the same LimitType in one class is not valid.");
			} else if (limitFields.get(0).last().getAnnotation(Limit.class).value() != LimitType.ROWS && limitFields.get(1).last().getAnnotation(Limit.class).value() != LimitType.ROWS) {
				throw new IllegalAnnotationException("Having both LimitType.OFFSET and LimitType.PART annotations is not valid.");
			}
		} else if (limitFields.size() == 1) {
			if (limitFields.get(0).last().getAnnotation(Limit.class).value() != LimitType.ROWS) {
				throw new IllegalAnnotationException("If only 1 @Limit annotation is specified in a given class, its LimitType value must be LimitType.ROWS.");
			}
		}
	}

	static Limits from(List<NestedField> limitFields) {
		if (limitFields.isEmpty()) {
			return null;
		}
		validate(limitFields);
		Map<LimitType, NestedField> map = new EnumMap<LimitType, NestedField>(LimitType.class);
		for (NestedField nestedField : limitFields) {
			map.put(nestedField.last().getAnnotation(Limit.class).value(), nestedField);
		}
		return new Limits(map.get(LimitType.OFFSET), map.get(LimitType.PART), map.get(LimitType.ROWS));
	}

	private Limits(NestedField offset, NestedField part, NestedField rows) {
		if (rows == null) {
			throw new IllegalArgumentException("NestedField rows argument is null");
		}
		this.offset = offset;
		this.part = part;
		this.rows = rows;
	}
	
	<T> String toLimitClause(T t) throws IllegalAccessException {
		if (this.rows != null) {
			Number numrows = (Number) this.rows.get(t);
			if (numrows == null) {
				return "";
			} else if (numrows.longValue() < 1) {
				throw new IllegalArgumentException("The value of fields annotated with @Limit of type LimitType.ROWS must be a positive integer");
			} else if (this.offset != null) {
				Number numoffset = (Number) this.offset.get(t);
				if (numoffset != null) {
					if (numoffset.longValue() < 0) {
						throw new IllegalArgumentException("The value of fields annotated with @Limit of type LimitType.OFFSET must be a non-negative integer");
					} else {
						return " LIMIT " + String.valueOf(numoffset) + ", " + String.valueOf(numrows) + " ";
					}
				}
			} else if (this.part != null) {
				Number numpart = (Number) this.part.get(t);
				if (numpart != null) {
					if (numpart.longValue() < 0) {
						throw new IllegalArgumentException("The value of fields annotated with @Limit of type LimitType.PART must be non-negative integer");
					} else {
						return " LIMIT " + String.valueOf(numpart.longValue() * numrows.longValue()) + ", " + String.valueOf(numrows) + " ";
					}
				}
			}
			return " LIMIT " + String.valueOf(numrows) + " ";
		} else {
			return "";
		}
	}

}
