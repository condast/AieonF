package org.aieonf.model.persistence;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;

public abstract class AbstractPersistModel<T extends IModelLeaf<U>, U extends IDescriptor> implements IPersistModel<T,U>
{
	public static final String S_ERR_MODEL_NOT_FOUND = "The fixed model was not found at the given location.";
	public static final String S_ERR_TEMPLATE_NOT_FOUND = "The template for this model was not provided.";
	public static final String S_ERR_CANNOT_CREATE_MODEL = "A fixed model cannot be created or extended.";
	
	private Persistence persistence;
	private T template;
	
	/**
	 * Persist the 
	 * @param model
	 * @param persistence
	 */
	protected AbstractPersistModel( Persistence persistence ){
		this.persistence = persistence;
		this.template = null;
	}

	/**
	 * Persist the 
	 * @param model
	 * @param persistence
	 */
	protected AbstractPersistModel( T template, Persistence persistence ){
		this.persistence = persistence;
		this.template = template;
	}
	
	/**
	 * Get the persistence
	 * @return
	 */
	public Persistence getPersistence(){
		return this.persistence;
	}

	/**
	 * Read a model from a certain source
	 * @return
	 * @throws ModelException
	 */
	public abstract T readModel() throws ModelException;
	
	/**
	 * Get the template for the given model
	 */
	protected T getTemplate(){
		return this.template;
	}

	/**
	 * Create a model from the given template
	 * @param template
	 * @throws ModelException
	*/
	protected void createModel( T template ) throws ModelException{}
	
	@Override
	public void create() throws ModelException
	{
		if( this.persistence.equals( Persistence.Fixed ))
			throw new ModelException( S_ERR_CANNOT_CREATE_MODEL );
		if( this.template == null )
			throw new NullPointerException( S_ERR_TEMPLATE_NOT_FOUND );
		createModel( this.template );
	}

	@Override
	public T getModel() throws ModelException
	{
		T model;
		switch( this.persistence ){
			case Fixed:
				if( !modelExists())
					throw new ModelException( S_ERR_MODEL_NOT_FOUND );
				model = this.readModel();
				break;
			default:
				if( !modelExists())
					this.create();
				model = this.readModel();
				break;
		}
		return model;
	}
}
