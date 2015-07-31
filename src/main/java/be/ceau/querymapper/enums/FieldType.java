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

import java.util.Collection;

public enum FieldType {

	COLLECTION,
	ENUM,
	NUMBER,
	OTHER;
	
	/**
	 * @return the appropriate FieldType enumtype for the class argument, never <code>null</code>
	 * @throws NullPointerException if <code>clazz</code> argument <code>null</code>
	 */
	public static FieldType lookup(Class<?> clazz) {
		if (Number.class.isAssignableFrom(clazz)) {
			return NUMBER;
		} else if (Collection.class.isAssignableFrom(clazz)) {
			return COLLECTION;
		} else if (clazz.isEnum()) {
			return ENUM;
		} else {
			return OTHER;
		}
	}
	
}
