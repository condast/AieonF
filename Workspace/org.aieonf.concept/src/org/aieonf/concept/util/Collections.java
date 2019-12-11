package org.aieonf.concept.util;

import java.util.Collection;

import org.aieonf.concept.IDescribable;

public class Collections
{

	/**
	 * Get all the concepts complying with the given descriptor.
	 * Strict determines a filter for the concepts based on version info
	 *
	 * @param results Enumeration
	 * @return IConcept
	*/
	public static IDescribable getFirst( Collection<? extends IDescribable> results )
	{
	  if(( results == null ) || ( results.size() == 0 ))
	    return null;
	  return results.iterator().next();
	}
}
