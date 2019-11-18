package org.aieonf.sketch.core;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.selection.IActiveDomainProvider;

public class Dispatcher implements IActiveDomainProvider{

	private static Dispatcher service = new Dispatcher();

	private IActiveDomainProvider provider;

	private Dispatcher() {}

	public static Dispatcher getInstance(){
		return service;
	}

	@Override
	public boolean isRegistered(long id, String name) {
		return provider.isRegistered(id, name);
	}

	
	@Override
	public IDomainAieon getDomain(long id, String name) {
		return provider.getDomain(id, name);
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

}
