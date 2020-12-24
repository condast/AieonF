package org.aieonf.osgi.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.persistence.ISessionStoreFactory;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.request.DataProcessedEvent;
import org.aieonf.concept.request.IKeyEventDataListener;
import org.aieonf.concept.request.IKeyEventDataProvider;
import org.aieonf.concept.request.IKeyEventListener;
import org.aieonf.concept.request.KeyEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelDatabase;
import org.eclipse.rap.rwt.RWT;

public abstract class AbstractBundleDispatcher<D extends Object> implements ISecureGenerator, IKeyEventListener<IDatabaseConnection.Requests>{

	private ISecureGenerator generator;

	private String path;
	private IModelDatabase<IDomainAieon, IDescriptor, IModelLeaf<IDescriptor>> database;
	
	private IDomainAieon domain; 

	private RestDataProvider restProvider;	

	private ISessionStoreFactory<HttpSession, D> store;

	protected AbstractBundleDispatcher( String path ) {
		this( path, null );
	}

	protected AbstractBundleDispatcher( String path, IDomainAieon domain) {
		this.path = path;
		this.domain = domain;
		restProvider = new RestDataProvider();
	}
	
	public void clear() {
		this.restProvider.input.clear();
	}
	
	public IDomainAieon getDomain() {
		return domain;
	}

	protected void setDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	protected RestDataProvider getRestProvider() {
		return restProvider;
	}

	@SuppressWarnings("unchecked")
	public IModelLeaf<IDescriptor>[] getInput(){
		Collection<IModelLeaf<IDescriptor>> input = this.restProvider.input;
		return input.toArray( new IModelLeaf[input.size()]);
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

	public void setSessionStore(ISessionStoreFactory<HttpSession, D> store) {
		this.store = store;
	}	

	public void removeSessionStore(ISessionStoreFactory<HttpSession, D> store) {
		this.store = null;
	}	

	public D getStore() {
		HttpSession session = RWT.getUISession().getHttpSession();
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

	public void addKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
		this.restProvider.addKeyEventDataListener(listener);
	}

	public void removeKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
		this.restProvider.removeKeyEventDataListener(listener);
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

	protected abstract void onHandleKeyEvent(KeyEvent<IDatabaseConnection.Requests> event, Collection<IModelLeaf<IDescriptor>> input);

	@Override
	public void notifyKeyEventReceived(KeyEvent<IDatabaseConnection.Requests> event) {
		restProvider.handleKeyEvent( event);
	}
	
	/**
	 * allow other providers to add data
	 * @param event
	 */
	protected void notifyDataFound( DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> event ) {
		this.restProvider.notifyDataFound(event);
	}
	
	protected abstract void update();
	
	/**
	 * Update the system by calling the last key event
	 */
	protected void update( KeyEvent<IDatabaseConnection.Requests> event ) {
		if( event != null )
			restProvider.handleKeyEvent( event );		
	}
	
	protected class RestDataProvider implements IKeyEventDataProvider<IDatabaseConnection.Requests,IModelLeaf<IDescriptor>[]>{

		private Collection<IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>> listeners;

		private Collection<IModelLeaf<IDescriptor>> input;

		private RestDataProvider() {
			super();
			this.input = new ArrayList<>();
			this.listeners = new ArrayList<>();
		}
		
		public Collection<IModelLeaf<IDescriptor>> getInput() {
			return input;
		}

		@Override
		public void addKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
			listeners.add(listener);
		}

		@Override
		public void removeKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
			listeners.remove(listener);
		}
	
		protected void notifyDataProcessedEvent( DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> event ) {
			for( IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener: listeners)
				listener.notifyKeyEventProcessed( event);
		}
		
		@SuppressWarnings("unchecked")
		private void notifyDataFound( DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> event ) {
			IModelLeaf<IDescriptor>[] results = null;
			switch( event.getRequest()) {
			case PREPARE:
				this.input.clear();
				break;
			default:
				results = input.toArray( new IModelLeaf[this.input.size()]); ;
				break;
			}
			DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> devent = 
					new DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>( this, event.getKeyEvent(), results);
			notifyDataProcessedEvent( devent );
		}

		@SuppressWarnings("unchecked")
		@Override
		public void handleKeyEvent(KeyEvent<IDatabaseConnection.Requests> event ) {
			onHandleKeyEvent(event, input);
			notifyDataFound( new DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>(this, event, input.toArray( new IModelLeaf[input.size()])));
		}

	}

}