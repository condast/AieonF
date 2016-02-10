/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.builder;

import java.util.EventObject;

import org.aieonf.model.IModelLeaf;

public class ModelBuilderEvent extends EventObject {
	private static final long serialVersionUID = -1266257260044093122L;

	private IModelLeaf<?> model;
	
	public ModelBuilderEvent(Object source, IModelLeaf<?> container) {
		super(source);
		this.model = container;
	}

	public IModelLeaf<?> getModel() {
		return model;
	}
}
