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


	
}
