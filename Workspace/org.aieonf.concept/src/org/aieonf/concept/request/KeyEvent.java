package org.aieonf.concept.request;

import java.util.EventObject;
import java.util.Map;

public class KeyEvent<R> extends EventObject{
	private static final long serialVersionUID = -9124208561015096326L;
	
	private R request;
	private Map<String, String> params;

	public KeyEvent(Object arg0, R request, Map<String, String> params ){
		super( arg0 );
		this.request = request;
		this.params = params;
	}

	/**
	 * @return the request
	 */
	public final R getRequest(){
		return request;
	}

	public Map<String, String> getParams() {
		return params;
	}
}