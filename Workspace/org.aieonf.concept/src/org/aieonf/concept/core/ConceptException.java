/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

import org.aieonf.concept.IDescriptor;

public class ConceptException extends Exception
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 2387725761424088097L;
	//Supported messages
  public static final String S_NOT_SUPPORTED_MSG =
    " is not Supported in this Eon";
  public static final String S_TRANSFORMATION_FAILED_MSG =
    " Could not transform this Eon. Requires a default constructor!";

  /**
   * CONSTRUCTOR: Create an exception with the given concept name
   *
   * @param descriptor IDescriptor
  */
  public ConceptException( IDescriptor descriptor )
  {
    super( "[" + descriptor.getName() + "]: Concept Exception" );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param msg String
  */
  public ConceptException( String msg )
  {
    super( msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param name String
   * @param msg String
  */
  public ConceptException( String name, String msg )
  {
    super( "[" + name + "]: " + msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param name String
   * @param msg String
   * @param ex Exception
  */
  public ConceptException( String name, String msg, Exception ex )
  {
    super( "[" + name + "]: " + msg, ex );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param msg String
   * @param ex Exception
  */
  public ConceptException( String msg, Exception ex )
  {
    super( msg, ex );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param msg String
   * @param ex Exception
  */
  public ConceptException( Throwable ex )
  {
    super( ex );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param descriptor IDescriptor
   * @param msg String
  */
  public ConceptException( IDescriptor descriptor, String msg )
  {
    super( "[" + descriptor.getName() + "]: " + msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param descriptor IDescriptor
   * @param msg String
   * @param ex Exception
  */
  public ConceptException( IDescriptor descriptor, String msg, Exception ex)
  {
    super( "[" + descriptor.getName() + "]: " + msg, ex );
  }
}
