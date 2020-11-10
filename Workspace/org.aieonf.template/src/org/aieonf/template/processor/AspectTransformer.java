package org.aieonf.template.processor;

import java.util.Iterator;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.constraints.Aspect;
import org.aieonf.model.constraints.IAspect;
import org.aieonf.model.constraints.IAspect.Attributes;

/**
 * The aspect transformer transforms a certain subcategory of concept attributes to an aspect and vice versa.
 * For instance, login and registration details are usually stored with org.condast.concept.db.security.IPasswordAieon.Attributes.
 * This transformer sees to it that only these attributes are coupled to an aspect. 
 * 
 * @author KP
 *
 */
public class AspectTransformer
{

	private String classifier;
	
	public AspectTransformer( String classifier )
	{
		this.classifier = classifier;
	}
	
	/**
	 * Fill the given descriptor with the aspect values
	 * @param descriptor
	 * @param aspect
	 * @throws ConceptException
	 */
	public void fillAieon( IDescriptor descriptor, IAspect aspect ) throws ConceptException{
		IAspect.Attributes[] attributes = IAspect.Attributes.values();
		String key, value;
		for( Attributes attribute: attributes ){
			key = attribute.name();
			value = aspect.get( key );
			this.fillAieon( descriptor, key, value );
		}
	}

	/**
	 * Fill the given descriptor with the attributes
	 * @param descriptor
	 * @param aspect
	 * @throws ConceptException
	 */
	public void fillAieon( IDescriptor descriptor, String attribute, String value ) throws ConceptException{
		if(( !Descriptor.assertNull( attribute )) && ( !Descriptor.assertNull( value )))
				descriptor.set( classifier + "."+ attribute, value);
	}

	/**
	 * Get the aspect belongin to the given descriptor 
	 * @param td
	 * @param descriptor
	 * @throws ConceptException
	 */
	public IAspect getAspect( IDescriptor descriptor) throws ConceptException{
		Iterator<String> iterator = descriptor.keySet();
		String key, value;
		IAspect aspect = new Aspect();
		while( iterator.hasNext() ){
			key = iterator.next();
			if( !key.startsWith( this.classifier ))
				continue;
			value = descriptor.get( key );
			key = key.replace(classifier + ".", "");
			aspect.set(key, value);
		}
		return aspect;
	}
}
