package org.aieonf.orientdb.service;

import org.aieonf.concept.security.ILoginProvider;
import org.aieonf.concept.security.LoginData;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class LoginConsumer {

	private static ILoginProvider provider;
	
	public LoginConsumer() {}

	public synchronized ILoginProvider getProvider() {
		return provider;
	}

	@Reference
	public synchronized void setProvider(ILoginProvider prvider) {
		provider = prvider;
	}

	public synchronized void unsetProvider(ILoginProvider prvider) {
		provider = null;
	}

	public static LoginData getLoginData(){
		if( provider == null )
			return new LoginData();
		else
			return provider.getLoginData();
	}
}
