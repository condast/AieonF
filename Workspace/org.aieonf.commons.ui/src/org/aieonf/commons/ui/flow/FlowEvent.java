package org.aieonf.commons.ui.flow;

import java.util.EventObject;

import org.aieonf.commons.ui.flow.IFlowControl.FlowEvents;

public class FlowEvent<T> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private FlowEvents flowEvent;
	
	public int index;

	public FlowEvent(Object source, FlowEvents flowEvent ) {
		this( source, flowEvent, 0 );
	}

	public FlowEvent(Object source, FlowEvents flowEvent, int index ) {
		super(source);
		this.flowEvent = flowEvent;
		this.index = index;
	}

	public FlowEvents getFlowEvent() {
		return flowEvent;
	}

	public int getIndex() {
		return index;
	}
}
