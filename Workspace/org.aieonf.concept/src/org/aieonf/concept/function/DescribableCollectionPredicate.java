package org.aieonf.concept.function;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescribable;

public class DescribableCollectionPredicate<D extends IDescribable> implements IDescribablePredicate<D> {

	private Collection<IDescribablePredicate<D>> predicates;
	
	public DescribableCollectionPredicate() {
		this.predicates = new ArrayList<>();
	}

	public void addPredicate( IDescribablePredicate<D> predicate ) {
		this.predicates.add(predicate);
	}

	public void removePredicate( IDescribablePredicate<D> predicate ) {
		this.predicates.remove(predicate);
	}

	@Override
	public boolean test( D t) {
		for( IDescribablePredicate<D> predicate: this.predicates) {
			if( predicate.test(t))
				return true;
		}
		return false;
	}
	
	@Override
	public int compare(D o1, D o2) {
		Compare compare = Compare.BOTH_INCORRECT; 
		for( IDescribablePredicate<D> predicate: this.predicates) {
			compare = Compare.get( predicate.compare(o1, o2));
			switch( compare ) {
			case SAME:
			case REFERENCE_OK:
				return compare.getIndex();
			default:
				break;
			}
		}
		return compare.getIndex();
	}

}