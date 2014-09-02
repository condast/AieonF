package org.aieonf.concept.context;

public interface IAieonController extends Comparable<IAieonController>
{
	/**
	 * The requests that are supported by every controller
	 * @author Kees Pieters
	 *
	*/
	public enum Requests
	{
		Initialise,
		Activate,
		Stop,
		Finalise
	}
	
	/**
	 * Get the id of the controller
	 * @return
	 */
	public String getControllerID();
		
	/**
	 * If true, this controller is enabled
	 * @return
	*/
	public boolean isEnabled();

	/**
	 * Set whether the controller is enabled or not
	 * @param order int
	*/
	public void setEnabled( boolean enabled );
	
	
	/**
	 * Initialise the controller
	 * @throws Exception
	*/
	public void initialise() throws Exception;
	
	/**
	 * Finalise the controller
	 * @throws Exception
	*/
	public void finalise() throws Exception;
	
}
