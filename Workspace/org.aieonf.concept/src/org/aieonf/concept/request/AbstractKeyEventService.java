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
public abstract class AbstractKeyEventService<R> implements IKeyEventService<R>
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

	protected void setDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	@Override
	public void addListener( IKeyEventListener<R> listener ){
		this.listeners.add(listener);
	}

	@Override
	public void removeListener( IKeyEventListener<R> listener ){
		this.listeners.remove(listener);
	}
	
	protected void notifyKeyEventChanged( KeyEvent<R> event ){
		for( IKeyEventListener<R> listener: listeners ){
			listener.notifyKeyEventReceived(event);
		}
	}

	@Override
	public void setEvent( R request, Map<String, String> parameters ) throws Exception{
		notifyKeyEventChanged( new KeyEvent<R>( this, request, parameters ));
	}

	@Override
	public void setEvent( R request, long id, long token ) throws Exception{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put( Attributes.ID.toString(), String.valueOf(id));
		parameters.put( Attributes.TOKEN.toString(), String.valueOf(token));
		parameters.put( Attributes.DOMAIN.toString(), domain.getDomain());
		notifyKeyEventChanged( new KeyEvent<R>( this, request, parameters ));
	}
	
	protected void prepare( R request ){
		Map<String, String> parameters = new HashMap<>();
		parameters.put(IDomainAieon.Attributes.DOMAIN.toString(), domain.getDomain());
		KeyEvent<R> event = new KeyEvent<R>( this, request, parameters );
		notifyKeyEventChanged(event);
	}
}
