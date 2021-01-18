package org.aieonf.commons.ui.thread;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * This utility handles all the issues when running a task in a separate thread.
 * It sees to activating the thread at the right time
 * @author Kees
 *
 */
public abstract class AbstractUIThreadController<D extends Object> implements Runnable {

	private Display display;
	
	private boolean async;
	private boolean busy;
	private boolean disposed;
	
	private D data;

	/**
	 * Run default asynchronously
	 * @param control
	 */
	protected AbstractUIThreadController( Control control) {
		this( control, true );
	}
	
	protected AbstractUIThreadController( Control control, boolean async) {
		this.display = control.getDisplay();
		control.addDisposeListener(e->onDispose(e) );
		this.disposed = false;
		this.async = async;
		this.busy = false;
	}

	protected D getData() {
		return data;
	}

	protected void setData(D data) {
		this.data = data;
	}

	/**
	 * The actual code to run
	 */
	protected abstract void onRun();
	
	protected void runThread() {
		if( disposed || busy )
			return;
		onRun();
		this.busy = false;
	}
	
	public void run() {
		if( disposed || this.display.isDisposed() || busy ) {
			busy = false;
			return;
		}
		//this.busy = true;
		if( async )
			this.display.asyncExec(()->runThread());
		else
			this.display.syncExec(()->runThread());			
	}
	
	void onDispose( DisposeEvent event ) {
		this.disposed = true;
	}
}