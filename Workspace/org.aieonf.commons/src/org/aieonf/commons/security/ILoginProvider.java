package org.aieonf.commons.security;

public interface ILoginProvider {

	public void addLoginListener( ILoginListener listener );
	public void removeLoginListener( ILoginListener listener );

	/**
	 * A login provider presents a login data object
	 * @return
	 */
	public LoginEvent getLoginData();
}
