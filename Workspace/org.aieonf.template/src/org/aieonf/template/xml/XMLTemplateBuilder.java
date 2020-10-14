/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.template.xml;

import java.io.InputStream;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.xml.IModelInterpreterFactory;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModelBuilder;

public class XMLTemplateBuilder<D extends IDescriptor, M extends IModelLeaf<D>> implements IModelBuilder<IModelLeaf<D>>{

	private XMLModelBuilder<D,M> builder;
	private IModelInterpreterFactory<D> factory;
	private String domainId;
	private boolean completed;
	
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param domainId
	 * @param clss
	 * @param location
	 * @param interpreter
	 */
	public XMLTemplateBuilder( String domainId, IModelInterpreterFactory<D> factory, String resource ) {
		this.factory = factory;
		this.domainId = domainId;
		InputStream in = factory.createInputStream(resource);
		builder = createBuilder(domainId, factory, in);
		builder.addModelBuilderListener( e->notifyBuildEvent( e ));
		this.completed = false;
	}

	@SuppressWarnings("unchecked")
	protected void notifyBuildEvent( ModelBuilderEvent<M> event ) {
		switch( event.getAttribute()) {
		case LOCATION:
			InputStream in = factory.createInputStream( IModelBuilder.S_DEFAULT_MODEL_LOCATION);
			XMLModelBuilder<D,M> mbuilder = createBuilder(domainId, factory, in );
			mbuilder.build();
			IModelNode<D> model =  (IModelNode<D>) event.getModel();
			for( IModelLeaf<D> child: mbuilder.getModel())
				model.addChild( child );
			break;
		default:
			break;
		}
	}
	
	@Override
	public void build() {
		builder.build();
		this.completed = true;
	}

	@Override
	public IModelLeaf<D> getModel() {
		return builder.getModel().iterator().next();
	}

	@Override
	public boolean isCompleted() {
		return this.completed;
	}
	
	protected static <D extends IDescriptor, M extends IModelLeaf<D>> XMLModelBuilder<D, M> createBuilder( String domainId,  IModelInterpreterFactory<D> factory, InputStream in ) {
		IXMLModelInterpreter<D> interpreter = factory.createInterpreter( in );
		return new XMLModelBuilder<D, M>( new XMLTemplateParser<D,M>( interpreter), domainId, interpreter );
	}
}