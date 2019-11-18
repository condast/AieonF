package org.aieonf.osgi.selection;

import org.aieonf.concept.domain.IDomainAieon;

public interface IActiveDomainProvider {

	public boolean isRegistered( long id, String name );
	
	public IDomainAieon getActiveDomain();

	/**
	 * Get the domain with the given id and domain name. Returns null
	 * if the domain doesn't exist
	 * @param id
	 * @param name
	 * @return
	 */
	public IDomainAieon getDomain(long id, String name);

	/**
	 * Get the domains that are provided 
	 * @return
	 */
	public IDomainAieon[] getDomains();	
}
