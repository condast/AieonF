package org.aieonf.template.factory;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IPersistModelFactory;
import org.aieonf.model.ModelException;
import org.aieonf.template.ITemplate;

public abstract class AbstractPersistModelFactory<T extends IModelLeaf<? extends IDescriptor>> implements IPersistModelFactory<T>
{
	public static final String S_ERR_MODEL_NOT_FOUND = "The fixed model was not found at the given location.";
	public static final String S_ERR_TEMPLATE_NOT_FOUND = "The template for this model was not provided.";
	public static final String S_ERR_CANNOT_CREATE_MODEL = "A fixed model cannot be created or extended.";
	
	private Persistence persistence;
	private ITemplateFactory factory;
	
	private T model;
	 
	/**
	 * Persist the 
	 * @param model
	 * @param persistence
	 */
	protected AbstractPersistModelFactory( ITemplateFactory factory, Persistence persistence ){
		this.persistence = persistence;
		this.factory = factory;
	}
	
	/**
	 * Get the persistence
	 * @return
	 */
	public final Persistence getPersistence(){
		return this.persistence;
	}
	
	/**
	 * Create a model from the given template
	 * @param template
	 * @return
	 * @throws ModelException
	*/
	protected abstract T createModel( ITemplate template ) throws ModelException;
	
	@Override
	public void create() throws ModelException
	{
		this.factory.create();
		ITemplate template = this.factory.getTemplate();
		if( this.persistence.equals( Persistence.Fixed ))
			throw new ModelException( S_ERR_CANNOT_CREATE_MODEL );
		if( template == null )
			throw new NullPointerException( S_ERR_TEMPLATE_NOT_FOUND );
		this.model = createModel( template );
	}

	@Override
	public T getModel() throws ModelException
	{
		return model;
	}
}
