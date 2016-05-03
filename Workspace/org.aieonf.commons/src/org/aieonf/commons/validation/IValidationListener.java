package org.aieonf.commons.validation;

public interface IValidationListener<T,U extends Object>
{

	/**
	 * Is called when a validation has been performed
	 * @param event
	 */
	public void notifyValidationResult( ValidationEvent<T,U> event );
}
