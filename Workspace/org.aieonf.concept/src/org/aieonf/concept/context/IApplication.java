package org.aieonf.concept.context;

import org.aieonf.concept.IDescribable;

public interface IApplication extends IDescribable<IContextAieon>
{
	/**
	 * Create the application
	*/
	public void create() throws Exception;

	/**
	 * Initialse the application
	 * @throws Exception
	*/
	public void initialise() throws Exception;

	/**
	 * Shut the context down
	 * @throws Exception
	*/
	public void shutdown() throws Exception;	
	
	/**
	 * Returns true if the context is starting up
	 * @return
	*/
	public boolean isStarting();
	
	/**
	 * Resets the startup flag. This can be used to control a decision when the
	 * startup of a context is completed. By default, this only done at a shutdown
	*/
	public void resetStartup();
	
}