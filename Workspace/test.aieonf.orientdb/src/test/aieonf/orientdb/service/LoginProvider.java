package test.aieonf.orientdb.service;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginProvider;
import org.aieonf.commons.security.LoginEvent;
import org.osgi.service.component.annotations.Component;

@Component
public class LoginProvider implements ILoginProvider {

	private LoginDispatcher dispatcher = LoginDispatcher.getInstance();
	
	private Collection<ILoginListener> listeners;
	
	private LoginEvent loginEvent;
	
	public LoginProvider() {
		this.listeners = new ArrayList<ILoginListener>();
		dispatcher.setLoginProvider(this);
	}

	@Override
	public LoginEvent getLoginData() {
		return loginEvent;
	}

	@Override
	public void addLoginListener(ILoginListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeLoginListener(ILoginListener listener) {
		this.listeners.remove(listener);
	}
	
	public void notifyLoginEvent( LoginEvent event ){
		this.loginEvent = event;
		for( ILoginListener listener: this.listeners )
			listener.notifyLoginEvent(event);
	}
}
