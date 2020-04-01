package org.aieonf.concept.rest;

public interface IKeyEventListener<T,U extends Object>
{
	/**
	 * Notify listeners of a key event change
	 * @param event
	 */
	public void notifyKeyEventReceived( KeyEvent<T,U> event );
}
