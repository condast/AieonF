package org.aieonf.osgi.selection;

import org.aieonf.concept.domain.IDomainAieon;

public interface IActiveDomainProvider {

	public boolean isRegistered( long id, String name );
	
	public IDomainAieon getActiveDomain();
	
}
