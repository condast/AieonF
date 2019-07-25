package org.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.orientdb.cache.CacheDatabase;
import org.aieonf.orientdb.factory.OrientDBFactory;

public class LoginDispatcher implements ILoginListener{

	private OrientDBFactory factory = OrientDBFactory.getInstance();
	
	private static LoginDispatcher dispatcher = new LoginDispatcher();
	
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
		if( !LoginEvents.LOGOFF.equals( event.getLoginEvent() ))
			cache.connect(domain, event );
		else
			cache.disconnect();
	}
}
