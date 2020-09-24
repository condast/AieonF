package org.aieonf.concept.request;

import java.util.EventObject;

public class DataProcessedEvent<R,D> extends EventObject{
	private static final long serialVersionUID = -9124208561015096326L;
	
	private KeyEvent<R> event;
	private D data;

	public DataProcessedEvent(Object arg0, KeyEvent<R> request, D data ){
		super( arg0 );
		this.event = request;
		this.data = data;
	}

	/**
	 * @return the request
	 */
	public final KeyEvent<R> getKeyEvent(){
		return event;
	}

	/**
	 * @return the request
	 */
	public final R getRequest(){
		return event.getRequest();
	}

	public D getData() {
		return data;
	}
}