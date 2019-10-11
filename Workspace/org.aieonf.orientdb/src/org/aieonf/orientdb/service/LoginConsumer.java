package org.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginProvider;
import org.aieonf.orientdb.core.Dispatcher;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class LoginConsumer {

	private Dispatcher dispatcher = Dispatcher.getInstance();
	private ILoginProvider provider;
	
	public LoginConsumer() {}

	@Reference
	public synchronized void setProvider(ILoginProvider provider) {
		this.provider = provider;
		this.provider.addLoginListener(dispatcher);
	}

	public synchronized void unsetProvider(ILoginProvider prvider) {
		this.provider.removeLoginListener(dispatcher);
		provider = null;
	}
}
