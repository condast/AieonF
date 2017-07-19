package org.aieonf.model.validation;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelException;

public interface IModelValidator<T extends IDescriptor>
{
	/**
	 * Get the template for this validator
	 * @return
	*/
	public IModelNode<T> getTemplate();
	
	/**
	 * Add a validator listener
	 * @param listener
	 * @return
	 */
	public boolean addValidatorListener( IValidatorListener<T> listener);

	/**
	 * Remove a validator listener
	 * @param listener
	 * @return
	 */
	public boolean removeValidatorListener( IValidatorListener<T> listener);
	
	/**
	 * Process the model
	 * @param model
	 * @throws modelException
	*/
	public void processModel( IModelNode<T> model ) throws ModelException;
}
