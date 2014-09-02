package org.aieonf.util.petition;

import java.util.EventObject;

public class KeyEvent<T,U extends Object> extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9124208561015096326L;
	
	private IPetition<T,U> petition;

	public KeyEvent(Object arg0, T key )
	{
		super(arg0);
		this.petition = new Petition<T,U>( key );
	}

	public KeyEvent(Object arg0, T key, U value )
	{
		super( arg0 );
		this.petition = new Petition<T,U>( key, value );
	}

	/**
	 * @return the petitionkey
	 */
	public final IPetition<T,U> getPetition()
	{
		return petition;
	}
}