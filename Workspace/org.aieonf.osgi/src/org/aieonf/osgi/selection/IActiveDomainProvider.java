package org.aieonf.osgi.selection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.concept.function.IDescribablePredicate;

public interface IActiveDomainProvider {

	public void addDomainListener( IDomainListener listener );

	public void removeDomainListener( IDomainListener listener );

	public boolean isRegistered( long id, String name );
	
	public IDomainAieon getActiveDomain();
	
	/**
	 * Get the predicates that have been registered
	 * @return
	 */
	public IDescribablePredicate<IDescriptor> getPredicates();

	/**
	 * Get the domain with the given id and domain name. Returns null
	 * if the domain doesn't exist
	 * @param id
	 * @param name
	 * @return
	 */
	public IDomainAieon getDomain(long id, long token, String name);

	/**
	 * Get the domains that are provided 
	 * @return
	 */
	public IDomainAieon[] getDomains();	
}
