package org.aieonf.osgi.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.aieonf.commons.Utils;
import org.aieonf.commons.db.IDatabaseConnection;
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

public abstract class AbstractBundleDispatcher implements IKeyEventListener<IDatabaseConnection.Requests>{

	private ISecureGenerator generator;

	private String path;
	private IModelDatabase<IDescriptor, IModelLeaf<IDescriptor>> database;
	
	private IDomainAieon domain; 

	private RestDataProvider restProvider;	

	protected AbstractBundleDispatcher( String path ) {
		this.path = path;
		restProvider = new RestDataProvider();
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

	@SuppressWarnings("unchecked")
	public IModelLeaf<IDescriptor>[] getInput(){
		Collection<IModelLeaf<IDescriptor>> input = this.restProvider.input;
		return input.toArray( new IModelLeaf[input.size()]);
	}

	protected abstract IModelDatabase<IDescriptor, IModelLeaf<IDescriptor>> createDatabase( ISecureGenerator generator, IDomainAieon domain, String path );
	
	public void setGenerator(ISecureGenerator generator) {
		this.generator = generator;
		database = createDatabase(generator, domain, path);
	}

	protected IModelDatabase<IDescriptor, IModelLeaf<IDescriptor>> getDatabase() {
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
	
	private class RestDataProvider implements IKeyEventDataProvider<IDatabaseConnection.Requests,IModelLeaf<IDescriptor>[]>{

		private Collection<IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>> listeners;

		private Collection<IModelLeaf<IDescriptor>> input;

		private RestDataProvider() {
			super();
			this.input = new ArrayList<>();
			this.listeners = new ArrayList<>();
		}

		@Override
		public void addKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
			listeners.add(listener);
		}

		@Override
		public void removeKeyEventDataListener(IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener) {
			listeners.remove(listener);
		}
	
		private void notifyDataFound( DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> event ) {
			IModelLeaf<IDescriptor>[] results = addInput(event.getData());
			DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> devent = 
					new DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>( this, event.getKeyEvent(), results);
			for( IKeyEventDataListener<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]> listener: listeners)
				listener.notifyKeyEventProcessed( devent);
		}

		@SuppressWarnings("unchecked")
		private IModelLeaf<IDescriptor>[] addInput( IModelLeaf<IDescriptor>[] models ) {
			IModelLeaf<IDescriptor>[] results = input.toArray( new IModelLeaf[this.input.size()]); 
			if( Utils.assertNull(models))
				return results;
			this.input.addAll(Arrays.asList(models));			
			results = input.toArray( new IModelLeaf[this.input.size()]); 
			return results;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleKeyEvent(KeyEvent<IDatabaseConnection.Requests> event ) {
			onHandleKeyEvent(event, input);
			notifyDataFound( new DataProcessedEvent<IDatabaseConnection.Requests, IModelLeaf<IDescriptor>[]>(this, event, input.toArray( new IModelLeaf[input.size()])));
		}

	}

}