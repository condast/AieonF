package org.aieonf.model.xml;

import org.aieonf.concept.IDescribable;
import org.aieonf.model.builder.IModelBuilderListener;

public interface IModelParser<M extends IDescribable> {
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
