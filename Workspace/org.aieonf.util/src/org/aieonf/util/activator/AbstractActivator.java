/**
 * 
 */
package org.aieonf.util.activator;

/**
 * @author keesp
 *
 */
public abstract class AbstractActivator implements IActivator
{

	private Status status;

	/**
	 * 
	 */
	public AbstractActivator()
	{
		this.status = Status.Idle;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IActivator#getStatus()
	 */
	@Override
	public Status getStatus()
	{
		return this.status;
	}

	/**
	 * Set the activator to idel
	 */
	protected void clear(){
		this.status = Status.Idle;
	}

	/**
	 * Supportive method gives true if the status is set to idle
	 * @return
	*/
	@Override
	public boolean isIdle()
	{
		return ( this.status.equals( Status.Idle ));		
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IActivator#isAvailable()
	 */
	@Override
	public boolean isAvailable()
	{
		return ( this.status.equals( Status.Available ));
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IActivator#isAvailable()
	 */
	@Override
	public boolean isActive()
	{
		return ( this.status.equals( Status.Active ));
	}

	/**
	 * Make the activator available
 */
	protected void setAvailable(){
		this.status = Status.Available;
	}

	/**
	 * Start activating. Returns true if the activation is possible, false
	 * if not, for instance when the activator is already started
	 */
	@Override
	public boolean start(){
		if( !this.status.equals( Status.Idle ))
			return false;
		this.status = Status.Activating;
		this.activate();
		this.status = Status.Active;
		return true;
	}

	/**
	 * Stop activating
	 */
	@Override
	public void stop(){
		if( this.status.equals( Status.Idle ))
			return;
		status = Status.ShuttingDown;
		this.deactivate();
	}
	
	/**
	 * Activate the activator
	*/
	protected void activate(){
		this.status = Status.Available;
	}
	
	/**
	 * Shut the activator down
	 */
	protected abstract void deactivate();

}
