package org.aieonf.concept.request;

public interface IKeyEventDataProvider<R,D> {

	public enum ProviderTypes{
		LINKS,
		SPONSOR,
		SEARCH;
	}
	
	public ProviderTypes getType();
	
	public void addKeyEventDataListener( IKeyEventDataListener<R,D> listener );

	public void removeKeyEventDataListener( IKeyEventDataListener<R,D> listener );

	public void handleKeyEvent( KeyEvent<R> event );
}
