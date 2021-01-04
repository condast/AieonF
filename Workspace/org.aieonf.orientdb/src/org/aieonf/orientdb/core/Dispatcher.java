package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.ISecureCall;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements IActiveDomainProvider, ILoginListener, ISecureCall{

	public static final String S_ERR_INVALID_CALL = "It is not allowed to have more than one secure call";
	
	private static Dispatcher service = new Dispatcher();

	private IActiveDomainProvider provider;

	private ISecureCall secureCall;
	
	private Collection<ILoginListener> listeners;

	private Map<Long, ILoginUser> users;

	private Dispatcher() {
		users = new TreeMap<>();
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

	public boolean isRegistered(long id, long token, String domain) {
		if(( this.provider == null ) || ( this.secureCall == null ))
			return false;
		if( !this.secureCall.isSecure(id, token, domain))
			return false;
		return provider.isRegistered(id, domain);
	}

	@Override
	public IDomainAieon getDomain(long id, long token, String name) {
		if( this.provider == null )
			return null;
		return provider.getDomain(id, token, name);
	}

	@Override
	public IDomainAieon getActiveDomain() {
		return provider.getActiveDomain();
	}

	@Override
	public IDomainAieon[] getDomains() {
		return provider.getDomains();
	}

	@Override
	public IDescribablePredicate<IDescriptor> getPredicates() {
		return provider.getPredicates();
	}
	
	public IActiveDomainProvider getProvider() {
		return provider;
	}

	public void setProvider(IActiveDomainProvider provider) {
		this.provider = provider;
	}
	
	public ISecureCall getSecureCall() {
		return secureCall;
	}

	public void setSecureCall(ISecureCall secureCall) {
		if( this.secureCall != null )
			throw new IllegalArgumentException( S_ERR_INVALID_CALL );
		this.secureCall = secureCall;
	}

	@Override
	public boolean isSecure(long id, long token, String domain) {
		return this.secureCall.isSecure(id, token, domain);
	}
	
	@Override
	public void notifyLoginEvent(LoginEvent event) {
		ILoginUser user = event.getUser();
		switch( event.getLoginEvent() ) {
		case LOGOFF:
			this.users.remove(user.getId());
			break;
		default:
			this.users.put(user.getId(), user);
			break;
		}
		notifyListeners(event);
	}
}
