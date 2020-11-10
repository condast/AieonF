package org.aieonf.commons.event;

public interface IDataEventListener<D extends Object> {

	public enum DataEvents{
		SEARCH_RESULT,
		SHOW,
		EDIT,
		DELETED
	}
	
	public void notifyDataEvent( DataEvent<D> event );
}
