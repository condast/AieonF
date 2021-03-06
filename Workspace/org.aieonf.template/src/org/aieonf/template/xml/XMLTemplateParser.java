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

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.model.builder.IModelBuilderListener.ModelAttributes;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.aieonf.model.xml.XMLModelParser;
import org.aieonf.model.xml.XMLUtils;
import org.aieonf.template.core.TemplateNode;
import org.aieonf.template.def.ITemplateNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLTemplateParser<D extends IDescriptor, M extends IModelLeaf<D>> extends XMLModelParser<D,M>{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	
	private ITemplateNode<D> root;	
	private XMLApplication application;

	public XMLTemplateParser( IXMLModelInterpreter<D> creator) {
		super( creator );
	}
	
	/**
	 * Get the root factory 
	 * @return
	 */
	public IModelLeaf<D> getRoot() {
		return root;
	}
	
	@Override
	protected void checkModel(ModelAttributes index, String qName) {
		if( !index.equals( ModelAttributes.APPLICATION ) && !index.equals( ModelAttributes.CHILDREN) && !index.equals( ModelAttributes.MODELS ))
			throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IModelNode<IDescriptor> createModel(Attributes attributes) {
		this.root = new TemplateNode<D>( attributes );
		return (IModelNode<IDescriptor>) this.root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		String str = StringStyler.styleToEnum(qName);
		IXMLModelInterpreter<D> creator = super.getCreator();
		if( !ModelAttributes.isModelAttribute( qName ))
			return;
		ModelAttributes ma = ModelAttributes.valueOf( str );
		switch( ma ){
		case APPLICATION:
			application = new XMLApplication();
			application.fill(attributes);
			break;
		case CONTEXT:
			ContextAieon ca = (ContextAieon)creator.create( qName, attributes);
			application.extendContext( ca );
			this.root.setData( (D) ca );
			this.root.setDescriptorId( ModelAttributes.CONTEXT.toString());
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
			if( !StringUtils.isEmpty(id))
				context.setApplicationID(id);
		}
	}
}