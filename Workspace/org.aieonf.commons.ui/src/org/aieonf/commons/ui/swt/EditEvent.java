package org.aieonf.commons.ui.swt;

import java.util.EventObject;

import org.aieonf.commons.strings.StringStyler;

public class EditEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	public enum EditTypes{
		UNKNOWN,
		CHANGED,
		FILLED;

		@Override
		public String toString() {
			return StringStyler.prettyString(name());
		}
	}
	
	private EditTypes type;
	private T data;

	public EditEvent(Object source) {
		this(source, EditTypes.UNKNOWN );
	}

	public EditEvent(Object source, EditTypes type ) {
		this(source, type, null );
	}
	
	public EditEvent(Object source, EditTypes type , T data ) {
		super(source);
		this.type = type;
		this.data = data;
	}

	public EditTypes getType() {
		return type;
	}

	public T getData() {
		return data;
	}
}
