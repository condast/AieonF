package org.aieonf.osgi.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.db.IDatabaseConnection.Requests;
import org.aieonf.commons.persistence.ISessionStoreFactory;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.request.IKeyEventListener;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelDatabase;

public abstract class AbstractBundleDispatcher<D extends Object> implements ISecureGenerator, 
IKeyEventListener<IDatabaseConnection.Requests>{

	private ISecureGenerator generator;

	private String path;
	private IModelDatabase<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> database;
	
	private IDomainAieon domain; 

	private ISessionStoreFactory<HttpSession, D> store;

	private Collection<IKeyEventListener<IDatabaseConnection.Requests>> listeners;

	protected AbstractBundleDispatcher( String path ) {
		this( path, null );
	}

	protected AbstractBundleDispatcher( String path, IDomainAieon domain) {
		this.path = path;
		this.domain = domain;
		this.listeners = new ArrayList<>();
	}
		
	public IDomainAieon getDomain() {
		return domain;
	}

	protected void setDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	public ISecureGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ISecureGenerator generator) {
		this.generator = generator;
		database = createDatabase(generator, domain, path);
	}
	
	@Override
	public Entry<Long, Long> createIdAndToken(String domain) {
		return this.generator.createIdAndToken(domain);
	}

	public void addKeyEventListener(IKeyEventListener<IDatabaseConnection.Requests> listener) {
		listeners.add(listener);
	}

	public void removeKeyEventListener(IKeyEventListener<IDatabaseConnection.Requests> listener) {
		listeners.remove(listener);
	}

	public void notifyKeyEventReceived( KeyEvent<IDatabaseConnection.Requests> event ) {
		for( IKeyEventListener<Requests> listener: this.listeners )
			listener.notifyKeyEventReceived(event);	
	}	

	public void setSessionStore(ISessionStoreFactory<HttpSession, D> store) {
		this.store = store;
	}	

	public void removeSessionStore(ISessionStoreFactory<HttpSession, D> store) {
		this.store = null;
	}	

	/**
	 * Get the store for the given session
	 * @param session
	 * @return
	 */
	public D getStore( HttpSession session ) {
		return store.createSessionStore(session);
	}

	/**
	 * Create the database
	 * @param generator
	 * @param domain
	 * @param path
	 * @return
	 */
	protected abstract IModelDatabase<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> createDatabase( ISecureGenerator generator, IDomainAieon domain, String path );

	protected IModelDatabase<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> getDatabase() {
		return database;
	}

	public boolean isRegistered(long id, String name) {
		return (( domain.getID() == id ) && ( domain.getDomain().equals(name)));
	}

	public IDomainAieon getDomain(long id, long token, String name) {
		if(( domain.getID() == id ) && ( domain.getDomain().equals(name)))
			return domain;
		return null;
	}

	protected IDomainAieon getDomain( String selected ) {
		if( StringUtils.isEmpty(selected))
			return null;
		if( domain.getDomain().equals(selected))
			return( domain);
		return null;	
	}

	public IDomainAieon[] getDomains() {
		Collection<IDomainAieon> results = new ArrayList<>();
		results.add( domain);
		return results.toArray( new IDomainAieon[results.size()]);	
	}
}