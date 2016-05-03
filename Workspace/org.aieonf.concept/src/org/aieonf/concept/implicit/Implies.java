package org.aieonf.concept.implicit;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;

/**
 * There are numerous situations when two descriptors can be considered the same. The most simple situation is when the
 * objects.equals() method matches, but often the value of attribute should be the same, such as with a category. 
 * @author Kees
 *
 * @param <T>
 * @param <U>
 */
public interface Implies<T extends IDescriptor, U extends IDescriptor> extends Comparable<U>{

	/**
	 * Get the rules when two descriptors are considered the same. By default, it is 'equals',
	 * but for instance a category s the same when the 'category' attributes has the same name
	 * @author Kees Pieters
	 */
	public enum Conditions
	{
		EQUAL,
		ID,
		ON_ATTRIBUTE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	
}
