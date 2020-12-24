package org.aieonf.commons.ui.persistence;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.UISession;

public abstract class AbstractSessionPersistence<S extends Object> {

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
	
	protected abstract S createPersistence( HttpSession session );
	
	protected int getSessionCount() {
		return sessions.size();
	}
	
	public HttpSession getSession() {
		UISession uiSession = RWT.getUISession();
		HttpSession session = RWT.getUISession().getHttpSession();
		if( !sessions.containsKey(session)) {
			sessions.put(session, createPersistence(session));
			session.setMaxInactiveInterval( (int) Duration.ofDays(1).getSeconds() );
			uiSession.addUISessionListener(e->updateSession(session));
		}
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
