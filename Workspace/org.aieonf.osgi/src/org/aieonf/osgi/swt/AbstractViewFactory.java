package org.aieonf.osgi.swt;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.swt.IViewFactory;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractViewFactory<C extends Control> implements IViewFactory<C, C> {

	private IDomainAieon domain;
	
	protected AbstractViewFactory( IDomainAieon domain ) {
		this.domain = domain;
	}

	@Override
	public boolean isOfDomain(IDomainAieon domain) {
		return ( this.domain != null ) && this.domain.equals(domain);
	}

	@Override
	public String getIdentifier() {
		return domain.getShortName();
	}
}
