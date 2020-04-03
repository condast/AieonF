package org.aieonf.concept.rest;

import java.util.EventObject;
import java.util.Map;

public class KeyEvent<R,D> extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9124208561015096326L;
	
	private R request;
	private Map<String, String> params;
	private D data;

	public KeyEvent(Object arg0, R request, Map<String, String> params )
	{
		this(arg0, request, params, null);
	}

	public KeyEvent(Object arg0, R request, Map<String, String> params, D data )
	{
		super( arg0 );
		this.request = request;
		this.params = params;
		this.data = data;
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

	/**
	 * @return the petitionkey
	 */
	public final D getData(){
		return data;
	}

}