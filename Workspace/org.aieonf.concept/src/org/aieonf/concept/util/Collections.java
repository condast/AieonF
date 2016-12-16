package org.aieonf.concept.util;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class Collections
{

	/**
	 * Get all the concepts complying with the given descriptor.
	 * Strict determines a filter for the concepts based on version info
	 *
	 * @param results Enumeration
	 * @return IConcept
	*/
	public static IDescribable<? extends IDescriptor> getFirst( Collection<? extends IDescribable<?>> results )
	{
	  if(( results == null ) || ( results.size() == 0 ))
	    return null;
	  return results.iterator().next();
	}
}
