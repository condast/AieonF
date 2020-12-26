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
public abstract class AbstractUIThreadController implements Runnable {

	private Display display;
	
	private boolean async;
	private boolean disposed;

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
	}

	/**
	 * The actual code to run
	 */
	protected abstract void onRun();
	
	public void run() {
		if( disposed || this.display.isDisposed())
			return;
		if( async )
			this.display.asyncExec(()->onRun());
		else
			this.display.syncExec(()->onRun());			
	}
	
	void onDispose( DisposeEvent event ) {
		this.disposed = true;
	}
}
