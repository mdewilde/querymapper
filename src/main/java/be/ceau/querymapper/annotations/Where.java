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

import be.ceau.querymapper.enums.Comparison;

/**
 * Map instance fields of Java objects to be used in an SQL <code>WHERE</code> clause.<br/>
 * <code>@Where</code> annotations placed on <strong>static fields</strong> are quietly <strong>ignored</strong>.
 * @author Marceau Dewilde
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Where {

	/**
	 * <code>column</code> (<strong>optional</strong>): the name of the column that corresponds to the annotated Java field.<br/>
	 * If not set, the exact of the Java field will be used as column name.
	 */
	public String column() default "";

	/**
	 * <code>table</code> (<strong>optional</strong>): the full name or query-specific alias of the table containing the column and annotated field.<br/>
	 * If not set, no value will be used.
	 */
	public String table() default "";
	
	/**
	 * <code>comparison</code> (<strong>optional</strong>): the comparison type to be used for this field in a query.<br/>
	 * Default value is Comparison.EQUALS.
	 */
	public Comparison comparison() default Comparison.EQUALS;

}
