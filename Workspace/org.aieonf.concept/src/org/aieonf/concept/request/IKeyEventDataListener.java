package org.aieonf.concept.request;

public interface IKeyEventDataListener<R,D> {
	
	public void notifyKeyEventProcessed( DataProcessedEvent<R,D> event );
}
