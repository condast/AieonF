package org.aieonf.commons.petition;

import java.util.ArrayList;
import java.util.Collection;

public class KeyEventService<T,U>
{

	private Collection<IKeyEventListener<T,U>> listeners;
	
	protected KeyEventService()
	{
		this.listeners = new ArrayList<IKeyEventListener<T,U>>();
	}

	public void addListener( IKeyEventListener<T,U> listener ){
		this.listeners.add(listener);
	}

	public void removeListener( IKeyEventListener<T,U> listener ){
		this.listeners.remove(listener);
	}
	
	private void notifyKeyEventChanged( KeyEvent<T,U> event ){
		for( IKeyEventListener<T,U> listener: listeners ){
			listener.notifyKeyEventReceived(event);
		}
	}

	public void setEvent( Object source, T key ){
		this.notifyKeyEventChanged( new KeyEvent<T,U>( source, key ));
	}

	public void setEvent( Object source, T key, U value ){
		this.notifyKeyEventChanged( new KeyEvent<T,U>( source, key, value ));
	}
}
