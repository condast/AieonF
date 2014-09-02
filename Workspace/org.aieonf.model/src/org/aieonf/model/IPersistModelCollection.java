package org.aieonf.model;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.ModelException;

public interface IPersistModelCollection< T extends IDescriptor>
{
	/**
	 * Get the model
	 * @return
	 * @throws ModelException
	*/
	public Collection<IModelNode<T>> getModels() throws ModelException;

	/**
	 * returns true if the model exists at the provided resource location
	 * @return
	 */
	public boolean modelExists( String location );

	/**
	 * Create a model if it doesn't exist
	 * @param location String
	 * @return IModelNode<T>
	 * @throws ModelException
	 */
	public IModelNode<T> create( String location ) throws ModelException;

	/**
	 * Get the size of the collection
	 */
	public int size();
}