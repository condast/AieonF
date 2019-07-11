package org.aieonf.osgi.authentication;

import java.util.Collection;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

public interface ILoginProvider {

	public void addAuthenticationListener( IAuthenticationListener listener );

	public void removeAuthenticationListener( IAuthenticationListener listener );

	/**
	 * Returns true if a login user is registered
	 * @return
	 */
	public boolean isRegistered( long loginId );

	/**
	 * Returns true if a login user is logged in
	 * @return
	 */
	public boolean isLoggedIn( long loginId );

	/**
	 * Get the login user. Requires a valid token to use 
	 * @return
	 */
	public ILoginUser getLoginUser( long loginId, long token );

	/**
	 * Get the user names for the given user ids
	 * @param userIds
	 * @return
	 */
	public Map<Long, String> getUserNames( Collection<Long> userIds);
	
	/**
	 * Request to initiate a log off sequence
	 */
	void logoutRequest(); 

	/**
	 * Log off the given user
	 * @param loginId
	 */
	void logout(long loginId, long token);

	CallbackHandler createCallbackHandler();
}
