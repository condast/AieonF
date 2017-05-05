package org.aieonf.osgi.selection;

import java.util.EventObject;

import org.aieonf.concept.domain.IDomainAieon;

public class DomainEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private IDomainAieon domain;
	
	public DomainEvent(Object source, IDomainAieon domain) {
		super(source);
		this.domain = domain;
	}

	public IDomainAieon getDomain() {
		return domain;
	}
}
