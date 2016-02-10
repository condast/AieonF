package org.aieonf.concept.security;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public interface IAccessTransaction extends IBaseAccessTransaction
{
  /**
   * Open the database.
   *
   * @param userName String
   * @param password String
   * @return boolean
   * @throws SecurityException
  */
  public boolean open( String userName, String password ) throws SecurityException;

  /**
   * Registers an application with the database using the given key.
   *
   * @param userName String
   * @param password String
   * @return boolean
   * @throws SecurityException
  */
  public boolean register( String userName, String password ) throws SecurityException;
  
	/**
	 * Update the password of the personal database
	 * @param userName
	 * @param password
	*/
	public boolean updatePassword( String userName, String password) throws SecurityException;  
}
