package org.aieonf.concept.request;

public interface IKeyEventListener<R extends Object>
{
	/**
	 * Notify listeners of a key event change
	 * @param event
	 */
	public void notifyKeyEventReceived( KeyEvent<R> event );
}
