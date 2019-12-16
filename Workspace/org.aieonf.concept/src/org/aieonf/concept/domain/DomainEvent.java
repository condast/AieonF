package org.aieonf.concept.domain;

import java.util.EventObject;

public class DomainEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private IDomainAieon domain;
	
	private IDomainSelection.SelectionEvents type;
	
	public DomainEvent(Object source, IDomainSelection.SelectionEvents type, IDomainAieon domain) {
		super(source);
		this.type = type;
		this.domain = domain;
	}

	public IDomainSelection.SelectionEvents getType() {
		return type;
	}

	public IDomainAieon getDomain() {
		return domain;
	}
}
