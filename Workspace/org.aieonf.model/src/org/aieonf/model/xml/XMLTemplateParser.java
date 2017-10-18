/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.xml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.IModelBuilderListener.ModelAttributes;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.Model;
import org.aieonf.model.xml.IModelParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLTemplateParser<T extends IDescriptor, M extends IModelLeaf<T>> extends XMLModelParser<T,M>{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	private static final String S_ERR_NO_CHILDREN   = " The node cannot contain children. ";
	private static final String S_WRN_DESCRIPTOR_NOT_FOUND = "The descriptor was not found. Defaulting to standard concept: ";
	

	private IModelNode<T> root;	
	private XMLApplication application;
	private XMLModel xmlModel;
	
	private Logger logger = Logger.getLogger( XMLTemplateParser.class.getName() );

	@SuppressWarnings("unchecked")
	public XMLTemplateParser( IXMLModelInterpreter<T, IModelLeaf<T>> creator) {
		super( (IXMLModelInterpreter<IDescriptor, T>) creator );
	}
	
	/**
	 * Get the root factory 
	 * @return
	 */
	public IModelLeaf<T> getRoot() {
		return root;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		//First check for model elements
		String str = StringStyler.styleToEnum(qName);
		ModelAttributes ma = null;
		ModelAttributes index = ModelAttributes.APPLICATION;
		Stack<ModelAttributes> stack = super.getStack();
		IXMLModelInterpreter<IDescriptor, T> creator = super.getCreator();
		IModelLeaf<T> current = super.getCurrent();
		IModelLeaf<T> parent = super.getCurrent();
		if( ModelAttributes.isModelAttribute( qName )){
			ma = ModelAttributes.valueOf( str );
			switch( ma ){
			case APPLICATION:
				if( !stack.isEmpty() )
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: 0");
				application = new XMLApplication();
				application.fill(attributes);
				break;
			case MODELS:
				this.root = new Model<T>( (T) new Descriptor( ));
				current = this.root;
				break;
			case MODEL:
				index = stack.lastElement();
				if( !index.equals( ModelAttributes.APPLICATION ) && 
						!index.equals( ModelAttributes.CHILDREN ) &&
						!index.equals( ModelAttributes.MODELS ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
				xmlModel = new XMLModel();
				xmlModel.fill(attributes);
				break;
			case CONTEXT:
				index = stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				T model = creator.create( qName, attributes);
				ContextAieon ca = (ContextAieon) model.getDescriptor();
				application.extendContext( ca );
				xmlModel.extendContext( ca );
				this.root = model;
				this.root.setIdentifier( ModelAttributes.CONTEXT.toString());
				super.setCurrent(  model );
				xmlModel.fill( current );
				break;
			case CHILDREN:
				index = stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				if(!( current instanceof IModelLeaf ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
					super.setParent( (org.aieonf.model.xml.XMLModel<T>) current );
				break;
			default:
				break; //do nothing
			}
		}else{
			index = stack.lastElement();
			switch( index ){
			case MODEL:
				ma = ModelAttributes.DESCRIPTOR;
				IModelLeaf<T> model = creator.create( qName, attributes);
				if( model == null  )
					logger.warning( S_WRN_DESCRIPTOR_NOT_FOUND + qName );
				if( current == null ){
					this.root = (IModelNode<T>) model;
					this.root.setIdentifier( ModelAttributes.CONTEXT.toString());
					
				}else if(!( current instanceof IModelNode ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + this.current.toString() + 
							" index: " + index + S_ERR_NO_CHILDREN );
					
				IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) this.current;
				current =  (IModelLeaf<T>) model;
				xmlModel.fill( current );
				String strng = StringStyler.styleToEnum( qName );
				current.setIdentifier( StringStyler.prettyString( strng));
				node.addChild( current );
				break;
			case CONTEXT:
			case DESCRIPTOR:
				ma = ModelAttributes.PROPERTIES;
				creator.setProperty( StringStyler.prettyString( qName ), attributes);				
				break;
			case PROPERTIES:
				creator.setProperty( StringStyler.prettyString( qName ), attributes);
				break;
			default:	
				throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
			}
		}
		stack.push(ma);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		Stack<ModelAttributes> stack = super.getStack();
		ModelAttributes ma = stack.pop();
		IXMLModelInterpreter<IDescriptor, T> creator = super.getCreator();
		IModelLeaf<T> current = super.getCurrent();
		IModelLeaf<T> parent = super.getCurrent();
		switch( ma ){
		case MODEL:
			this.notifyListeners( new ModelBuilderEvent<M>( this, ma, (M) current ));
			if(( parent != null ) &&( parent != current )){
				((IModelNode<IDescriptor>) parent).addChild( current );
				super.setCurrent( (org.aieonf.model.xml.XMLModel<T>) parent );
			}
			break;
		case CONTEXT:
			break;
		case DESCRIPTOR:
			creator.endProperty();
			break;
		case PROPERTIES:
			creator.endProperty();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if( Utils.assertNull( value  ))
			return;
		IXMLModelInterpreter<IDescriptor, T> creator = super.getCreator();
		IModelLeaf<T> current = super.getCurrent();
		IModelLeaf<T> parent = super.getCurrent();
		try{
			logger.fine("Setting value for key: " + creator.getKey() );
			IDescriptor descriptor = current.getDescriptor();
			descriptor.set( creator.getKey(), value);
		}
		catch( Exception ex ){
			logger.severe( printError( current, "Exception thrown for [" + value + "]"));
			throw new SAXException( ex );
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		print(e);
		super.error(e);
	}

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.fatalError(arg0);
	}

	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.warning(arg0);
	}


	private MessageFormat message =
		      new MessageFormat("({0}: {1}, {2}): {3}");
	
	private void print(SAXParseException x)
	{
		String msg = message.format(new Object[]
				{
				x.getSystemId(),
				new Integer(x.getLineNumber()),
				new Integer(x.getColumnNumber()),
				x.getMessage()
				});
		Logger.getLogger( this.getClass().getName()).info(msg);
	}

	
	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static void fillAttributes( IModelLeaf<IDescriptor> leaf, Attributes attributes ){
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.assertNull( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
				IModelLeaf.Attributes attr = IModelLeaf.Attributes.valueOf( str );
				leaf.set( attr, attributes.getValue(i));
			}
		}
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static String getAttributeValue( String key, Attributes attributes ){
		String id = StringStyler.xmlStyleString( key );
		return attributes.getValue( id );
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static Map<String, String> convertAttributes( Attributes attributes ){
		Map<String,String> attrs = new HashMap<String,String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.assertNull( attributes.getLocalName(i))){
				attrs.put( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return attrs;
	}

	private static class XMLModel {

		private Map<String, String> properties;

		public XMLModel() {
			super();
			properties = new HashMap<String, String>();
		}
		
		public void fill( Attributes attributes ){
			properties = XMLUtils.convertAttributesToProperties(attributes);
			String name = getName();
			if(!Utils.assertNull( name ))
				properties.put( IModelLeaf.Attributes.IDENTIFIER.name().toLowerCase(), getName());
		}
		
		
		public String getID(){
			return properties.get( IDescriptor.Attributes.ID.name().toLowerCase());
		}

		public String getName(){
			return properties.get( IDescriptor.Attributes.NAME.name().toLowerCase());
		}

		/**
		 * Extends the given context aieon
		 * @param context
		 */
		public void extendContext( ContextAieon context ){
			context.setContext( this.getName() );
			context.set( IDescriptor.Attributes.ID.toString(), this.getID() );
		}
		
		public void fill( IModelLeaf<IDescriptor> leaf ){
			Iterator<Map.Entry<String, String>> iterator = this.properties.entrySet().iterator();
			
			while( iterator.hasNext() ){
				Map.Entry<String, String> entry = iterator.next();
				IModelLeaf.Attributes attr = IModelLeaf.Attributes.valueOf( StringStyler.styleToEnum( entry.getKey() ));
				leaf.set(attr, entry.getValue() );
			}
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