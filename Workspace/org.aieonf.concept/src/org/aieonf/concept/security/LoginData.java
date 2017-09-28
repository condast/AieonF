package org.aieonf.concept.security;

public class LoginData
{
	private String loginName;
	private String password;

	public LoginData()
	{
		super();
		this.loginName = null;
		this.password = null;
	}

	public LoginData( String loginName, String password )
	{
		super();
		this.loginName = loginName;
		this.password = password;
	}

	public boolean isLoggedIn()
	{
		return loginName != null;
	}

	public String getLoginName()
	{
		return this.loginName;
	}

	public synchronized String getPassword() {
		return password;
	}
}