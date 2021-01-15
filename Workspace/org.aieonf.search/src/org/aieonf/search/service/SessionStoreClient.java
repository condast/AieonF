package org.aieonf.search.service;

import javax.servlet.http.HttpSession;
import org.aieonf.commons.persistence.ISessionStoreFactory;
import org.condast.saight.selection.core.Dispatcher;
import org.condast.saight.swt.persistence.SessionStore;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class SessionStoreClient {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	public SessionStoreClient() {}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public synchronized void setProvider(ISessionStoreFactory<HttpSession, SessionStore>  provider) {
		dispatcher.setSessionStore(provider);
	}

	public synchronized void unsetProvider(ISessionStoreFactory<HttpSession, SessionStore> provider) {
		dispatcher.removeSessionStore( null);
	}
}
