package org.aieonf.util.parser;

import java.util.EventObject;

public class ParseEvent<T extends Object> extends EventObject
{
	private static final long serialVersionUID = 2113739661852090208L;

	private T data;
	
	public ParseEvent( Object source, T data )
	{
		super( source );
	}

	public T getData() {
		return data;
	}
	
	

}
