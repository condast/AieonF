package org.aieonf.util.filter;

//Condast
import org.aieonf.util.action.*;

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
public abstract class FilteredAction<T> implements IAction<T>
{
  //Supported error messages
  public static final String S_ERR_INVALID_OBJECT =
    "The provided object is not valid for this operation";

  //The filter that performs the selection
  private AbstractFilter<T> filter;

  //If true, only the accept operation is performed. a reject will be ignored
  private boolean acceptOnly;

  //Is implemented from IAction, and is false on a reject action. Default true
  private boolean allowNext;

  /**
   * Create the filter action
   * @param filter Filter
  */
  public FilteredAction( AbstractFilter<T> filter )
  {
    super();
    this.filter = filter;
    this.acceptOnly = false;
    this.allowNext = true;
  }

  /**
   * Create the filter action. If acceptOnly is true, the action upon rejection
   * is omitted
   * @param filter Filter
   * @param acceptOnly boolan
  */
  public FilteredAction( AbstractFilter<T> filter, boolean acceptOnly )
  {
    super();
    this.filter = filter;
    this.acceptOnly = acceptOnly;
    this.allowNext = true;
  }

  /**
   * Perform the action that is required when the filter
   * accepts the given object
   *
   * @param obj Object
   * @return boolean
   * @throws Exception
  */
  protected abstract boolean onAccept( T obj ) throws Exception;

  /**
   * Perform the action that is required when the filter
   * rejects the given object
   *
   * @param obj Object
   * @return boolean
   * @throws Exception
  */
  protected abstract boolean onReject( T obj ) throws Exception;

  /**
   * Perfprm the filtered action
   * @param obj Object
   * @return boolean
   * @throws Exception
  */
  @Override
	public boolean doAction( T obj ) throws Exception
  {
    T t = obj;
  	if( filter.accept( t ))
      return this.onAccept( t );

    if( this.acceptOnly == false ){
      this.allowNext = false;
      return this.onReject( t );
    }
    return true;
  }

  /**
   * An optional flag to indicate that a next action is permitted.
   * This is used by that action sequence
   * @return boolean
  */
  @Override
	public boolean allowNext()
  {
    return this.allowNext;
  }

  /**
   * If true, only the accept operation is performed. a reject will be ignored
   *
   * @return boolean
  */
  public boolean isAcceptOnly()
  {
    return acceptOnly;
  }

  /**
   * Set acceptOnly.
   * If true, only the accept operation is performed. a reject will be ignored
   *
   * @param acceptOnly boolean
  */
  public void setAcceptOnly( boolean acceptOnly )
  {
    this.acceptOnly = acceptOnly;
  }
}
