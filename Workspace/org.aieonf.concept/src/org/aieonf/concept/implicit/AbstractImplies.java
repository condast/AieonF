package org.aieonf.concept.implicit;

import org.aieonf.concept.IDescriptor;

public abstract class AbstractImplies<T extends IDescriptor, U extends IDescriptor> implements Implies<T, U> {

	private Conditions condition;
	private T reference;
	
	protected AbstractImplies( T reference ) {
		this( reference, Conditions.EQUAL );
	}
	
	protected AbstractImplies( T reference, Conditions condition ) {
		this. condition = condition;
		this.reference = reference;
	}

	protected Conditions getCondition() {
		return condition;
	}

	
	protected T getReference() {
		return reference;
	}

	/**
	 * Perform the comparison, based on the attribute
	 * @param obj
	 * @return
	 */
	protected abstract int compareOnAttribute( U obj );
	
	@Override
	public int compareTo(U obj) {
		int retval = 0;
		switch( this.condition ){
		case ID:
			retval =( reference.equals(obj))? 0: this.reference.toString().compareTo( obj.toString() );
			break;
		case ON_ATTRIBUTE:
			retval = this.compareOnAttribute(obj);
			break; 
		default:
			retval = ( !reference.equals(obj)) ? 0: reference.compareTo( obj );
			break;
		}
		return retval;
	}

}
