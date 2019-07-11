package org.aieonf.orientdb.service;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.domain.IActiveDomainProvider;

public class DomainService implements IActiveDomainProvider{

	private Collection<IActiveDomainProvider> providers;
	
	private static DomainService service = new DomainService();
		
	private DomainService() {
		providers = new ArrayList<>();
	}
	
	public static DomainService getService() {
		return service;
	}

	public void addProvider( IActiveDomainProvider provider){
		this.providers.add( provider );
	}

	public void removeProvider( IActiveDomainProvider provider){
		this.providers.remove( provider );
	}
	
	public boolean isValid( String domain, int token ) {
		return false;
	}

	@Override
	public IDomainAieon getSelected(String userName, String password) {
		for( IActiveDomainProvider provider: providers ) {
			IDomainAieon domain = provider.getSelected(userName, password);
			if( domain != null )
				return domain;
		}
		return null;
	}
}
