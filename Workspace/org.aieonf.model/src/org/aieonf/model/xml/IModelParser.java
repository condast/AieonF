package org.aieonf.model.xml;

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
}
