package org.aieonf.concept.domain;

public interface IDomainSelection {

	public enum SelectionEvents{
		ADD_PROVIDER,
		SETUP_DOMAIN,
		SELECT_DOMAIN,
		REMOVE_PROVIDER
	}
	
	void addListener(IDomainListener listener);

	IDomainAieon[] getDomains();

	IDomainAieon getActiveDomain();

	void setActiveDomain( String domainId);

	void removeListener(IDomainListener listener);

}