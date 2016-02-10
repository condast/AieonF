package org.aieonf.concept.security;

import org.aieonf.concept.core.ConceptException;

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
public interface IBaseAccessTransaction
{
	//Error messages
	public static final String S_ERR_NO_USERNAME = 
		"No user name provided ";
	public static final String S_ERR_NO_PASSWORD = 
		"No password provided";
	public static final String S_ERR_INVALID_USERNAME = 
		"The provided user name is invalid: ";
	public static final String S_ERR_INVALID_PASSWORD = 
		"The provided password is invalid";
	public static final String S_ERR_ALREADY_OPEN = 
		"A personal database is already open. Close this first ";

	/**
	 * Supported requests
	 * @author Kees Pieters
	*/
	public enum Requests
	{
		Login,
		Logoff,
		Register,
		IsLoggedIn,
		UpdatePassword
	}

	/**
   * Close the database.
  */
  public void close();

  /**
   * Returns true if the transaction is opened
   * @return boolean
  */
  public boolean isOpen();

	/**
	 * Get the user name of the opened personal database, or null if it is closed
	 * @return
	 * @throws ConceptException
	 */
	public String getUserName() throws ConceptException;
}
