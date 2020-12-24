package org.aieonf.commons.ui.persistence;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.aieonf.commons.persistence.ISessionStoreFactory;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.UISession;

public abstract class AbstractSessionPersistence<S extends Object> implements ISessionStoreFactory<HttpSession,S>{

	public static final int DEFAULT_HOURS = 4;
	
	private Map<HttpSession, S> sessions;
	
	private ScheduledExecutorService executor;

	protected AbstractSessionPersistence( ) {
		this( DEFAULT_HOURS, TimeUnit.HOURS );
	}
	
	protected AbstractSessionPersistence( int amount, TimeUnit tu) {
		sessions = new HashMap<>();
		executor = Executors.newScheduledThreadPool(5);
		executor.schedule(() -> onScheduledCleanup(), amount, tu );
	}
		
	@Override
	public S createSessionStore(HttpSession session) {
		UISession uiSession = RWT.getUISession();
		S result=  null;
		if( !sessions.containsKey(session)) {
			result = createPersistence(session);
			sessions.put(session, result);
			session.setMaxInactiveInterval( (int) Duration.ofDays(1).getSeconds() );
			uiSession.addUISessionListener(e->updateSession(session));
		}else {
			result = sessions.get(session);
		}
		return result;
	}

	protected abstract S createPersistence( HttpSession session );
	
	protected int getSessionCount() {
		return sessions.size();
	}
	
	public HttpSession getSession() {
		HttpSession session = RWT.getUISession().getHttpSession();
		createPersistence(session);
		return session;
	}
	
	protected boolean isMaxTimeInactive( HttpSession session) {
		return ( System.currentTimeMillis() - session.getLastAccessedTime()) > session.getMaxInactiveInterval();
	}
	
	private void updateSession( HttpSession session) {
		if( isMaxTimeInactive(session) )
			sessions.remove(session);
	}
	
	private void onScheduledCleanup() {
		for( HttpSession session: sessions.keySet()) {
			updateSession(session);
		}
	}
}
