package org.aieonf.concept.request;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractKeyEventDataProvider<R, D> implements IKeyEventDataProvider<R, D> {

	private String name;
	
	private ProviderTypes type;
	
	private Collection<IKeyEventDataListener<R, D>> listeners;
	
	protected AbstractKeyEventDataProvider( String name, ProviderTypes type ) {
		this.name = name;
		this.type = type;
		listeners = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public ProviderTypes getType() {
		return type;
	}

	@Override
	public void addKeyEventDataListener(IKeyEventDataListener<R, D> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeKeyEventDataListener(IKeyEventDataListener<R, D> listener) {
		this.listeners.remove(listener);
	}

	protected void notifyDataProcessed( DataProcessedEvent<R, D> event ) {
		for( IKeyEventDataListener<R, D> listener: this.listeners )
			listener.notifyKeyEventProcessed(event);
	}
	
	protected abstract D onProcesskeyEvent( KeyEvent<R> event );
	
	@Override
	public void handleKeyEvent(KeyEvent<R> event) {
		D data = onProcesskeyEvent(event);
		if( data == null )
			return;
		for( IKeyEventDataListener<R, D> listener: this.listeners )
			listener.notifyKeyEventProcessed( new DataProcessedEvent<R, D>(this, event, data));
	}
}
