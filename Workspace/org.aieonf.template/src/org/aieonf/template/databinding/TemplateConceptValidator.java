package org.aieonf.template.databinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.validation.IValidationListener;
import org.aieonf.commons.validation.ValidationEvent;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.parser.attr.TemplateAttributeValidator;
import org.aieonf.template.property.TemplateProperty;

public class TemplateConceptValidator
{
	private ITemplateLeaf<? extends IDescriptor> leaf;
	private Collection<IValidationListener<String, String>> listeners;
		
	public TemplateConceptValidator( ITemplateLeaf<? extends IDescriptor> leaf )
	{
		this.leaf = leaf;
		this.listeners = new ArrayList<IValidationListener<String, String>>();
	}

	public void addValidationListener( IValidationListener<String, String> listener ){
		this.listeners.add( listener );
	}

	public void removeValidationListener( IValidationListener<String, String> listener ){
		this.listeners.remove( listener );
	}

	protected void notifyValidationResult( ValidationEvent<String, String> event ){
		for( IValidationListener<String, String> listener: listeners)
			listener.notifyValidationResult(event);
	}
	
	/**
	 * Validate all the attribues of the given descriptor
	 * @param descriptor
	 * @return
	 */
	public boolean validateDescriptor( IDescriptor descriptor ){
		TemplateAttributeValidator tav;
		boolean retVal = true;
		String key = null, value = null;
		ValidationEvent<String, String> event;
/*
		for( TemplateProperty tda: tDesc.getTemplateDescriptorAttributes() ){
			tav = new TemplateAttributeValidator( tda );
			key = tda.getKey();
			value = descriptor.get( key );
			if( Descriptor.isNull(value ))
				value =  descriptor.getFromExtendedKey( tda.getKey() );
			retVal &= tav.validate( value ).isCorrect();
			if( retVal )
				continue;
			String msg = descriptor.toString() + ": " + tda.getKey() + " is false: " + value;
			event = new ValidationEvent<String, String>( this, tda.getKey(), tda.getValue(), msg );
			this.notifyValidationResult(event);
			return false;
		}
		*/
		event = new ValidationEvent<String, String>( this, true );
		this.notifyValidationResult(event);
		return retVal;
	}

	/**
	 * Validate the attributes provided in the given keystring of the given descriptor.
	 * @param descriptor
	 * @param keys
	 * @return
	 * @throws ParseException
	 */
	public boolean validateDescriptor( IDescriptor descriptor, Enum<?>[] keys ) throws ParseException{		
		List<Enum<?>>keyCol = Arrays.asList(keys);
		Collection<TemplateProperty<?,?>> attrs = new ArrayList<TemplateProperty<?,?>>();
		
		/* 
		 for( TemplateProperty<?,?,?> tda: leaf.attributes(keys[0])){
		 
			if( !tda.getKey().equals( keyCol.get(0) ))
				continue;
			attrs.add(tda);
			keyCol.remove(0);
		}
		*/

		TemplateAttributeValidator tav;
		boolean retVal = true;
		String value = null;
		ValidationEvent<String, String> event;
		for( TemplateProperty<?,?> tda: attrs ){
			tav = new TemplateAttributeValidator( tda );
			value = descriptor.get( tda.getKey() );
			if( Descriptor.assertNull(value ))
				value =  null;//descriptor.getFromExtendedKey( tda.getKey() );
			retVal &= tav.validate( value ).isCorrect();
			if( retVal )
				continue;
			String msg = descriptor.toString() + ": " + tda.getKey() + " is false: " + value;
			event = null;//new ValidationEvent<String, String>( this, tda.getKey(), tda.getValue(), msg );
			this.notifyValidationResult(event);
			return false;
		}
		event = new ValidationEvent<String, String>( this, true );
		this.notifyValidationResult(event);
		return retVal;
	}
}