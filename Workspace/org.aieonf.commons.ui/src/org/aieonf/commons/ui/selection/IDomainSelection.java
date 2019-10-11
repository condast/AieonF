package org.aieonf.commons.ui.selection;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;

public interface IDomainSelection {

	void addlistener(IDomainListener listener);

	IDomainAieon[] getDomains();

	IDomainAieon getActiveDomain();

	void setActiveDomain(ILoginUser user, String domainId);

	void removeListener(IDomainListener listener);

}