package org.aieonf.concept.security;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.util.StringStyler;
import org.aieonf.util.implicit.IImplicit;

public interface IPasswordAieon extends IDescriptor, IImplicit<IDescriptor>
{

	//The supported attributes
	public enum Attributes
	{
		USER_NAME,
		PASSWORD,
		CONFIRMATION,
		REGISTER;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ){
			for( Attributes attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * Get the user name of the concept
	 *
	 * @return String
	 */
	public abstract String getUserName();

	/**
	 * Set the user name
	 *
	 * @param userName String
	 * @throws ConceptException
	 */
	public abstract void setUserName(String userName) throws ConceptException;

	/**
	 * Get the password of the concept
	 *
	 * @return String
	 */
	public abstract String getPassword();

	/**
	 * Set the password
	 *
	 * @param password String
	 */
	public abstract void setPassword(String password);

	/**
	 * Get the confirmation of the concept
	 *
	 * @return String
	 */
	public abstract String getConfirmation();

	/**
	 * Set the confirmation of the password
	 *
	 * @param password String
	 */
	public abstract void setConfirmation(String password);

	/**
	 * If true, the loaded collection is registering
	 * @return
	 */
	public abstract boolean isRegistering() ;

	/**
	 * Set the register value
	 * @param register
	 */
	public abstract void setRegister(boolean register);

	/**
	 * Returns true if the user is valid
	 *
	 * @param userName String
	 * @param password String
	 * @return boolean
	 */
	public abstract boolean isValidUser(String userName, String password);

}