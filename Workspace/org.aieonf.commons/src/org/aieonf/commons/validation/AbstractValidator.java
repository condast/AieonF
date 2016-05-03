package org.aieonf.commons.validation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractValidator<T,U extends Object> implements IValidator<T, U>
{
	public static final String S_ERR_NULL_VALUE = "The given value may not be null for attribute: ";
	
	private T reference;
	
	private boolean lastResult;
	
	private Collection<IValidationListener<T,U>> listeners;
	
	protected AbstractValidator( T reference )
	{
		this.reference = reference;
		this.listeners = new ArrayList<IValidationListener<T,U>>();
		this.lastResult = false;
	}

	/**
	 * @return the reference
	 */
	@Override
	public T getReference()
	{
		return reference;
	}

	/**
	 * Returns true if the validator contains the given reference
	 * @param reference
	 * @return
	*/
	@Override
	public boolean hasReference( T reference ){
		if( reference == null )
			return false;
		if( this.reference == null )
			return false;
		return this.reference.equals( reference );
	}

	@Override
	public boolean lastResult() {
		return this.lastResult;
	}

	/**
	 * The actual validation method that must be implemented
	 * @param reference
	 * @param object
	 * @return
	 */
	protected abstract ValidationEvent<T,U> performValidation(U object );
	
	/* (non-Javadoc)
	 * @see org.condast.concept.validation.IValidator#addValidationListener(org.condast.concept.validation.IValidationListener)
	 */
	@Override
	public void addValidationListener(IValidationListener<T,U> listener)
	{
		this.listeners.add( listener );
		
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.validation.IValidator#removeValidationListener(org.condast.concept.validation.IValidationListener)
	 */
	@Override
	public void removeValidationListener(IValidationListener<T,U> listener)
	{
		this.listeners.remove( listener );
	}
	
	protected void notifyListeners( ValidationEvent<T,U> event ){
		for( IValidationListener<T,U> listener: listeners )
			listener.notifyValidationResult(  event );
	}

	@Override
	public final ValidationEvent<T,U> validate(U object )
	{
		this.lastResult = false;
		ValidationEvent<T,U> result = this.performValidation( object);
		this.notifyListeners( result);
		this.lastResult = result.isCorrect();
		return result;
	}
}
