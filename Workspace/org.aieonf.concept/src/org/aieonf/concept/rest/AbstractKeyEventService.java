package org.aieonf.concept.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.http.AbstractHttpRequest;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.domain.IDomainAieon;

/**
 * Send a Request <R> and returns data <D> 
 * @author Kees
 *
 * @param <R>
 * @param <D>
 */
public abstract class AbstractKeyEventService<R,D>
{
	public enum Attributes{
		ID,
		TOKEN,
		DOMAIN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString(super.name());
		}
	}

	private String url;
	private IDomainAieon domain;
	
	private Collection<IKeyEventListener<R,D>> listeners;
	
	protected AbstractKeyEventService( IDomainAieon domain, String url ){
		this.domain = domain;
		this.url = url;
		this.listeners = new ArrayList<IKeyEventListener<R,D>>();
	}
	
	protected IDomainAieon getDomain() {
		return domain;
	}

	public void addListener( IKeyEventListener<R,D> listener ){
		this.listeners.add(listener);
	}

	public void removeListener( IKeyEventListener<R,D> listener ){
		this.listeners.remove(listener);
	}
	
	private void notifyKeyEventChanged( KeyEvent<R,D> event ){
		for( IKeyEventListener<R,D> listener: listeners ){
			listener.notifyKeyEventReceived(event);
		}
	}

	public void setEvent( R request, long id, long token ) throws Exception{
		setEvent( request, id, token, new HashMap<String, String>());
	}

	public void setEvent( R request, long id, long token, Map<String, String> parameters ) throws Exception{
		WebClient client = new WebClient( url);
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		client.sendGet(request, parameters);
	}

	public void add( R request, long id, long token, Map<String, String> parameters, String post, D data ) throws Exception{
		WebClient client = new WebClient( url);
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		client.sendPost(request, parameters, post, data);
	}

	public void delete( R request, long id, long token, Map<String, String> parameters, D data ) throws Exception{
		WebClient client = new WebClient( url);
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		client.sendDelete(request, parameters, data);
	}

	protected abstract D onProcessResponse( ResponseEvent<R,D> response );
	
	private class WebClient extends AbstractHttpRequest<R,D>{

		public WebClient(String path) {
			super(path);
		}
	
		@Override
		public void sendGet( R request, Map<String, String> parameters) throws Exception {
			super.sendGet(request, parameters);
		}

		@Override
		public void sendPost( R request, Map<String, String> parameters, String post, D data) throws Exception {
			super.sendPost(request, parameters, post, data );
		}

		public void sendDelete( R request, Map<String, String> parameters, D data) throws Exception {
			super.sendDelete(request, parameters, data);
		}

		@Override
		protected String onHandleResponse(ResponseEvent<R,D> response, D data ) throws IOException {
			D result = onProcessResponse( response );
			notifyKeyEventChanged( new KeyEvent<R,D>( this, response.getRequest(), response.getParameters(), result ));
			return response.getResponse();
		}	
	}

}
