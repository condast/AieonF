package org.aieonf.util.validation;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.util.activator.AbstractActivator;

public abstract class AbstractValidationDatabinding<T,U extends Object> extends AbstractActivator implements
		IValidationListener<T, U>
{
	private T attribute;
	private U value;;
	
	private Collection<IValidator<T, U>> validators;
	Collection<IValidationListener<T,U>> listeners;
	
	public AbstractValidationDatabinding( T attribute )
	{
		this.attribute = attribute;
		this.validators = new ArrayList<IValidator<T, U>>();
		listeners = new ArrayList<IValidationListener<T,U>>();
	}

	public void addValidator( IValidator<T, U> validator ){
		this.validators.add( validator );
		validator.addValidationListener(this);
	}

	public void removeValidator( IValidator<T, U> validator ){
		this.validators.remove( validator );
		validator.removeValidationListener(this);
	}

	public void addValidationListener( IValidationListener<T,U> listener ){
		this.listeners.add( listener );
	}

	public void removeValidationListener( IValidationListener<T,U> listener ){
		this.listeners.remove( listener );
	}

	/**
	 * @return the attribute
	 */
	public final T getAttribute()
	{
		return attribute;
	}

	/**
	 * @return the validators
	 */
	public final Collection<IValidator<T, U>> getValidators()
	{
		return validators;
	}

	/**
	 * Get th last correct value that was validated
	 * @return
	 */
	public U getLastCorrectValue() {
		return value;
	}

	/**
	 * @return the valid
	 */
	public final boolean isValid()
	{
		for( IValidator<T,U> validator: validators )
			if( !validator.lastResult() )
				return false;
		return true;
	}

	/**
	 * Response to correct validation
	 */
	protected abstract void onValidationCorrect( ValidationEvent<T, U> event );

	/**
	 * Response when the validation is incorrect
	 */
	protected abstract void onValidationIncorrect( ValidationEvent<T,U> event );

	@Override
	public void notifyValidationResult(ValidationEvent<T,U> event)
	{
		if(( event.getKey() == null) || ( !event.getKey().equals( this.attribute )))
			return;
		if( event.isCorrect() ){
			this.onValidationCorrect( event );
			this.value = event.getValue();
		}
		else
			this.onValidationIncorrect( event );
		for( IValidationListener<T,U> listener: this.listeners )
			listener.notifyValidationResult(event);
	}
}
