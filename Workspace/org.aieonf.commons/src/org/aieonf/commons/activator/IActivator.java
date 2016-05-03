package org.aieonf.commons.activator;

public interface IActivator
{
	public static final String S_ACTIVATOR = "Activator";
	
	public enum Status{
		Idle,
		Activating,
		Active,
		Available,
		ShuttingDown
	}

	/**
	 * Start activating. Returns true if the activation is possible, false
	 * if not, for instance when the activator is already started
	 */
	public boolean start();
	
	/**
	 * Stop activating
	 */
	public void stop();	

	/**
	 * Get the status of the activator
	 * @return
	*/
	public Status getStatus();
	
	/**
	 * Supportive method gives true if the status is set to idle
	 * @return
	 */
	public boolean isIdle();

	/**
	 * Supportive method gives true if the status is set to available
	 * @return
	 */
	public boolean isAvailable();

	/**
	 * Supportive method gives true if the status is set to active
	 * @return
	 */
	public boolean isActive();

}