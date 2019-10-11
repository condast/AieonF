package org.aieonf.osgi.selection;

import org.aieonf.concept.domain.IDomainAieon;

public interface IActiveDomainProvider {

	public boolean isRegistered( String id, String name );
	
	public IDomainAieon getActiveDomain();
	
}
