package test.aieonf.orientdb.service;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginProvider;
import org.osgi.service.component.annotations.Component;

import test.aieonf.orientdb.core.Dispatcher;

@Component
public class LoginProvider implements ILoginProvider {

	private Dispatcher dispatcher = Dispatcher.getInstance();
			
	public LoginProvider() {
	}

	@Override
	public void addLoginListener(ILoginListener listener) {
		dispatcher.addLoginListener(listener);
	}

	@Override
	public void removeLoginListener(ILoginListener listener) {
		dispatcher.removeLoginListener(listener);
	}
}
