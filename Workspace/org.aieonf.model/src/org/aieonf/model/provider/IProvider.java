package org.aieonf.model.provider;

import org.aieonf.model.core.IModelListener;

/**
 * A provider can return an object U, if the correct model is provided. This
 * allows for a transformation of a model to a concrete class
 * @author Kees
 *
 * @param <U>
 */
public interface IProvider<U extends Object> {

	/**
	 * Get a unique identifier for this model provider
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Returns true if the function with the given name is provided. 
	 * @param function
	 * @return
	 */
	public boolean hasFunction( String function ); 
	
	public void addListener( IModelListener<U> listener );

	public void removeListener( IModelListener<U> listener );
	
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

}
