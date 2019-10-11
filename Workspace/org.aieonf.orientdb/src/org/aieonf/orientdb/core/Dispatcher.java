package org.aieonf.orientdb.core;

import org.aieonf.commons.security.ILoginListener;
import org.aieonf.commons.security.ILoginUser;
import org.aieonf.commons.security.LoginEvent;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IConcept.Scope;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements IActiveDomainProvider, ILoginListener{

	private static Dispatcher service = new Dispatcher();

	private IActiveDomainProvider provider;

	private ILoginUser user;

	private Dispatcher() {
		user = null;		
	}

	public static Dispatcher getInstance(){
		return service;
	}

	@Override
	public boolean isRegistered(String id, String name) {
		if( this.provider == null )
			return false;
		return provider.isRegistered(id, name);
	}

	@Override
	public IDomainAieon getActiveDomain() {
		return provider.getActiveDomain();
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
	}

}
