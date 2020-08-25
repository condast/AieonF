package org.aieonf.concept.function;

import java.util.Comparator;
import java.util.function.Predicate;

import org.aieonf.concept.IDescribable;

public interface IDescribablePredicate<D extends IDescribable> extends Predicate<D>, Comparator<D>{

	public enum Compare{
		SAME(0),
		REFERENCE_OK(1),
		ARGUMENT_OK(-1),
		BOTH_INCORRECT(-2);
		
		private int index;
		
		private Compare( int index ) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
		
		public static Compare get( int index ) {
			for( Compare compare: values()) {
				if( compare.getIndex() == index )
					return compare;
			}
			return BOTH_INCORRECT;
		}
	}
}
