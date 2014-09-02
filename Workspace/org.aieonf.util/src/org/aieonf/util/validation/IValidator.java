package org.aieonf.util.validation;

public interface IValidator<T,U extends Object>
{
	/**
	 * Returns true if the last validation was correct
	 * @return
	 */
	public boolean lastResult();
	
	/**
	 * Get the reference that identifies this validator
	 * @return
	 */
	public T getReference();
	
	/**
	 * Returns true if the validator contains the given reference
	 * @param reference
	 * @return
	 */
	public boolean hasReference( T reference );
	
	/**
	 * Validate the given object
	 * @param object
	 * @return
	 */
	public ValidationEvent<T,U> validate(U object );
	
	/**
	 * Add a validation Listener
	 * @param listener
	 */
	public void addValidationListener( IValidationListener<T,U> listener );
	
	/**
	 * Remove a validation Listener
	 * @param listener
	 */
	public void removeValidationListener( IValidationListener<T,U> listener );

}
