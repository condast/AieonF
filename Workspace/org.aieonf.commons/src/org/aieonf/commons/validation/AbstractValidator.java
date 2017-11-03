package org.aieonf.commons.validation;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractValidator<T extends Object> implements IValidator<String, T>
{
	public static final String S_ERR_NULL_VALUE = "The given value may not be null for attribute: ";
	
	private String reference;
	
	private boolean lastResult;
	
	private Collection<IValidationListener<String,T>> listeners;
	
	protected AbstractValidator( String reference )
	{
		this.reference = reference;
		this.listeners = new ArrayList<IValidationListener<String,T>>();
		this.lastResult = false;
	}

	/**
	 * @return the reference
	 */
	@Override
	public String getReference()
	{
		return reference;
	}

	/**
	 * Returns true if the validator contains the given reference
	 * @param reference
	 * @return
	*/
	@Override
	public boolean hasReference( String reference ){
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
	protected abstract ValidationEvent<String, T> performValidation(T object );
	
	/* (non-Javadoc)
	 * @see org.condast.concept.validation.IValidator#addValidationListener(org.condast.concept.validation.IValidationListener)
	 */
	@Override
	public void addValidationListener(IValidationListener<String,T> listener)
	{
		this.listeners.add( listener );
		
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.validation.IValidator#removeValidationListener(org.condast.concept.validation.IValidationListener)
	 */
	@Override
	public void removeValidationListener(IValidationListener<String,T> listener)
	{
		this.listeners.remove( listener );
	}
	
	protected void notifyListeners( ValidationEvent<String,T> event ){
		for( IValidationListener<String, T> listener: listeners )
			listener.notifyValidationResult(  event );
	}

	@Override
	public final ValidationEvent<String,T> validate(T object )
	{
		this.lastResult = false;
		ValidationEvent<String, T> result = this.performValidation( object);
		this.notifyListeners( result);
		this.lastResult = result.isCorrect();
		return result;
	}
}
