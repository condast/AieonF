package org.aieonf.commons.security;

import java.util.EventObject;

import org.aieonf.commons.security.ILoginListener.LoginEvents;
import org.condast.commons.authentication.user.ILoginUser;

public class LoginEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	
	private LoginEvents loginEvent;
	
	private ILoginUser user;
	private boolean loggedIn;

	public LoginEvent( Object source, LoginEvents loginEvent, ILoginUser user )
	{
		super( source );
		this.loginEvent = loginEvent;
		this.user = user;
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

	public ILoginUser getUser(){
		return this.user;
	}
}