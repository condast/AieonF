/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.template.xml;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.template.ITemplateLeaf;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModelBuilder;
import org.aieonf.template.xml.XMLTemplateParser;

public class XMLTemplateBuilder<C extends IContextAieon, T extends ITemplateLeaf<C>> implements IModelBuilder<T>{

	private XMLModelBuilder<C,T> builder;
	private boolean completed;
	
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param domainId
	 * @param clss
	 * @param location
	 * @param interpreter
	 */
	public XMLTemplateBuilder( String domainId, IXMLModelInterpreter<C,T> interpreter ) {
		builder = new XMLModelBuilder<C,T>( new XMLTemplateParser<C,T>( interpreter ), domainId, interpreter );
		this.completed = false;
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param domainId
	 * @param clss
	 * @param location
	 * @param interpreter
	 */
	public XMLTemplateBuilder( String domainId, ITemplateParser<C,T> parser ) {
		builder = new XMLModelBuilder<C,T>( parser, domainId, parser.getCreator() );
		this.completed = false;
	}

	@Override
	public void build() {
		builder.build();
		this.completed = true;
	}

	@Override
	public T getModel() {
		return builder.getModel().iterator().next();
	}

	@Override
	public boolean isCompleted() {
		return this.completed;
	}
}