package org.aieonf.sketch.service;

import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.sketch.core.Dispatcher;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class SecureGeneratorConsumer {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	
	public SecureGeneratorConsumer() {}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public synchronized void setProvider(ISecureGenerator  provider) {
		dispatcher.setGenerator(provider);
	}

	public synchronized void unsetProvider(ISecureGenerator provider) {
		dispatcher.setGenerator(null);
	}
}
