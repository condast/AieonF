package org.aieonf.commons.validation;

import java.util.EventObject;

import org.aieonf.commons.strings.StringUtils;

public class ValidationEvent<T,U extends Object> extends EventObject
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6620168955993942939L;
	
	private static final String S_ERR_NULL = "The value for the given key is null: ";
	
	private T key;
	private U value;
	private String msg;
	private boolean correct;

	public ValidationEvent(Object arg0, boolean correct )
	{
		super(arg0);
		this.correct = correct;
	}

	public ValidationEvent(Object arg0, T key, U value )
	{
		this(arg0, key , value, false );
	}

	public ValidationEvent(Object arg0, T key, boolean correct )
	{
		super(arg0);
		this.key = key;
		this.correct = correct;
	}

	public ValidationEvent(Object arg0, T key, U value, String msg )
	{
		this(arg0, key , value );
		this.msg = msg;
	}

	public ValidationEvent(Object arg0, T key, U value, boolean correct )
	{
		super(arg0);
		this.key = key;
		this.value = value;
		this.correct = correct;
	}

	public ValidationEvent(Object arg0, T key, U value, String msg, boolean correct )
	{
		this( arg0, key, value, correct );
		this.msg = msg;
	}

	/**
	 * @return the key
	 */
	public T getKey()
	{
		return key;
	}

	
	/**
	 * @return the value
	 */
	public U getValue()
	{
		return value;
	}

	/**
	 * @return if the key-value pair is correct
	 */
	public boolean isCorrect()
	{
		return correct;
	}
	

	/**
	 * @return the correct
	 */
	public boolean isNull()
	{
		this.msg = S_ERR_NULL + this.key;
		return ( this.value == null );
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return msg;
	}

	
	/**
	 * @param msg the msg to set
	 */
	public final void setMessage(String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the message
	 */
	public String getExtendedMessage()
	{
		String val = this.getValue().toString();
		if( StringUtils.isEmpty(val ))
			val = "null";
		return msg + ": (" + this.getKey() + ", " + val + ")";
	}

}
