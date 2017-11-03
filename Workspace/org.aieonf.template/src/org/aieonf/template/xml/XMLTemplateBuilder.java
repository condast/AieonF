/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.template.xml;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.xml.XMLTemplateParser;

public class XMLTemplateBuilder<T extends IDescriptor, M extends IModelLeaf<T>> implements IModelBuilder<IModelLeaf<T>>{

	private XMLModelBuilder<T,M> builder;
	private boolean completed;
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param domainId
	 * @param clss
	 * @param location
	 * @param interpreter
	 */
	public XMLTemplateBuilder( String domainId, IXMLModelInterpreter<IDescriptor, T> interpreter ) {
		builder = new XMLModelBuilder<T,M>( new XMLTemplateParser<T,M>( interpreter ), domainId, interpreter );
		this.completed = false;
	}

	@Override
	public void build() {
		builder.build();
		this.completed = true;
	}

	@Override
	public IModelLeaf<T> getModel() {
		return builder.getModel().iterator().next();
	}

	@Override
	public boolean isCompleted() {
		return this.completed;
	}
}