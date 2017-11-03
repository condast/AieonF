package org.aieonf.commons.validation;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationCollection<T extends Object> extends AbstractValidator<T> implements
		IValidator<String, T>
{
	public static final String S_VAL_ALL_VALIDATORS_CORRECT = "All the validators gave a correct result";
	
	private Collection<IValidator<String,T>> validators;
	
	public ValidationCollection( String reference )
	{
		super( reference );
		validators = new ArrayList<IValidator<String,T>>();
	}

	@Override
	protected ValidationEvent<String, T> performValidation(T object)
	{
		ValidationEvent<String,T> event;
		for( IValidator<String,T> validator: this.validators ){
			event = validator.validate(object);
			if( !event.isCorrect())
				return event;
		}
		event = new ValidationEvent<String,T>( this, super.getReference(), true );
		event.setMessage(S_VAL_ALL_VALIDATORS_CORRECT );
		return event;
	}
}
