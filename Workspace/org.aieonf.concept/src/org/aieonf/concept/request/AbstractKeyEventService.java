package org.aieonf.concept.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.domain.IDomainAieon;

/**
 * Send a Request <R> and returns data <D> 
 * @author Kees
 *
 * @param <R>
 * @param <D>
 */
public abstract class AbstractKeyEventService<R>
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

	private IDomainAieon domain;
	
	private Collection<IKeyEventListener<R>> listeners;
	
	protected AbstractKeyEventService( IDomainAieon domain ){
		this.domain = domain;
		this.listeners = new ArrayList<IKeyEventListener<R>>();
	}
	
	protected IDomainAieon getDomain() {
		return domain;
	}

	public void addListener( IKeyEventListener<R> listener ){
		this.listeners.add(listener);
	}

	public void removeListener( IKeyEventListener<R> listener ){
		this.listeners.remove(listener);
	}
	
	protected void notifyKeyEventChanged( KeyEvent<R> event ){
		for( IKeyEventListener<R> listener: listeners ){
			listener.notifyKeyEventReceived(event);
		}
	}

	public void setEvent( R request, Map<String, String> parameters ) throws Exception{
		notifyKeyEventChanged( new KeyEvent<R>( this, request, parameters ));
	}

	public void setEvent( R request, long id, long token ) throws Exception{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		notifyKeyEventChanged( new KeyEvent<R>( this, request, parameters ));
	}
}
