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
package be.ceau.querymapper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import be.ceau.querymapper.enums.SortOrder;

/**
 * Map instance fields of Java objects to be used in an SQL <code>ORDER BY</code> clause.<br/>
 * <code>@Order</code> annotations placed on <strong>static fields</strong> are quietly <strong>ignored</strong>.
 * @author Marceau Dewilde
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

	/**
	 * The name of the column that corresponds to the annotated Java field (<strong>optional</strong>).<br/>
	 * Default value is the name of the annotated Java property.
	 */
	public String column() default "";

	/**
	 * The full name or query-specific alias of the table containing the column and annotated field (<strong>optional</strong>).<br/>
	 * If not set, no value will be used.
	 */
	public String table() default "";

	/**
	 * The sort order to used when sorting results on the annotated field (<strong>optional</strong>).<br/>
	 * Default value is <code>SortOrder.ASCENDING</code>.
	 */
	public SortOrder order() default SortOrder.ASCENDING;
	
	/**
	 * The zero-based index of this field in the resulting order clause (<strong>optional</strong>).<br/>
	 * The default value is <code>0</code>.
	 */
	public int index() default 0;

	
	
}
