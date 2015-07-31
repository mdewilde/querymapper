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

import be.ceau.querymapper.enums.LimitType;

/**
 * Map instance fields of Java objects to be used in an SQL <code>LIMIT</code> clause.<br/>
 * Note that annotating any field that is not a primitive or boxed <code>Byte</code>, <code>Short</code>, <code>Integer</code> or <code>Long</code> will cause an <code><strong>IllegalAnnotationException</strong></code> to be thrown.<br/>
 * Note that as both LimitType.ROWS and LimitType.OFFSET should be a nonnegative integer, validation may be desirable for fields with this annotation.
 * @author Marceau Dewilde
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * <code>value</code> (<strong>optional</strong>): the type of Limit the annotated field represents.<br/>
	 * Default value is LimitType.ROWS.
	 */
	public LimitType value() default LimitType.ROWS;

}