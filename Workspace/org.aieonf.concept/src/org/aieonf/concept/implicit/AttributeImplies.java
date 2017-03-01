package org.aieonf.concept.implicit;

import org.aieonf.commons.Utils;
import org.aieonf.commons.implicit.IImplicit.Conditions;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;

public class AttributeImplies<T extends IDescriptor, U extends IDescriptor>
		extends AbstractImplies<T, U> {

	private String key;

	protected AttributeImplies(T reference ) {
		this(reference, reference.getName());
	}

	protected AttributeImplies(T reference, String key ) {
		super(reference, Conditions.ON_ATTRIBUTE);
		this.key = key;
	}

	protected AttributeImplies(T reference, Enum<?> key ) {
		this(reference, ConceptBase.getAttributeKey(key));
	}

	@Override
	protected int compareOnAttribute(U obj) {
		String reference = super.getReference().get( key );
		String compare = obj.get( key );
		if( Utils.assertNull( reference ) && Utils.assertNull( compare ))
			return 0;
		if( !Utils.assertNull( reference ) && Utils.assertNull( compare ))
			return 1;
		if( Utils.assertNull( reference ) && !Utils.assertNull( compare ))
			return -1;		
		return reference.compareTo( compare );
	}
}