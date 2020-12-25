package org.aieonf.commons.persistence;

@FunctionalInterface
public interface ISessionStoreFactory<I,D extends Object> {
	
	public D createSessionStore( I session );
}
