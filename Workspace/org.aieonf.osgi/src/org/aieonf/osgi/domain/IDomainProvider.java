package org.aieonf.osgi.domain;

import org.aieonf.commons.security.ILoginUser;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;

public interface IDomainProvider {

	public IDomainAieon[] getDomains( ILoginUser user );

	/**
	 * Get the predicates that have been registered
	 * @return
	 */
	public IDescribablePredicate<IDescriptor> getPredicates();
}