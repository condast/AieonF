package org.aieonf.model.validation;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelNode;
import org.aieonf.model.ModelException;

public interface IValidatorListener<T extends IDescriptor>
{
	/**
	 * handle the node that is being validated
	 * @param node
	 * @throws ModelException
	*/
	public void handleModelNode( IModelNode<T> node ) throws ModelException;
}
