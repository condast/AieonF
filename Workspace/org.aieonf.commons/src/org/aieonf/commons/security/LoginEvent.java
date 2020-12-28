package org.aieonf.commons.security;

import java.util.EventObject;

import org.aieonf.commons.security.ILoginListener.LoginEvents;

public class LoginEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	
	private LoginEvents loginEvent;
	
	private ILoginUser user;
	
	private String userName;
	private String password;
	
	private boolean loggedIn;

	public LoginEvent( Object source, LoginEvents loginEvent, ILoginUser user ){
		this( source, loginEvent, user, null, null );
	}

	public LoginEvent( Object source, LoginEvents loginEvent, String userName, String password ){
		this( source, loginEvent, null, userName, password);
	}
	
	public LoginEvent( Object source, LoginEvents loginEvent, ILoginUser user, String userName, String password ){
		super( source );
		this.loginEvent = loginEvent;
		this.user = user;
		this.userName = userName;
		this.password = password;
		this.loggedIn = false;
	}

	public synchronized LoginEvents getLoginEvent() {
		return loginEvent;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
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