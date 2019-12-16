package org.aieonf.concept.domain;

import org.aieonf.commons.security.ILoginUser;

public interface IDomainSelection {

	public enum SelectionEvents{
		ADD_PROVIDER,
		SELECT_DOMAIN,
		REMOVE_PROVIDER
	}
	
	void addListener(IDomainListener listener);

	IDomainAieon[] getDomains();

	IDomainAieon getActiveDomain();

	void setActiveDomain(ILoginUser user, String domainId);

	void removeListener(IDomainListener listener);

}