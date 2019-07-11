package org.aieonf.osgi.domain;

import java.util.Map;

import org.aieonf.concept.domain.IDomainAieon;

public interface IDomainProvider {

	/**
	 * Provides the domain with the given name, if the userName and password are
	 * correct, or if the domain is public
	 * @param userName
	 * @param password
	 * @param domain
	 * @return
	 */
	public IDomainAieon getDomain( String userName, String password, String domain );

	/**
	 * Get the domain names,as id-name pair
	 * @param domain
	 * @return
	 */
	public Map<String, String> getDomains( );

	/**
	 * Get the (informal) domain names
	 * @return
	 */
	String[] getDomainNames();

}
