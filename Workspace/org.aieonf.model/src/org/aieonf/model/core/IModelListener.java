/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.core;

public interface IModelListener<M extends Object> {
	
	/**
	 * Notify relevant events duribng the build process 
	 * @param event
	 */
	public void notifyChange( ModelEvent<M> event );
}
