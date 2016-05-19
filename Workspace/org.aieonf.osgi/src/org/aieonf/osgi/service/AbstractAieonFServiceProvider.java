package org.aieonf.osgi.service;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.selection.ISelectionBean;

public abstract class AbstractAieonFServiceProvider<T extends Object> implements IAieonFService<T>{

	private IDomainAieon domain;
	private Collection<ISelectionBean> selections;
	
	
	protected AbstractAieonFServiceProvider() {
		selections = new ArrayList<ISelectionBean>();
	}

	public void activate(){ /* NOTHING*/ };
	public void deactivate(){ /* NOTHING*/ }

	
	public synchronized IDomainAieon getDomain() {
		return domain;
	}

	@Override
	public ISelectionBean[] getSelectionBeans() {
		return selections.toArray( new ISelectionBean[selections.size()]);
	}

	@Override
	public void setActiveDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	public void unsetActiveDomain(IDomainAieon domain) {
		this.domain = null;
	}
}
