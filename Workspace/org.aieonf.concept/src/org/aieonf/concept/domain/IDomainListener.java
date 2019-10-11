package org.aieonf.concept.domain;

public interface IDomainListener {

	public void notifyDomainChange( DomainEvent event );
}
