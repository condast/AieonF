package org.aieonf.osgi.sketch.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.selection.ISelectionBean;
import org.aieonf.osgi.service.IAieonFService;
import org.aieonf.osgi.swt.IViewFactory;
import org.eclipse.swt.widgets.Composite;

public class AieonFServiceProvider implements IAieonFService<Composite>{

	private IDomainAieon domain;
	
	public AieonFServiceProvider() {
	}

	public void activate(){ /* NOTHING*/ };
	public void deactivate(){ /* NOTHING*/ }

	@Override
	public ISelectionBean[] getSelectionBeans() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setActiveDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	public void unsetActiveDomain(IDomainAieon domain) {
		this.domain = null;
	}

	@Override
	public IViewFactory<Composite, Composite> getCompositeFactory(String id) {
		// TODO Auto-generated method stub
		return null;
	};

}
