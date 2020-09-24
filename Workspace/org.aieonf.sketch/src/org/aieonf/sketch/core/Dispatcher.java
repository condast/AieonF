package org.aieonf.sketch.core;

import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.domain.IDomainListener;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements IActiveDomainProvider{

	private static Dispatcher service = new Dispatcher();

	private IActiveDomainProvider provider;

	private ISecureGenerator generator;

	private Dispatcher() {}

	public static Dispatcher getInstance(){
		return service;
	}

	public ISecureGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ISecureGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void addDomainListener(IDomainListener listener) {
		provider.addDomainListener(listener);
	}

	@Override
	public void removeDomainListener(IDomainListener listener) {
		provider.removeDomainListener(listener);
	}

	@Override
	public boolean isRegistered(long id, String name) {
		return provider.isRegistered(id, name);
	}
	
	@Override
	public IDomainAieon getDomain(long id, long token, String name) {
		return provider.getDomain(id, token, name);
	}

	@Override
	public IDomainAieon getActiveDomain() {
		return provider.getActiveDomain();
	}

	@Override
	public IDomainAieon[] getDomains() {
		return provider.getDomains();
	}

	public void setProvider(IActiveDomainProvider provider) {
		this.provider = provider;
	}

	/**
	 * Get the predicates that have been registered
	 * @return
	 */
	@Override
	public IDescribablePredicate<IDescriptor> getPredicates(){
		return provider.getPredicates();
	}	

}
