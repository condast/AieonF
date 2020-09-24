package org.aieonf.commons.ui.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

public class PushSession<T> {

	public static final int DEFAULT_TIMEOUT = 5000;
	private Display display;
	
	private ServerPushSession session;
	private boolean refresh;
	private int sleep;
	private boolean started;

	private Timer timer;

	private Collection<ISessionListener<T>> listeners;

	private LinkedList<T> data;

	public PushSession() {
		this( DEFAULT_TIMEOUT );
	}
	
	protected PushSession( int sleep ) {
		listeners = new ArrayList<ISessionListener<T>>();
		data = new LinkedList<T>();
		this.sleep = sleep;
		this.started = false;
		timer = new Timer();
		timer.schedule(new PollTask(), 5000, this.sleep);
	}

	public void addSessionListener( ISessionListener<T> listener ){
		this.listeners.add( listener );
	}

	public void removeSessionListener( ISessionListener<T> listener ){
		this.listeners.remove( listener );
	}

	public void init( Display display ){
			this.display = display;
	}

	public synchronized void addData( T dt ){
		if(!started )
			return;
		data.add(dt);
		this.refresh = !data.isEmpty();
	}
	
	/**
	 * @author Kees
	 * Start the push session. Put in try/catch to prevent a system freeze on
	 * the server. Not quite sure why java.util.concurrent.RejectedExecutionException occurs
	 */
	public void start(){
		session = new ServerPushSession();
		try {
			session.start();
			this.started = true;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	public void stop(){
		this.started = false;
	}
	

	public void dispose(){
		if( this.listeners != null )
			this.listeners.clear();
		this.display = null;
		this.stop();
	}
	
	private class PollTask extends TimerTask {

	    @Override
	    public void run() {
			if( !refresh || !started || ( display == null ) || ( display.isDisposed()))
				return;
			display.asyncExec( new Runnable() {
				@Override
				public void run() {
					for(ISessionListener<T> listener: listeners){
						while( !data.isEmpty())
							listener.notifySessionChanged( new SessionEvent<T>( this, data.removeFirst() ));
					}
					session.stop();
					start();
				}
			});
		};
	}
}
