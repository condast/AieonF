/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept.persist;

import org.aieonf.concept.IDescriptor;


public class ConceptPersistException extends DatabaseException
{
  /**
   * For serialisation 
  */
	private static final long serialVersionUID = 635451261885370963L;

	private IDescriptor descriptor;
	
	/**
   * Create an exception with the given message
   *
   * @param msg String
  */
  public ConceptPersistException( String msg )
  {
    super( msg );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given message
   *
    * @param cause Exception
  */
  public ConceptPersistException( Exception cause )
  {
    super( cause );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given message
   *
   * @param msg String
   * @param cause Exception
  */
  public ConceptPersistException( String msg, Exception cause )
  {
    super( msg, cause );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param location String
   * @param msg String
  */
  public ConceptPersistException( IDescriptor descriptor, String msg )
  {
    super( "[" + descriptor.toString() + "]: " + msg );
    this.descriptor = descriptor;
  }

  /**
   * Create an exception with the given location and message
   *
   * @param location String
   * @param msg String
   * @param cause Exception
  */
  public ConceptPersistException( String location, String msg, Exception cause )
  {
    super( location, "[" + location + "]: " + msg, cause );
  }

  /**
   * CONSTRUCTOR: Create an exception with the given location and message
   *
   * @param location String
   * @param msg String
   * @param cause Exception
  */
  public ConceptPersistException( IDescriptor descriptor, String msg, Exception cause )
  {
    super( descriptor.toString(), "[" + descriptor.toString() + "]: " + msg, cause );
    this.descriptor = descriptor;
  }
 
  /**
   * Get the descriptor
   * @return
   */
  public IDescriptor getDescriptor()
  {
  	return this.descriptor;
  }
}
