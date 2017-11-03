/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.template.xml;

import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.model.builder.IModelBuilderListener.ModelAttributes;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModel;
import org.aieonf.model.xml.XMLModelParser;
import org.aieonf.model.xml.XMLUtils;
import org.aieonf.template.core.TemplateNode;
import org.aieonf.template.def.ITemplateLeaf;
import org.aieonf.template.def.ITemplateNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLTemplateParser<T extends IDescriptor, M extends IModelLeaf<T>> extends XMLModelParser<T,M>{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	

	private ITemplateNode<T> root;	
	private XMLApplication application;

	public XMLTemplateParser( IXMLModelInterpreter<IDescriptor, T> creator) {
		super( creator );
	}
	
	/**
	 * Get the root factory 
	 * @return
	 */
	public IModelLeaf<T> getRoot() {
		return root;
	}
	
	@Override
	protected void checkModel(ModelAttributes index, String qName) {
		if( !index.equals( ModelAttributes.APPLICATION ) && !index.equals( ModelAttributes.CHILDREN))
			throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
	}

	@Override
	protected IModelNode<T> createModel(Attributes attributes) {
		this.root = new TemplateNode<T>( attributes );
		return this.root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		String str = StringStyler.styleToEnum(qName);
		ModelAttributes ma = null;
		IXMLModelInterpreter<? extends IDescriptor, T> creator = super.getCreator();
		if( !ModelAttributes.isModelAttribute( qName ))
			return;
		ma = ModelAttributes.valueOf( str );
		switch( ma ){
		case APPLICATION:
			application = new XMLApplication();
			application.fill(attributes);
			break;
		case CONTEXT:
			ContextAieon ca = (ContextAieon)creator.create( qName, attributes);
			application.extendContext( ca );
			this.root.init( (T) ca );
			this.root.setIdentifier( ModelAttributes.CONTEXT.toString());
			break;
		default:
			break;
		}
	}
			
	private static class XMLApplication {

		public enum Fields{
			ID,
			NAME,
			VERSION;
			
			@Override
			public String toString() {
				return StringStyler.prettyString( super.toString());
			}	
		}

		private Map<String, String> properties;

		public XMLApplication() {
			super();
			properties = new HashMap<String, String>();
		}
		
		public void fill( Attributes attributes ){
			properties = XMLUtils.convertAttributesToProperties(attributes);
		}

		public String getID(){
			return properties.get( Fields.ID.name().toLowerCase());
		}

		public String getName(){
			return properties.get( Fields.NAME.name().toLowerCase());
		}

		public String getVersion(){
			return properties.get( Fields.VERSION.name().toLowerCase());
		}
		
		/**
		 * Extends the given context aieon
		 * @param context
		 */
		public void extendContext( ContextAieon context ){
			context.setApplicationName( this.getName() );
			context.setApplicationVersion( this.getVersion() );
			String id = this.getID();
			if( !Utils.assertNull(id))
				context.setApplicationID(id);
		}
	}
}