package org.aieonf.concept.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.http.AbstractHttpRequest;
import org.aieonf.commons.http.ResponseEvent;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.request.AbstractKeyEventService;
import org.aieonf.concept.request.KeyEvent;

/**
 * Send a Request <R> and returns data <D> 
 * @author Kees
 *
 * @param <R>
 * @param <D>
 */
public abstract class AbstractRESTKeyEventService<R,D> extends AbstractKeyEventService<R>
{
	private String url;
	
	protected AbstractRESTKeyEventService( IDomainAieon domain, String url ){
		super( domain );
		this.url = url;
	}
	
	@Override
	public void setEvent( R request, long id, long token ) throws Exception{
		setEvent( request, id, token, new HashMap<String, String>());
	}

	public void setEvent( R request, long id, long token, Map<String, String> parameters ) throws Exception{
		WebClient client = new WebClient( url);
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), super.getDomain().getDomain());
		client.sendGet(request, parameters);
	}

	public void add( R request, long id, long token, Map<String, String> parameters, String post, D data ) throws Exception{
		WebClient client = new WebClient( url );
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), super.getDomain().getDomain());
		client.sendPost(request, parameters, post, data);
	}

	public void delete( R request, long id, long token, Map<String, String> parameters, String post, D data ) throws Exception{
		WebClient client = new WebClient( url);
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), super.getDomain().getDomain());
		client.sendDelete(request, parameters, post, data);
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

		@Override
		public void sendDelete( R request, Map<String, String> parameters, String post, D data) throws Exception {
			super.sendDelete(request, parameters, post, data);
		}

		@Override
		protected String onHandleResponse(ResponseEvent<R,D> response, D data ) throws IOException {
			notifyKeyEventChanged( new KeyEvent<R>( this, response.getRequest(), response.getParameters() ));
			return response.getResponse();
		}	
	}

}
