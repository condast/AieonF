package org.aieonf.concept.core;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class EmbeddedAieon extends ConceptWrapper
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 417080451657494437L;

	//Supported attributes
	public enum Attributes{
		Embedded
	}
	
	public EmbeddedAieon(IConcept concept) throws ConceptException
	{
		super(concept);
		super.set( Attributes.Embedded.name(), Boolean.TRUE.toString() );
	}

	/**
	 * Returns true if the given concept is embedded
	 * @return
	*/
	public boolean isEmbedded(){
		return Boolean.valueOf( super.get( Attributes.Embedded.name() ));
	}
	
	/**
	 * Returns true if the given concept is embedded
	 * @param descriptor
	 * @return
	 */
	public static boolean isEmbedded( IDescriptor descriptor ){
		return Boolean.valueOf( Attributes.Embedded.name() );		
	}

	/**
	 * Sets the embedded flag of the given descriptor to true
	 * @param descriptor
	 * @returnn
	 */
	public static void setEmbedded( IDescriptor descriptor ){
		descriptor.set( Attributes.Embedded.name(), Boolean.TRUE.toString() );		
	}

}