package org.aieonf.osgi.domain;

import org.aieonf.concept.domain.IDomainAieon;

public interface IActiveDomainProvider {

	/**
	 * Provides the active domain in the container
	 * correct, or if the domain is public
	 * @param userName
	 * @param password
	 * @return
	 */
	public IDomainAieon getSelected( String userName, String password );
}
