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
import java.util.Iterator;
import java.util.List;

/**
 * Objects of this class represent a hierarchy of Field objects, 
 * with entries from the root class and each of the nested fields.
 */
public class NestedField implements Iterable<Field> {

	private final List<Field> hierarchy;
	
	public NestedField(Field root) {
		this.hierarchy = new ArrayList<Field>();
		this.hierarchy.add(root);
	}
	
	/**
	 * Add a Field to the hierarchy of Field objects.<br/>
	 * Field objects <strong>must</strong> be added in the order of the original class hierarchy, 
	 * starting from the root class, and ending with the actual nested Field.
	 * @throws IllegalArgumentException if Field argument <code>null</code>
	 * @throws IllegalArgumentException if Field argument is not a field of the previous Field in the established hierarchy
	 */
	public void nest(Field field) {
		if (field == null) {
			throw new IllegalArgumentException("The Field argument is null");
		}
		if (!hierarchy.isEmpty()) {
			if (!field.getDeclaringClass().equals(last().getType())) {
				throw new IllegalArgumentException("The Field argument is not declared by the class of the Field preceding it in the NestedField hierarchy");
			}
		}
		hierarchy.add(field);
	}

	public Field first() {
		if (!hierarchy.isEmpty()) {
			return hierarchy.get(0);
		} else {
			return null;
		}
	}

	public Field last() {
		if (!hierarchy.isEmpty()) {
			return hierarchy.get(hierarchy.size() - 1);
		} else {
			return null;
		}
	}

	public Iterator<Field> iterator() {
		return Collections.unmodifiableList(hierarchy).iterator();
	}
	
	/**
	 * Retrieves the value of the nested field for the given instance of class T.
	 * @throws IllegalAccessException 
	 * @throws IllegalStateException if no hierarchy has been defined in this NestedField instance.
	 * @throws IllegalArgumentException if type of the argument is not the root class of the NestedField hierarchy.
	 */
	public <T> Object get(T t) throws IllegalAccessException {
		if (hierarchy.isEmpty()) {
			throw new IllegalStateException("No hierarchy has been specified for this NestedField");
		}
		if (t == null) {
			return null;
		}
		if (!t.getClass().equals(first().getDeclaringClass())) {
			throw new IllegalArgumentException("The class of the argument is not the root class for this NestedField instance");
		}
		Object o = t;
		for (Field field : this) {
			o = field.get(t);
			if (o == null) {
				break;
			}
		}
		return o;
	}
	
}
