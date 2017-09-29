package org.aieonf.commons.security;

import java.util.EventObject;

import org.aieonf.commons.security.ILoginListener.LoginEvents;

public class LoginEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	
	private LoginEvents loginEvent;
	
	private String loginName;
	private String password;

	public LoginEvent( Object source)
	{
		super( source );
		this.loginEvent = LoginEvents.LOGOFF;
		this.loginName = null;
		this.password = null;
	}

	public LoginEvent( Object source, LoginEvents loginEvent, String loginName, String password )
	{
		super( source );
		this.loginEvent = loginEvent;
		this.loginName = loginName;
		this.password = password;
	}

	public synchronized LoginEvents getLoginEvent() {
		return loginEvent;
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