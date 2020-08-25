package org.aieonf.concept.function;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public abstract class AbstractDescribablePredicate<D extends IDescribable> extends ImplicitPredicate<D> {

	protected AbstractDescribablePredicate( D describable ) {
		super( describable );
	}

	protected abstract boolean onTest( IDescriptor reference, IDescriptor arg0 );
	
	@Override
	public boolean test(D t) {
		if( onTest( super.getDescribable().getDescriptor(), t.getDescriptor()))
			return true;
		return super.test(t);
	}
}
