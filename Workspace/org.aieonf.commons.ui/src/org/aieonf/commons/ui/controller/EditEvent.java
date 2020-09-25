package org.aieonf.commons.ui.controller;

import java.util.EventObject;

public class EditEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private T data;

	public EditEvent(Object source) {
		this(source, null );
	}

	public EditEvent(Object source, T data ) {
		super(source);
		this.data = data;
	}

	public T getData() {
		return data;
	}
}
