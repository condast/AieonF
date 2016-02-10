package org.aieonf.template.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.aieonf.concept.core.Descriptor;

public class BindingDescriptor extends Descriptor implements PropertyChangeListener{

	private static final long serialVersionUID = 1L;

	public BindingDescriptor() {
		super( new BindingConceptBase());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		PropertyChangeListener listener = (PropertyChangeListener) super.getBase();
		listener.propertyChange(evt);
	}	
}
