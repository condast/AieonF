package org.aieonf.concept.security;

public interface ILoginProvider {

	/**
	 * A login provider presents a login data object
	 * @return
	 */
	public LoginData getLoginData();
}
