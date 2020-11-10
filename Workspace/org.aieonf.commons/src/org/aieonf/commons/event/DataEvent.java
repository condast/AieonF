package org.aieonf.commons.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

import org.aieonf.commons.event.IDataEventListener.DataEvents;

public class DataEvent<D extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private DataEvents devent;
	
	private Collection<D> data;

	public DataEvent(Object source, DataEvents devent, D datum ) {
		super(source);
		this.devent = devent;
		this.data = new ArrayList<D>();
		this.data.add( datum );
	}
	
	public DataEvent(Object source, DataEvents devent, Collection<D> data ) {
		super(source);
		this.devent = devent;
		this.data = data;
	}

	public DataEvents getDataEvent() {
		return devent;
	}

	public Collection<D> getData() {
		return data;
	}
	
	public D getDatum(){
		return this.data.iterator().next();
	}
}
