package org.aieonf.model.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.aieonf.commons.Utils;
import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.http.AbstractHttpRequest;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.commons.http.IHttpRequest.HttpStatus;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.FilterFactory;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;

import com.google.gson.Gson;

public abstract class AbstractRestDatabase<D extends IDescriptor, M extends IModelLeaf<D>> implements IModelDatabase<IDomainAieon, D, M> {

	private ISecureGenerator generator;
	private String path;
	private IDomainAieon domain;
	private boolean open;
	
	//Declared here because of its use in a listener
	private String id;

	private Collection<IModelListener<M>> listeners;

	protected AbstractRestDatabase( ISecureGenerator generator, IDomainAieon domain, String path ) {
		this.path = path;
		id = null;
		this.domain = domain;
		this.generator = generator;
		this.open = false;
		this.listeners = new ArrayList<>();
	}

	@Override
	public void open(IDomainAieon key) {
		this.open = domain.equals(key);
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void close() {
		this.open = false;
	}

	@Override
	public void sync() {
	}

	@Override
	public void deactivate() {
		this.open = false;
	}

	@Override
	public String getIdentifier() {
		return domain.getDomain();
	}

	@Override
	public boolean hasFunction(String function) {
		return true;
	}

	@Override
	public void addListener(IModelListener<M> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<M> listener) {
		this.listeners.remove(listener);
	}

	protected void notifyListeners( ModelEvent<M> event ) {
		for( IModelListener<M> listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Create a model
	 * @return
	 */
	protected abstract IModelLeaf<? extends IDescriptor> onCreateModel( );

	/**
	 * Create a model
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IModelLeaf<? extends IDescriptor> createModel(){
		IModelLeaf<? extends IDescriptor> model = onCreateModel();
		notifyListeners( new ModelEvent(this, model ));
		return model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean create(M leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.CREATE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] added = new IModelLeaf[1]; 
		added[0] = leaf;
		boolean result = false;
		WebClient<IModelLeaf<? extends IDescriptor>[]> client = new WebClient<>( path );
		Collection<IModelLeaf<? extends IDescriptor>>results = new ArrayList<>();
		try {
			client.addListener( e->{
				if( HttpStatus.OK.getStatus() != e.getResponseCode())
					return;
				String id = e.getResponse();
				IModelLeaf<? extends IDescriptor>[] models = e.getData();
				models[0].set(IDescriptor.Attributes.ID.name(), id);
			});
			client.sendPost(request, parameters, onSerialise(added), added);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public long add(M leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.ADD;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] added = new IModelLeaf[1]; 
		added[0] = leaf;
		WebClient<IModelLeaf<? extends IDescriptor>[]> client = new WebClient<>( path );
		try {
			client.addListener( e->{
				if( HttpStatus.OK.getStatus() != e.getResponseCode())
					return;
				id = e.getResponse();
				IModelLeaf<? extends IDescriptor>[] models = e.getData();
				models[0].set(IDescriptor.Attributes.ID.name(), id);
			});
			client.sendPost(request, parameters, onSerialise(added), added);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtils.isEmpty(id)?-1: Long.parseLong(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addNode(long modelId, String label, M child) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.ADD_NODE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf( modelId ));
		parameters.put( Attributes.LABEL.toString(), label);
		IModelLeaf<? extends IDescriptor>[] added = new IModelLeaf[1]; 
		added[0] = child;
		boolean result = false;
		WebClient<IModelLeaf<? extends IDescriptor>[]> client = new WebClient<>( path );
		Collection<IModelLeaf<? extends IDescriptor>>results = new ArrayList<>();
		try {
			client.addListener( e->{
				if( HttpStatus.OK.getStatus() != e.getResponseCode())
					return;
				String id = e.getResponse();
				IModelLeaf<? extends IDescriptor>[] models = e.getData();
				models[0].set(IDescriptor.Attributes.ID.name(), id);
			});
			client.sendPost(request, parameters, onSerialise(added), added);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	protected abstract void getResults( ResponseEvent<IDatabaseConnection.Requests, M[]> event, Collection<M> results );

	@Override
	public boolean contains(M leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.CONTAINS;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf( leaf.getID() ));
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !Utils.assertNull(results);
	}

	@Override
	public M find( long id) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.FIND;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.MODEL_ID.toString(), String.valueOf(id));
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ));
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.assertNull(results)? null: results.iterator().next();
	}

	@Override
	public M findOnDescriptor( long id) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.FIND_ON_DESCRIPTOR;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.MODEL_ID.toString(), String.valueOf(id));
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ));
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.assertNull(results)? null: results.iterator().next();
	}

	@Override
	public Collection<M> searchModels( String key, String value) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.SEARCH_MODELS;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.KEY.toString(), key);
		parameters.put( Attributes.VALUE.toString(), value);
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ));
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public Collection<M> get(IDescriptor descriptor) throws ParseException {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.GET;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf( descriptor.getID() ));
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@Override
	public Collection<M> search(IModelFilter<D,M> filter)
			throws ParseException {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.SEARCH;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put(StringStyler.xmlStyleString( IDomainAieon.Attributes.DOMAIN.name()), domain.getDomain());
		parameters.put(FilterFactory.Attributes.FILTER.toString(), StringStyler.xmlStyleString( filter.getType().name() ));
		parameters.put(FilterFactory.Attributes.RULES.toString(), filter.getRule() );
		parameters.put(FilterFactory.Attributes.REFERENCE.toString(), filter.getReference() );
		parameters.put(FilterFactory.Attributes.VALUE.toString(), filter.getValue() );
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	protected abstract String onSerialise( IModelLeaf<? extends IDescriptor>[] leaf);
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(M leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.REMOVE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf( leaf.getID() ));
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] removed = new IModelLeaf[1]; 
		removed[0] = leaf;
		WebClient<IModelLeaf<? extends IDescriptor>[]> client = new WebClient<>( path );
		Collection<IModelLeaf<? extends IDescriptor>>results = new ArrayList<>();
		boolean result = false;
		try {
			//client.addListener( e->getResults(e, results ));
			Gson gson = new Gson();
			String str = gson.toJson(leaf.getID(), Entry.class);
			client.sendDelete(request, parameters, str, removed);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Collection<M> remove( long parent, long[] children) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.REMOVE_CHILDREN;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.MODEL_ID.toString(), String.valueOf(parent));
		WebClient<M[]> client = new WebClient<>( path );
		Collection<M>results = new ArrayList<>();
		try {
			client.addListener( e->getResults(e, results ));
			Gson gson = new Gson();
			String str = gson.toJson(children, long[].class);
			client.sendDelete(request, parameters, str, children);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public boolean remove( long[] ids) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.REMOVE_ALL;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		WebClient<M[]> client = new WebClient<>( path );
		Collection<IModelLeaf<? extends IDescriptor>>results = new ArrayList<>();
		boolean result = false;
		try {
			//client.addListener( e->getResults(e, results ));
			Gson gson = new Gson();
			String str = gson.toJson(ids, long[].class);
			client.sendDelete(request, parameters, str, ids);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean remove(Entry<Long, Long[]> ids) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.REMOVE_ALL;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf( ids.getKey() ));
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		WebClient<Entry<Long, Long[]>> client = new WebClient<>( path );
		boolean result = false;
		try {
			//client.addListener( e->getResults(e, results ));
			Gson gson = new Gson();
			String str = gson.toJson(ids, Entry.class);
			client.sendDelete(request, parameters, str, ids);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(M leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.UPDATE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), String.valueOf(  leaf.getID() ));
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		WebClient<M> client = new WebClient<>( path );
		IModelLeaf<? extends IDescriptor>[] updated = new IModelLeaf[1]; 
		updated[0] = leaf;
		boolean result = false;
		try {
			client.sendPut(request, parameters, onSerialise(updated), leaf);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private class WebClient<DS extends Object> extends AbstractHttpRequest<IDatabaseConnection.Requests, DS>{

		public WebClient(String path) {
			super(path);
		}
	
		@Override
		public void sendGet( IDatabaseConnection.Requests request, Map<String, String> parameters) throws Exception {
			super.sendGet(request, parameters);
		}

		@Override
		public void sendPost( IDatabaseConnection.Requests request, Map<String, String> parameters, String post, DS data) throws Exception {
			super.sendPost(request, parameters, post, data );
		}

		@Override
		public void sendDelete( IDatabaseConnection.Requests request, Map<String, String> parameters, String post, DS data) throws Exception {
			super.sendDelete(request, parameters, post, data);
		}

		public void sendDelete( IDatabaseConnection.Requests request, Map<String, String> parameters, String post, long[] data) throws Exception {
			super.sendDelete(request, parameters, post, null);
		}

		@Override
		protected String onHandleResponse(ResponseEvent<IDatabaseConnection.Requests, DS> response, DS data ) throws IOException {
			//IModelLeaf<? extends IDescriptor>[] result = onProcessResponse( response );
			//notifyKeyEventChanged( new KeyEvent<IDatabaseConnection.Requests, IModelLeaf<? extends IDescriptor>[]>( this, response.getRequest(), response.getParameters(), result ));
			return response.getResponse();
		}	
	}
}
