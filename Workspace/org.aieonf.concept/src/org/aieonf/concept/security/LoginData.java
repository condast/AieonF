package org.aieonf.concept.security;

public class LoginData
{
	private String loginName;

	
	public LoginData()
	{
		super();
		this.loginName = null;
	}

	public LoginData( String loginName )
	{
		super();
		this.loginName = loginName;
	}

	public boolean isLoggedIn()
	{
		return loginName != null;
	}

	public String getLoginName()
	{
		return this.loginName;
	}
}