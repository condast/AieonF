package org.aieonf.concept.request;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractKeyEventDataProvider<R, D> implements IKeyEventDataProvider<R, D> {

	private Collection<IKeyEventDataListener<R, D>> listeners;
	
	public AbstractKeyEventDataProvider() {
		listeners = new ArrayList<>();
	}

	@Override
	public void addKeyEventDataListener(IKeyEventDataListener<R, D> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeKeyEventDataListener(IKeyEventDataListener<R, D> listener) {
		this.listeners.remove(listener);
	}

	protected abstract D onProcesskeyEvent( KeyEvent<R> event );
	
	@Override
	public void registerKeyEvent(KeyEvent<R> event) {
		D data = onProcesskeyEvent(event);
		if( data == null )
			return;
		for( IKeyEventDataListener<R, D> listener: this.listeners )
			listener.notifyKeyEventProcessed( new DataProcessedEvent<R, D>(this, event, data));
	}
}
