package org.aieonf.template.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.template.ITemplateNode;
import org.aieonf.util.validation.IValidationListener;
import org.aieonf.util.validation.IValidator;
import org.aieonf.util.validation.ValidationEvent;

public class TemplateNodeValidator<T extends IDescriptor> implements IValidationListener<String, String>
{

	private Collection<IValidator<String, String>> validators;
	private Map<String, String> results;
	
	private ITemplateNode<T> node;
	
	public TemplateNodeValidator( ITemplateNode<T> node ){
		this.node = node;
		this.validators = new ArrayList<IValidator<String, String>>();
		this.results = new TreeMap<String ,String>();
	}
	
	public void addValidator( IValidator<String, String> validator ){
		this.validators.add( validator );
		validator.addValidationListener( this );
	}

	public void removeValidator( IValidator<String, String> validator ){
		this.validators.remove( validator );
		validator.removeValidationListener( this );
	}

	/**
	 * Perform a validation for all the attributes for which validators have been registered
	 * @param object
	 * @return
	 */
	public boolean performValidation()
	{
		Iterator<String> iterator = node.getDescriptor().iterator();
		String attribute;
		while( iterator.hasNext() ){
			attribute = iterator.next();
			for( IValidator<String,String> validator: validators ){
				if( validator.hasReference( attribute )){
						if( results.get(attribute ) == null )
							return false;
				}
			}
		}
		return true;
	}

	@Override
	public void notifyValidationResult( ValidationEvent<String, String> event )
	{
		if( event.isCorrect() )
			this.results.put(event.getKey(), event.getValue() );
	}
	
	/**
	 * Fill the given descriptor with the validated results. Only good results are entered
	 * @param descriptor
	 * @throws ConceptException
	 */
	public void fill( String classifier, IDescriptor descriptor ) throws ConceptException{
		AspectTransformer transformer = 
				new AspectTransformer( classifier );
		Set<Map.Entry<String, String>> entrySet = this.results.entrySet();
		Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
		Map.Entry<String, String> entry;
		while( iterator.hasNext() ){
			entry = iterator.next();
			transformer.fillAieon( descriptor, entry.getKey(), entry.getValue());
		}
	}
}
