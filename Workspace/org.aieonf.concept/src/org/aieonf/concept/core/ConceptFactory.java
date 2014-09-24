package org.aieonf.concept.core;

import java.util.Iterator;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;

/**
 * Creates a concept from a descriptor
 * @author keesp
 *
 */
public class ConceptFactory
{

	/**
	 * Create a concept from the given descriptor
	 * @param descriptor
	 * @return
	 * @throws ConceptException
	*/
	public static IConcept create( IDescriptor descriptor ) throws ConceptException
	{
		Iterator<String> keys = descriptor.iterator();
		IConcept concept = new Concept();
		String key, value;
		while( keys.hasNext() ){
			key = keys.next();
			value = descriptor.get( key );
			if( value == null )
				continue;
			concept.set( key, value );
		}
		return concept;
	}
}
