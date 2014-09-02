package org.aieonf.concept.domain;

import java.util.EventObject;

public class DomainEvent<T extends Object> extends EventObject
{
	/**
	 * A domain event always carries a service object (e.g. a bean)
	 * with specific data
	 */
	private T serviceObject;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6027145252228402119L;

	public DomainEvent(Object arg0, T serviceObject )
	{
		super(arg0);
		this.serviceObject = serviceObject;
	}
	
	/**
	 * Get the service object
	 * @return
	 */
	public T getServiceObject(){
		return serviceObject;
	}

}
