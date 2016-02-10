package org.aieonf.model.processor;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelNode;
import org.aieonf.model.ModelException;

public interface IModelFactory<T extends IModelNode<? extends IDescriptor>>
{
	/**
	 * Handle a model node
	 * @param node
	 * @throws ModelException
	 */
	public void processModelNode( T node ) throws ModelException ;
}
