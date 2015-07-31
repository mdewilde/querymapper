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

public enum LimitType {

	/**
	 * The offset of the first row to return.
	 */
	OFFSET,
	/**
	 * The offset of the first row to return divided by the maximum number of rows to return.
	 */
	PART,
	/**
	 * The maximum number of rows to return.
	 */
	ROWS;
	
}
