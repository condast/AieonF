package org.aieonf.template.databinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.activator.AbstractActivator;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.validation.AbstractValidationDatabinding;
import org.aieonf.commons.validation.IValidator;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.template.property.TemplateProperty;

public abstract class AbstractTemplateDescriptorDatabinding<T,U extends Object> extends AbstractActivator
{
	private IValidator<T,U> validator;
	private IDescriptor descriptor;
	private Map<String, AbstractValidationDatabinding<T,U>> dbs;
	
	public AbstractTemplateDescriptorDatabinding( IValidator<T,U> validator, IDescriptor descriptor ){
		this.validator = validator;
		this.descriptor = descriptor;
		this.addAttributeValidator( this.descriptor );
		dbs = new HashMap<String, AbstractValidationDatabinding<T,U>>();
	}

	/* (non-Javadoc)
	 * @see org.condast.util.activator.AbstractActivator#activate()
	 */
	@Override
	protected void activate()
	{
		for( AbstractValidationDatabinding<T,U> vdb: this.dbs.values() )
			vdb.start();
		super.activate();
	}


	/* (non-Javadoc)
	 * @see org.condast.util.activator.AbstractActivator#deactivate()
	 */
	@Override
	protected void deactivate()
	{
		for( AbstractValidationDatabinding<T,U> vdb: this.dbs.values() )
			vdb.stop();
	}


	/**
	 * Get the data binding, or null if there is none
	 * @param attribute
	 * @return
	 */
	public AbstractValidationDatabinding<T,U> getDataBinding( String attribute ){
		return dbs.get( attribute );
	}

	
	/**
	 * @return the validator
	 */
	public final IValidator<T,U> getValidator()
	{
		return validator;
	}

	/**
	 * get a valid attribute data binding from the given template descriptor and attribute. 
	 * @param td
	 * @return
	 */
	protected abstract AbstractValidationDatabinding<T,U> getDataBinding(TemplateProperty<?,?,?> ta );

	/**
	 * Create a descriptor from the given template descriptor
	 * @param td
	 * @return
	 * @throws ConceptException
	 * @throws ParseException
	 */
	protected void addAttributeValidator( IDescriptor td ){
		Iterator<String> iterator = td.iterator();
		String key;
		TemplateProperty<?,?,?> ta;
		AbstractValidationDatabinding<T,U> listener;
		
		while( iterator.hasNext() ){
			key = iterator.next();
			ta = null;//td.getAttribute( key );
			if( !this.validator.getReference().equals( ta ))
				continue;
			listener = this.getDataBinding( ta );
			if( listener == null )
				continue;
			validator.addValidationListener( listener );
			dbs.put(key, listener);
		}
	}
}