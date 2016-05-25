/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

public class ModelBuilderEvent<M extends Object> extends EventObject {
	private static final long serialVersionUID = -1266257260044093122L;

	private Collection<M> models;
	
	private boolean completed; 

	/**
	 * Default: Represents a general failure
	 * @param source
	 */
	public ModelBuilderEvent(Object source ) {
		this( source, null, false );
	}

	public ModelBuilderEvent(Object source, M model ) {
		super( source );
		models = new ArrayList<M>();
		models.add(  model );
		this.completed = true;
	}

	public ModelBuilderEvent(Object source, Collection<M> models ) {
		this( source, models, (models != null ));
	}
	
	public ModelBuilderEvent(Object source, Collection<M> models, boolean completed ) {
		super(source);
		this.models = models;
		this.completed = completed;
	}

	public Collection<M> getModel() {
		return models;
	}

	public synchronized boolean isCompleted() {
		return completed;
	}
}
