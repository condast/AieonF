package org.aieonf.commons.event;

import java.util.ArrayList;
import java.util.Collection;

/**
 * General format for a listener. Put the following code in a host object:
 *   
 * @author Kees
 *
 * @param <D>
 */
public class DataEventFacade<D extends Object> {

	private Collection<IDataEventListener<D>> listeners;
	
	public DataEventFacade() {
		this.listeners = new ArrayList<IDataEventListener<D>>();
	}
	
	public void addDataEventListener( IDataEventListener<D> listener ){
		this.listeners.add( listener );
	}

	public void removeDataEventListener( IDataEventListener<D> listener ){
		this.listeners.remove( listener );
	}
	
	public void notifyListeners( DataEvent<D> event ){
		for(IDataEventListener<D> listener: this.listeners )
			listener.notifyDataEvent(event);
	}
}
