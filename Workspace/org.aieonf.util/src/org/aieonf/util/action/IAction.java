package org.aieonf.util.action;

/**
 * <p>Title: Util</p>
 *
 * <p>Description: This package consists of classes that are used~nto implement
 * a run time environment, objetc ids and so on.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public interface IAction<T>
{
  /**
   * The status that
   */
  public static enum Status{
    STATUS_ERROR,
    STATUS_IDLE,
    STATUS_INIT,
    STATUS_ACTIVATING,
    STATUS_ACTIVE,
    STATUS_DEACTIVATING
  }

  /**
   * Activate the action
  */
  public void activate();

  /**
   * Deactivate the action
   */
  public void deactivate();

  /**
   * Get the status of the action
   * @return int
  */
  public IAction.Status getStatus();

  /**
   * Set the status of this action
   * @param status IAction.Status
  */
  public void setStatus( IAction.Status status );

  /**
   * Returns true if the status of the action equals the given one
   * @param status IAction.Status
   * @return boolean
   */
  public boolean hasStatus( IAction.Status status );

  /**
   * Get an optional message that accompanies the status
   * @return String
   */
  public String getStatusMessage();
  
  /**
   * Perform an action on the given object. Returns true if the action was
   * performed successfully
   *
   * @param obj Object
   * @return boolean
   * @throws Exception
  */
  public boolean doAction( T obj ) throws Exception;

  /**
   * An optional flag to indicate that a next action is permitted.
   * This is used by that action sequence
   * @return boolean
  */
  public boolean allowNext();
}
