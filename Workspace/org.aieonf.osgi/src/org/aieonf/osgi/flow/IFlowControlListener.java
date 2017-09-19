package org.aieonf.osgi.flow;

public interface IFlowControlListener<T extends Object> {

	/**
	 * Respond to changes in the flow
	 * @param event
	 */
	public void notifyEventChanged( FlowEvent<T> event );
}
