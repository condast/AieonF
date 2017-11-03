package org.aieonf.template.core;

import java.io.IOException;
import java.io.InputStream;

import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.model.core.ModelException;
import org.aieonf.model.persistence.IPersistModel.Persistence;
import org.aieonf.template.def.ITemplate;
import org.aieonf.template.factory.ITemplateFactory;

public abstract class AbstractPersistTemplateFactory<T extends ITemplate> implements ITemplateFactory
{
	public static final String S_ERR_TEMPLATE_NOT_FOUND = "The template was not found at the given location.";
	public static final String S_ERR_CANNOT_CREATE_TEMPLATE = "A template cannot be created or extended.";
	
	private IPersistence<T> persistence;
	private T template;
	
	/**
	 * Persist the 
	 * @param model
	 * @param persistence
	 */
	protected AbstractPersistTemplateFactory( IPersistence<T> persistence ){
		this.persistence = persistence;
		this.template = null;
	}

	/**
	 * Persist the 
	 * @param model
	 * @param persistence
	 */
	protected AbstractPersistTemplateFactory( T template, IPersistence<T> persistence ){
		this.persistence = persistence;
		this.template = template;
	}
	
	/**
	 * Get hte inputstream that points to the source of the template
	 * @return
	 */
	@Override
	public abstract InputStream getInputStream();
	
	/**
	 * Get the persistence
	 * @return
	 */
	public IPersistence<T> getPersistence(){
		return this.persistence;
	}

	/**
	 * Get the template for the given model
	 */
	@Override
	public T getTemplate(){
		return this.template;
	}

	@Override
	public ITemplate create() throws ModelException
	{
		if( this.persistence.equals( Persistence.Fixed ))
			throw new ModelException( S_ERR_CANNOT_CREATE_TEMPLATE );
		try {
			this.template = this.persistence.read( this.getInputStream() );
			return template;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new ModelException( e );
		}
	}

	@Override
	public boolean templateExists()
	{
		return ( this.template != null );
	}
}
