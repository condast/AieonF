package org.aieonf.util.action;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public abstract class AbstractAction<T>
  implements IAction<T>
{

  //The status of the action
  private IAction.Status status;

  private String statusMsg;

  public AbstractAction()
  {
    this.status = IAction.Status.STATUS_IDLE;
  }

  /**
   * Get hte status of this concept
   * @return int
   */
  @Override
	public final IAction.Status getStatus()
  {
    return this.status;
  }

  /**
   * Set the status of this action
   * @param status IAction.Status
   */
  @Override
	public final void setStatus( IAction.Status status )
  {
    this.status = status;
  }

  /**
   * Set an error status of this action
   *
   * @param msg String
  */
  protected final void setError( String msg )
  {
    this.status = IAction.Status.STATUS_ERROR;
    this.setStatusMessage( msg );
  }

  /**
   * Returns true if the given status matches the status of the action
   *
   * @param status IAction.Status
   * @return boolean
  */
  @Override
	public final boolean hasStatus( IAction.Status status )
  {
    return ( this.status == status );
  }

  /**
   * Get an optional message that accompanies the status
   *
   * @return String
  */
  @Override
	public String getStatusMessage()
  {
    return this.statusMsg;
  }

  /**
   * Set an optional message that accompanies the status
   *
   * @param msg String
  */
  public void setStatusMessage( String msg )
  {
    this.statusMsg = msg;
  }
}
