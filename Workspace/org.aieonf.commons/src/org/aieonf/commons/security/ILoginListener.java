package org.aieonf.commons.security;

public interface ILoginListener {

	public enum LoginEvents{
		REGISTER,
		LOGIN,
		LOGOFF,
		NOT_FOUND,
	}
	
	public void notifyLoginEvent( LoginEvent event );
}
