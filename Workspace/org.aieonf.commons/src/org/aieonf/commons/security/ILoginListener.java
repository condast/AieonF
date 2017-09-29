package org.aieonf.commons.security;

public interface ILoginListener {

	public enum LoginEvents{
		REGISTER,
		LOGIN,
		LOGOFF
	}
	
	public void notifyLoginEvent( LoginEvent event );
}
