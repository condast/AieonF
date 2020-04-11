package org.aieonf.model.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.db.IDatabaseConnection;
import org.aieonf.commons.http.AbstractHttpRequest;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.security.ISecureGenerator;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.filter.FilterFactory;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDatabase;

public abstract class AbstractRestDatabase implements IModelDatabase<IDescriptor, IModelLeaf<? extends IDescriptor>> {

	public enum Attributes{
		ID,
		TOKEN,
		DOMAIN,
		MODEL_ID,
		CATEGORY,
		WILDCARD;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( super.name());
		}
	}

	private ISecureGenerator generator;
	private String path;
	private IDomainAieon domain;
	private boolean open;
	
	private Collection<IModelListener<IModelLeaf<? extends IDescriptor>>> listeners;

	Collection<IModelLeaf<? extends IDescriptor>> results;
	
	protected AbstractRestDatabase( ISecureGenerator generator, IDomainAieon domain, String path ) {
		this.path = path;
		this.domain = domain;
		this.generator = generator;
		this.open = false;
		this.listeners = new ArrayList<>();
		results = new ArrayList<>();
	}

	@Override
	public void open(IDescriptor key) {
		this.open = true;
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
		// TODO Auto-generated method stub
		
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
	public void addListener(IModelListener<IModelLeaf<? extends IDescriptor>> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(IModelListener<IModelLeaf<? extends IDescriptor>> listener) {
		this.listeners.remove(listener);
	}

	protected void notifyListeners( ModelEvent<IModelLeaf<? extends IDescriptor>> event ) {
		for( IModelListener<IModelLeaf<? extends IDescriptor>> listener: this.listeners )
			listener.notifyChange(event);
	}

	@Override
	public boolean contains(IModelLeaf<? extends IDescriptor> leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.CONTAINS;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), leaf.getID() );
		WebClient client = new WebClient( path );
		results.clear();
		try {
			client.addListener( e->onGetResults(e) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !Utils.assertNull(results);
	}

	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> get(IDescriptor descriptor) throws ParseException {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.GET;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), descriptor.getID() );
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		WebClient client = new WebClient( path );
		results.clear();
		try {
			client.addListener( e->onGetResults(e) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	protected void onGetResults( ResponseEvent<IDatabaseConnection.Requests, IModelLeaf<? extends IDescriptor>[]> event ) {
		results = Arrays.asList( event.getData());
	}

	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> search(IModelFilter<IDescriptor, IModelLeaf<? extends IDescriptor>> filter)
			throws ParseException {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.SEARCH;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(FilterFactory.Attributes.FILTER.toString(), filter.getName() );
		parameters.put(FilterFactory.Attributes.RULES.toString(), filter.getRule() );
		parameters.put(FilterFactory.Attributes.REFERENCE.toString(), filter.getReference() );
		parameters.put(FilterFactory.Attributes.VALUE.toString(), filter.getAttribute() );
		WebClient client = new WebClient( path );
		results.clear();
		try {
			client.addListener( e->onGetResults(e) );
			client.sendGet(request, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	protected abstract String onSerialise( IModelLeaf<? extends IDescriptor>[] leaf);
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean add(IModelLeaf<? extends IDescriptor> leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.ADD;
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] added = new IModelLeaf[1]; 
		added[0] = leaf;
		boolean result = false;
		WebClient client = new WebClient( path );
		results.clear();
		try {
			client.addListener( e->onGetResults(e) );
			client.sendPost(request, parameters, onSerialise(added), added);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(IModelLeaf<? extends IDescriptor> leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.REMOVE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), leaf.getID() );
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] removed = new IModelLeaf[1]; 
		removed[0] = leaf;
		WebClient client = new WebClient( path );
		results.clear();
		boolean result = false;
		try {
			client.addListener( e->onGetResults(e) );
			client.sendDelete(request, parameters, removed);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update(IModelLeaf<? extends IDescriptor> leaf) {
		Map.Entry<Long, Long> entry = generator.createIdAndToken( domain.getDomain());
		IDatabaseConnection.Requests request = IDatabaseConnection.Requests.UPDATE;
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		parameters.put(Attributes.MODEL_ID.toString(), leaf.getID() );
		parameters.put( Attributes.ID.toString(), String.valueOf(entry.getKey()));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf( entry.getValue()));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		IModelLeaf<? extends IDescriptor>[] removed = new IModelLeaf[1]; 
		removed[0] = leaf;
		WebClient client = new WebClient( path );
		results.clear();
		boolean result = false;
		try {
			client.addListener( e->onGetResults(e) );
			client.sendDelete(request, parameters, removed);
			result = Utils.assertNull(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private class WebClient extends AbstractHttpRequest<IDatabaseConnection.Requests, IModelLeaf<? extends IDescriptor>[]>{

		public WebClient(String path) {
			super(path);
		}
	
		@Override
		public void sendGet( IDatabaseConnection.Requests request, Map<String, String> parameters) throws Exception {
			super.sendGet(request, parameters);
		}

		@Override
		public void sendPost( IDatabaseConnection.Requests request, Map<String, String> parameters, String post, IModelLeaf<? extends IDescriptor>[] data) throws Exception {
			super.sendPost(request, parameters, post, data );
		}

		public void sendDelete( IDatabaseConnection.Requests request, Map<String, String> parameters, IModelLeaf<? extends IDescriptor>[] data) throws Exception {
			super.sendDelete(request, parameters, data);
		}

		@Override
		protected String onHandleResponse(ResponseEvent<IDatabaseConnection.Requests, IModelLeaf<? extends IDescriptor>[]> response, IModelLeaf<? extends IDescriptor>[] data ) throws IOException {
			//IModelLeaf<? extends IDescriptor>[] result = onProcessResponse( response );
			//notifyKeyEventChanged( new KeyEvent<IDatabaseConnection.Requests, IModelLeaf<? extends IDescriptor>[]>( this, response.getRequest(), response.getParameters(), result ));
			return response.getResponse();
		}	
	}
}
