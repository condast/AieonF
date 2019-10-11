package org.aieonf.concept.domain;

import java.util.EventObject;

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
