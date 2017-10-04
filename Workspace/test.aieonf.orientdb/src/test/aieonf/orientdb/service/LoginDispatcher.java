package test.aieonf.orientdb.service;

import org.aieonf.commons.security.LoginEvent;

public class LoginDispatcher {

	private static LoginDispatcher dispatcher = new LoginDispatcher();
	
	private LoginProvider provider;
	
	private LoginDispatcher() {
	}

	public static LoginDispatcher getInstance(){
		return dispatcher;
	}
		
	public void setLoginProvider( LoginProvider provider) {
		this.provider = provider;
	}
	
	public void setLoginEvent( LoginEvent event ){
		this.provider.notifyLoginEvent(event);
	}

}
