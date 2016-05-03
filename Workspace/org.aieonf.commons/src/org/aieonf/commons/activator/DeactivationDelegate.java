/**
 * 
 */
package org.aieonf.commons.activator;

/**
 * @author keesp
 *
 */
public class DeactivationDelegate implements Runnable
{
	//The activator that is used
	private IActivator activator;
	
	/**
	 * Create the delegate
	 */
	public DeactivationDelegate( IActivator activator )
	{
		this.activator = activator;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		this.activator.stop();
	}

}
