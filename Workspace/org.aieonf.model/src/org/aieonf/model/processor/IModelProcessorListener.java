package org.aieonf.model.processor;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelException;

public interface IModelProcessorListener<T extends IModelNode<? extends IDescriptor>>
{
	/**
	 * Handle a model node
	 * @param node
	 * @throws ModelException
	 */
	public void handleModelNode( T node ) throws ModelException ;
}
