package org.aieonf.model.security;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.security.IBaseAccessTransaction;
import org.aieonf.model.IModelNode;

public interface IAccessTransactionModel<T extends IDescriptor> extends IBaseAccessTransaction
{
  /**
   * Registers an application with the database using the given model.
   * 
   * @param accessModel
   * @return
   * @throws SecurityException
   */
	public boolean register( IModelNode<T> accessModel ) throws SecurityException;

  /**
   * Login using the given model.
   *
   * @param accessModel
   * @return
   * @throws SecurityException
  */
  public boolean login( IModelNode<T> accessModel ) throws SecurityException;

  /**
   * Log off using the given model.
   *
   * @param accessModel
   * @return
   * @throws SecurityException
  */
  public boolean logoff( IModelNode<T> accessModel ) throws SecurityException;

	/**
	 * Update the password of the personal database
	*/
	public boolean updatePassword( IModelNode<T> accessModel )throws SecurityException; 

}
