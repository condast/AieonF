package org.aieonf.osgi.flow;

/**
 * Flow contol takes care of flows in UI, such as in a wizard. 
 * @author Kees
 *
 */
public interface IFlowControl {

	public enum FlowEvents{
		INIT,
		NEXT,
		PREVIOUS,
		COMPLETE;
	}
	
	public enum Direction{
		PROCEED,
		BACKTRACK
	}
	/**
	 * initialise the flow control prior to starting it
	 */
	public void init();
	
	/**
	 * Get the index that is currently selected
	 * @return
	 */
	public int getIndex();
	
	/**
	 * response to a 'next' event in the flow. Typically create a new index
	 * position. Returns a negative value if no next page is available, or when the 
	 * flow has completed. 
	 * @return int
	 */
	public int next();

	/**
	 * response to a 'previous' event in the flow. Typically create a new index
	 * position. Returns a negative value if no previous page is available, or when the 
	 * flow has completed. 
	 * @return int
	 */
	public int previous( );
	
	/**
	 * Get the direction of the flow. Will always 'PROCEEED',
	 * unless PREVIOUS is pressed 
	 * @return
	 */
	public Direction getDirection();

	/**
	 * Complete the flow. Handles the everything prior to stopping 
	 */
	public void complete();
	
	/**
	 * Returns true if the flow is completed
	 * @return
	 */
	public boolean isCompleted();

	/**
	 * sometimes the wizard needs to set the direction prior to
	 * calling the previous method, for instance when
	 * synchronising data
	 * @param direction
	 */
	public void setDirection(Direction direction);
}