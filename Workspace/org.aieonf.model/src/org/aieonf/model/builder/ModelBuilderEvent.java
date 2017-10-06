/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.builder;

import java.util.EventObject;

import org.aieonf.model.builder.IModelBuilderListener.ModelAttributes;

public class ModelBuilderEvent<M extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private M model;
	private ModelAttributes attribute;
	
	private boolean completed; 

	public ModelBuilderEvent(Object source, ModelAttributes attribute, M model, boolean completed ) {
		super( source );
		this.model = model ;
		this.attribute = attribute;
		this.completed = completed;
	}

	public ModelBuilderEvent(Object source, ModelAttributes attribute, M model ) {
		this(source, attribute, model, true);
	}

	/**
	 * The model is provided by the listener
	 * @param source
	 * @param attribute
	 */
	public ModelBuilderEvent(Object source, ModelAttributes attribute ) {
		this(source, attribute, null, true);
	}

	public ModelAttributes getAttribute() {
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
