package org.aieonf.util.builder;
/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
public class BuilderException extends RuntimeException
{

  /**
   * Needed for serialization
  */
  private static final long serialVersionUID = -6480346588304388451L;

	//Supported messages
  public static final String S_UNIQUE_NAME_MSG =
    " The filter name should be unique in this application";
  public static final String S_RULE_NOT_SUPPORTED_MSG =
    " The provided rule is not supported for this filter";
  public static final String S_CHAIN_RULE_NOT_SUPPORTED_MSG =
    " The provided chain rule is not supported for this filter chain: ";

  /**
   * CONSTRUCTOR: Create an exception with the given message
   *
   * @param msg String
  */
  public BuilderException( String msg )
  {
    super( msg );
  }

  /**
   * Create an exception with the given exception
   *
   * @param msg String
  */
  public BuilderException( Exception ex )
  {
    super( ex );
  }

  /**
   * CONSTRUCTOR: Create an exception for the given builder
   *
   * @param name String
   * @param rule String
   * @param msg String
  */
  public BuilderException( String name, String rule, String msg )
  {
    super( "[" + name + "] rule " + rule + " : "  + msg );
  }

  /**
   * CONSTRUCTOR: Create an exception for the given builder
   *
   * @param msg String
   * @param ex Exception
  */
  public BuilderException( String msg, Exception ex )
  {
    super( msg, ex );
  }
}
