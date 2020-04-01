package org.aieonf.orientdb.service;

import org.aieonf.commons.security.ISecureCall;
import org.aieonf.orientdb.core.Dispatcher;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class SecureCallConsumer {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	public SecureCallConsumer() {}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public synchronized void setProvider(ISecureCall provider) {
		dispatcher.setSecureCall(provider);
	}

	public synchronized void unsetProvider(ISecureCall provider) {
	}
}
