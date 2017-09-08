package org.aieonf.template.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.aieonf.commons.parser.IParser;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.validation.IValidationListener;
import org.aieonf.commons.validation.ValidationEvent;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;
import org.aieonf.model.xml.StoreModel;
import org.aieonf.template.ITemplate;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.template.builder.ITemplateModelBuilder;
import org.aieonf.template.databinding.TemplateConceptValidator;
import org.aieonf.template.factory.ITemplateFactory;
import org.aieonf.template.parser.ReadableTemplateParser;

public class ModelController<T extends IModelLeaf<? extends IDescriptor>> implements IValidationListener<String, String>
{
	private ITemplateModelBuilder builder;
	private ITemplateFactory factory;
	
	private boolean modelCreated;
	private Collection<IValidationListener<String, String>> listeners;
	
	private Logger logger;

	protected ModelController( )
	{
		logger = Logger.getLogger( this.getClass().getName() );
		this.modelCreated = false;
		this.listeners = new ArrayList<IValidationListener<String, String>>();
	}

	public ModelController( ITemplateModelBuilder builder, ITemplateFactory factory )
	{
		this();
		this.builder = builder;
		this.factory = factory;
		this.listeners = new ArrayList<IValidationListener<String, String>>();
	}

	public void addValidationListener( IValidationListener<String, String> listener ){
		this.listeners.add( listener );
	}

	public void removeValidationListener( IValidationListener<String, String> listener ){
		this.listeners.remove( listener );
	}

	/**
	 * @return the builder
	 */
	public ITemplateModelBuilder getBuilder()
	{
		return builder;
	}

	/**
	 * @return the factory
	 */
	protected final ITemplateFactory getFactory()
	{
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	protected final void setFactory(
			ITemplateFactory factory)
	{
		this.factory = factory;
	}

	/**
	 * @param builder the builder to set
	 */
	protected final void setBuilder(ITemplateModelBuilder builder)
	{
		this.builder = builder;
	}

	/**
	 * @return the modelCreated
	 */
	public boolean isModelCreated()
	{
		return modelCreated;
	}

	/**
	 * Create a model 
	 * @throws ModelException
	 * @throws ParseException 
	 * @throws ConceptException 
	 */
	public void createModel() throws ModelException, ConceptException, ParseException {
		IParser<ITemplateLeaf<? extends IDescriptor>> parser = new ReadableTemplateParser();
		parser.addListener( builder );
		try {
			factory.create();
		} catch (ModelException e) {
			e.printStackTrace();
			throw e;
		}
		finally{
			parser.removeListener( builder );
		}
		ITemplate template = factory.getTemplate();
		builder.assembleModel( template );
		logger.info("Model found:\n" + StoreModel.printModel( builder.getModel(), true ));					
		this.modelCreated = true;
	}
		
	protected boolean verifyConcept( IConcept concept )
	{
		if( !this.modelCreated )
			return false;

		TemplateConceptValidator tcv;
		boolean result = true;
		tcv = builder.getValidator( concept );
		tcv.addValidationListener(this);
		result = tcv.validateDescriptor(concept);
		tcv.removeValidationListener(this);
		logger.info("Concept: " + concept.getName() + " validated: " + result );
		return result;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.template.parser.attr.IValidationListener#notifyValidation(org.condast.concept.template.parser.attr.ValidationEvent)
	 */
	@Override
	public void notifyValidationResult(ValidationEvent<String, String> event)
	{
		for( IValidationListener<String, String> listener: listeners)
			listener.notifyValidationResult(event);
	}


	/**
	 * Remove the template directives "{}" and "[]" if they are present
	 * @param concept
	 */
	protected static void removeTemplateDirectives( IConcept concept ){
		Iterator<String> iterator = concept.iterator();
		String key, value;
		while( iterator.hasNext() ){
			key = iterator.next();
			value = concept.get(key);
			if( Descriptor.isNull(value ))
				continue;
			if( value.startsWith("{") && value.endsWith("}"))
				concept.set(key, null);
			if( value.startsWith("[") && value.endsWith("]"))
				concept.set(key, null);
		}
	}
}