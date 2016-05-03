/**
 * 
 */
package org.aieonf.commons.activator;

import java.util.concurrent.Callable;

/**
 * @author keesp
 *
 */
public class ActivationDelegate implements Callable<Boolean>
{
	//The activator that is used
	private IActivator activator;
	
	/**
	 * Create the delegate
	 */
	public ActivationDelegate( IActivator activator )
	{
		this.activator = activator;
	}

	/* Starts the activation sequence. If the activator is idle, then the
	 * sequence is started, else it wits for the activator to become available
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public Boolean call()
	{
		if( this.activator.isIdle() ){
			this.activator.start();
			return false;
		}
		while( !this.activator.isAvailable() ){
			try{
				Thread.sleep( 50 );
			}
			catch( InterruptedException ex ){
				return this.activator.isAvailable();
			}
		}
		return true;
	}

}
