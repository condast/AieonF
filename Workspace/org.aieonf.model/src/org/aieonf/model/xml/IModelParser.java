package org.aieonf.model.xml;

import java.util.Collection;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;

public interface IModelParser<T extends IDescriptor, M extends IDescribable<T>> {
	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelBuilderListener(IModelBuilderListener<M> listener);

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelBuilderListener( IModelBuilderListener<M> listener );
	
	/**
	 * Parse the given model	
	 * @param model
	 * @return true if all went well.
	 */
	//public boolean parseModel( IModelLeaf<T> model ) throws ModelException;

	/**
	 * Get the model that builds the application
	 * @return
	 */
	public Collection<M> getModels();

}
