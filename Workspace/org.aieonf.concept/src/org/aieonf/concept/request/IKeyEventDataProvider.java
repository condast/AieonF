package org.aieonf.concept.request;

public interface IKeyEventDataProvider<R,D> {

	public void addKeyEventDataListener( IKeyEventDataListener<R,D> listener );

	public void removeKeyEventDataListener( IKeyEventDataListener<R,D> listener );

	public void handleKeyEvent( KeyEvent<R> event );
}
