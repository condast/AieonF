package org.aieonf.concept.rest;

import java.util.EventObject;

public class KeyEvent<R,D> extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9124208561015096326L;
	
	private R request;
	private D data;

	public KeyEvent(Object arg0, R request )
	{
		this(arg0, request, null);
	}

	public KeyEvent(Object arg0, R request, D data )
	{
		super( arg0 );
		this.request = request;
		this.data = data;
	}

	/**
	 * @return the request
	 */
	public final R getRequest(){
		return request;
	}

	/**
	 * @return the petitionkey
	 */
	public final D getData(){
		return data;
	}

}