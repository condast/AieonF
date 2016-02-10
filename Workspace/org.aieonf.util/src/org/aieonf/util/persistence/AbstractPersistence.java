package org.aieonf.util.persistence;

import org.aieonf.util.persistence.IPersistence;

public abstract class AbstractPersistence<T extends Object> implements IPersistence<T>
{
	public static final String S_ERR_PERSISTENCE_NOT_OPEN = 
		"The persistence is not open";
	
	//if true persistence is opened
	private boolean open;
	
	/**
	 * Create persistence for concepts
	*/
	public AbstractPersistence()
	{
		this.open = false;
	}

	/**
	 * open the persistence 
	*/
	@Override
	public void open()
	{
		this.open = true;
	}
	
	/**
	 * returns true if persistence is open
	 * @return
	*/
	@Override
	public boolean isOpen()
	{
		return this.open;
	}
	
	/**
	 * Close the persistence 
	*/
	@Override
	public void close()
	{
		this.open = false;
	}
}
