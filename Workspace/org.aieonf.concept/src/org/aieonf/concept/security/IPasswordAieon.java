package org.aieonf.concept.security;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;

public interface IPasswordAieon extends IDescriptor, IImplicit<IDescriptor>
{

	//The supported attributes
	public enum Attributes
	{
		USER_NAME,
		PASSWORD,
		CONFIRMATION,
		EMAIL,
		REGISTER,
		LOGIN,
		URL;
		
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
	 */
	public abstract void setUserName(String userName);

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
	 * Get the email address
	 * @return String
	 */
	public String getEmail();

	/**
	 * Set the email address
	 * @param email String
	 * @
	*/
	void setEmail(String email);

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