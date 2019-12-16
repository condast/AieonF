package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements IActiveDomainProvider, ILoginListener{

	private static Dispatcher service = new Dispatcher();

	private IActiveDomainProvider provider;

	private ILoginUser user;
	
	private Collection<ILoginListener> listeners;

	private Dispatcher() {
		user = null;	
		listeners = new ArrayList<>();
	}

	public static Dispatcher getInstance(){
		return service;
	}

	public void addListener( ILoginListener listener ) {
		this.listeners.add(listener);
	}

	public void removeListener( ILoginListener listener ) {
		this.listeners.remove(listener);
	}
	
	protected void notifyListeners( LoginEvent event ) {
		for( ILoginListener listener: this.listeners )
			listener.notifyLoginEvent(event);
	}
	
	@Override
	public void addDomainListener(IDomainListener listener) {
		provider.addDomainListener(listener);
	}

	@Override
	public void removeDomainListener(IDomainListener listener) {
		provider.removeDomainListener(listener);
	}

	@Override
	public boolean isRegistered(long id, String name) {
		if( this.provider == null )
			return false;
		return provider.isRegistered(id, name);
	}

	@Override
	public IDomainAieon getDomain(long id, String name) {
		if( this.provider == null )
			return null;
		return provider.getDomain(id, name);
	}

	@Override
	public IDomainAieon getActiveDomain() {
		return provider.getActiveDomain();
	}

	@Override
	public IDomainAieon[] getDomains() {
		return provider.getDomains();
	}

	public boolean isAllowed( IModelLeaf<? extends IDescriptor> model ) {
		IConcept.Scope scope = model.getScope();
		if( Scope.PUBLIC.equals(scope))
			return true;
		return ( user != null );
	}
	
	public void setProvider(IActiveDomainProvider provider) {
		this.provider = provider;
	}
	
	public boolean isLoggedIn( long userId, long token ) {
		return user.isCorrect(userId, String.valueOf( token ));
	}
	
	public ILoginUser getUser() {
		return user;
	}
	
	@Override
	public void notifyLoginEvent(LoginEvent event) {
		switch( event.getLoginEvent() ) {
		case LOGOFF:
			user = null;
			break;
		default:
			user = event.getUser();
		break;
		}
		notifyListeners(event);
	}
}
