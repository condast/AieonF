package org.aieonf.orientdb.service;

import java.util.Collection;
import java.util.TreeSet;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;

public class LoginDispatcher implements ILoginListener{

	private static LoginDispatcher dispatcher = new LoginDispatcher();
		
	private Collection<ILoginUser> users;
	
	private LoginDispatcher() {
		users = new TreeSet<>();
	}

	public static LoginDispatcher getInstance(){
		return dispatcher;
	}
	
	public ILoginUser getUser( long userId ) {
		for( ILoginUser user: users ) {
			if( user.getId() == userId )
				return user;
		}
		return null;
	}
	
	@Override
	public void notifyLoginEvent(LoginEvent event) {
		switch( event.getLoginEvent() ) {
		case LOGOFF:
			users.add(event.getUser());
			break;
		default:
			users.remove(event.getUser());
		break;
		}
	}
}
