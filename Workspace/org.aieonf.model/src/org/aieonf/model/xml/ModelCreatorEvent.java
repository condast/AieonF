package org.aieonf.model.xml;

import java.util.EventObject;

import org.aieonf.concept.IDescriptor;

public class ModelCreatorEvent extends EventObject {
	private static final long serialVersionUID = -3384930756374025217L;
	
	private IDescriptor descriptor;
	
	public ModelCreatorEvent(Object source, IDescriptor descriptor ) {
		super(source);
		this.descriptor = descriptor;
	}

	public IDescriptor getDescriptor() {
		return descriptor;
	}
}
