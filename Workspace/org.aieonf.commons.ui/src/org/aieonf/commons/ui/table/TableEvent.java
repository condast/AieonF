package org.aieonf.commons.ui.table;

import java.util.EventObject;

import org.aieonf.commons.ui.table.ITableEventListener.TableEvents;

public class TableEvent<D,C extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private TableEvents event;
	private D data;
	private C children;

	public TableEvent(Object source, TableEvents event ) {
		this( source, event, null );
	}

	public TableEvent(Object source, TableEvents event, D data ) {
		this( source, event, data, null );
	}
	
	public TableEvent(Object source, TableEvents event, D data, C children ) {
		super(source);
		this.event = event;
		this.data = data;
		this.children = children;
	}

	public TableEvents getTableEvent() {
		return event;
	}

	public D getData() {
		return data;
	}

	public C getChildren() {
		return children;
	}
}
