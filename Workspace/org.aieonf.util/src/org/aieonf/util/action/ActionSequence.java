package org.aieonf.util.action;

//J2SE
import java.util.*;

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
public class ActionSequence<T>
  implements IAction<T>
{

  /**
   * A list of action sequences
  */
  private List<IAction<T>> sequence;

  //If true, the sequence is stopped if an action does not allow a next
  private boolean breakNext;

  public ActionSequence()
  {
    sequence = new ArrayList<IAction<T>>();
  }

  /**
   * Add an action
   * @param action IAction
  */
  public void addAction( IAction<T> action )
  {
    this.sequence.add( action );
  }

  /**
   * Remove an action
   * @param action IAction
  */
  public void removeAction( IAction<?> action )
  {
    this.sequence.remove( action );
  }

  /**
   * If true, the sequence will break if an action does not allow a next one
   * @return boolean
  */
  public boolean isBreakNext()
  {
    return breakNext;
  }

  /**
   * If true, the sequence will break if an action does not allow a next one.
   * Otherwise the full sequence is performed
   *
   * @param breakNext boolean
   */
  public void setBreakNext( boolean breakNext )
  {
    this.breakNext = breakNext;
  }

  /**
   * Perform an action on the given object. Returns true if the action was
   * performed successfully
   *
   * @param obj Object
   * @return boolean
   * @throws Exception
   */
  @Override
	public boolean doAction( T obj ) throws Exception
  {
    IAction<T> action;
    boolean result = true;
    for( int i = 0; i < this.sequence.size(); i++ ){
      action = this.sequence.get( i );
      result &= action.doAction( obj );
      if( this.breakNext == false )
        continue;
      if( action.allowNext() == false )
        return result;
    }
    return result;
  }

  /**
   * An optional flag to indicate that a next action is permitted.
   * This is used by that action sequence
   * @return boolean
   */
  @Override
	public boolean allowNext()
  {
    return true;
  }

	@Override
	public void activate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public org.aieonf.util.action.IAction.Status getStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(org.aieonf.util.action.IAction.Status status)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasStatus(org.aieonf.util.action.IAction.Status status)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStatusMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
