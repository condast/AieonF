/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.filter.xml;

import java.util.EventObject;

import org.aieonf.commons.filter.xml.IFilterBuilderListener.FilterNodes;

public class FilterBuilderEvent<M extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private M model;
	private FilterNodes attribute;
	
	private boolean completed; 

	public FilterBuilderEvent(Object source, FilterNodes attribute, M model, boolean completed ) {
		super( source );
		this.model = model ;
		this.attribute = attribute;
		this.completed = completed;
	}

	public FilterBuilderEvent(Object source, FilterNodes attribute, M model ) {
		this(source, attribute, model, true);
	}

	/**
	 * The model is provided by the listener
	 * @param source
	 * @param attribute
	 */
	public FilterBuilderEvent(Object source, FilterNodes attribute ) {
		this(source, attribute, null, true);
	}

	public FilterNodes getAttribute() {
		return attribute;
	}

	public M getModel() {
		return this.model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public synchronized boolean isCompleted() {
		return completed;
	}
}
