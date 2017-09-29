package org.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.orientdb.cache.CacheDatabase;
import org.aieonf.orientdb.factory.OrientDBFactory;

public class LoginDispatcher implements ILoginListener{

	private OrientDBFactory factory = OrientDBFactory.getInstance();
	
	private static LoginDispatcher dispatcher;
	private CacheDatabase cache = CacheDatabase.getInstance();
	
	private LoginDispatcher() {
	}

	public static LoginDispatcher getInstance(){
		return dispatcher;
	}
	
	
	@Override
	public void notifyLoginEvent(LoginEvent event) {
		factory.createTemplate();
		IDomainAieon domain = factory.getDomain();
		switch( event.getLoginEvent()){
		case REGISTER:
			cache.connect(domain, event );
			break;
		case LOGIN:
			cache.connect(domain, event );
			break;
		default:
			break;
		}
		
	}

}
