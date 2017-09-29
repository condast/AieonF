package org.aieonf.commons.security;

import java.util.EventObject;

import org.aieonf.commons.security.ILoginListener.LoginEvents;

public class LoginEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	
	private LoginEvents loginEvent;
	
	private String loginName;
	private String password;
	private boolean loggedIn;

	public LoginEvent( Object source){
		this( source, LoginEvents.LOGOFF, null, null );
	}

	public LoginEvent( Object source, LoginEvents loginEvent, String loginName, String password )
	{
		super( source );
		this.loginEvent = loginEvent;
		this.loginName = loginName;
		this.password = password;
		this.loggedIn = false;
	}

	public synchronized LoginEvents getLoginEvent() {
		return loginEvent;
	}

	public boolean isLoggedIn(){
		return this.loggedIn;
	}

	public synchronized void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginName(){
		return this.loginName;
	}

	public synchronized String getPassword() {
		return password;
	}
}