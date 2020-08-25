package org.aieonf.concept.function;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.concept.IDescribable;

public class ImplicitPredicate<D extends IDescribable> implements IDescribablePredicate<D> {

	private D reference;
	
	public ImplicitPredicate( D reference ) {
		this.reference = reference;
	}

	protected D getDescribable() {
		return reference;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean test(D t) {
		if( !( reference instanceof IImplicit ))
			return false;
		IImplicit<D> implicit = (IImplicit<D>) reference.getDescriptor();
		return implicit.test(t);
	}

	@Override
	public int compare(D o1, D o2) {
		boolean test1 = test( o1 );
		boolean test2 = test( o2 );
		return (test1 && test2)?Compare.SAME.getIndex(): test1?Compare.REFERENCE_OK.getIndex(): 
			test2?Compare.ARGUMENT_OK.getIndex(): Compare.BOTH_INCORRECT.getIndex();
	}
}
