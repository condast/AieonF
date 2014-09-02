package org.aieonf.model.function;

import java.util.EventObject;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.function.IModelCompleteListener.ParseEvents;

public class ModelCompleteEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private IModelLeaf<? extends IDescriptor> root;
	private ParseEvents parseEvent;
	
	public ModelCompleteEvent(Object source, ParseEvents parseEvent, IModelLeaf<? extends IDescriptor> root) {
		super(source);
		this.root = root;
		this.parseEvent = parseEvent;
	}
	
	public ParseEvents getParseEvent() {
		return parseEvent;
	}

	public IModelLeaf<? extends IDescriptor> getRoot() {
		return root;
	}
}
