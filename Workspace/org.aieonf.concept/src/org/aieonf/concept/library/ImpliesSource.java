package org.aieonf.concept.library;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.implicit.AttributeImplies;

public class ImpliesSource<T extends IDescriptor> extends
		AttributeImplies<T, T> {

	public ImpliesSource(T reference) {
		super(reference, IConcept.Attributes.SOURCE.name() );
	}
}
