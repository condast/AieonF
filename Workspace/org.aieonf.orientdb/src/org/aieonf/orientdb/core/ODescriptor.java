package org.aieonf.orientdb.core;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;

public class ODescriptor extends Descriptor implements IDescriptor {
	private static final long serialVersionUID = -6443275870133892852L;

	public ODescriptor( String id ) {
		super( new OConceptBase( id ));
	}	
}
