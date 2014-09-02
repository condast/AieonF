package org.aieonf.util.validation;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationCollection<T, U extends Object> extends AbstractValidator<T,U> implements
		IValidator<T, U>
{
	public static final String S_VAL_ALL_VALIDATORS_CORRECT = "All the validators gave a correct result";
	
	private Collection<IValidator<T,U>> validators;
	
	public ValidationCollection( T reference )
	{
		super( reference );
		validators = new ArrayList<IValidator<T,U>>();
	}

	@Override
	protected ValidationEvent<T, U> performValidation(U object)
	{
		ValidationEvent<T,U> event;
		for( IValidator<T,U> validator: this.validators ){
			event = validator.validate(object);
			if( !event.isCorrect())
				return event;
		}
		event = new ValidationEvent<T,U>( this, super.getReference(), true );
		event.setMessage(S_VAL_ALL_VALIDATORS_CORRECT );
		return event;
	}
}
