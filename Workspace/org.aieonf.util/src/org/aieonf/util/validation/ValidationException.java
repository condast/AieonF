/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.util.validation;

public class ValidationException extends Exception
{
	/**
	 * For serialization purposes 
	*/
	private static final long serialVersionUID = 387135501309063550L;

/**
   * CONSTRUCTOR: Create an exception with the given message
   *
   * @param msg String
  */
  public ValidationException( String msg )
  {
    super( msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param cause Exception
  */
  public ValidationException( Exception cause )
  {
    super( cause );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param msg String
   * @param cause Exception
  */
  public ValidationException( String msg, Exception cause )
  {
    super( msg, cause );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param location String
   * @param msg String
  */
  public ValidationException( String location, String msg )
  {
    super( "[" + location + "]: " + msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param location String
   * @param msg String
   * @param cause Exception
  */
  public ValidationException( String location, String msg, Exception cause )
  {
    super( "[" + location + "]: " + msg, cause );
  }
}
