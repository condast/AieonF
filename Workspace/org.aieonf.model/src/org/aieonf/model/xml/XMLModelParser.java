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
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.ContextAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.core.ModelLeaf;
import org.aieonf.model.xml.IModelParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLModelParser<T extends IDescriptor, M extends IModelLeaf<T>> extends DefaultHandler implements IModelParser<IDescriptor, IModelLeaf<IDescriptor>>{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	private static final String S_ERR_NO_EXTENDER   = " No extender found for: ";
	private static final String S_ERR_NO_CHILDREN   = " The node cannot contain children. ";
	private static final String S_WRN_DESCRIPTOR_NOT_FOUND = "The descriptor was not found. Defaulting to standard concept: ";
	
	private IModelNode<T> root;

	private IModelLeaf<IDescriptor> parent;
	private M current;
	
	private Stack<IModelBuilderListener.ModelAttributes> stack;
	
	private IXMLModelInterpreter<T, M> creator;	
	private Collection<IModelBuilderListener<IModelLeaf<IDescriptor>>> listeners;
	
	private XMLApplication application;
	private XMLModel xmlModel;
	
	private Logger logger = Logger.getLogger( XMLModelParser.class.getName() );

	public XMLModelParser( IXMLModelInterpreter<T, M> creator ) {
		this.stack = new Stack<IModelBuilderListener.ModelAttributes>();
		this.creator = creator;
		listeners = new ArrayList<IModelBuilderListener<IModelLeaf<IDescriptor>>>();
	}

	/**
	 * Get the root factory 
	 * @return
	 */
	public IModelLeaf<T> getRoot() {
		return root;
	}

	@Override
	public void addModelBuilderListener(IModelBuilderListener<IModelLeaf<IDescriptor>> listener) {
		this.listeners.add( listener );
	}

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	@Override
	public void removeModelBuilderListener( IModelBuilderListener<IModelLeaf<IDescriptor>> listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyBuilderListeners( ModelBuilderEvent<IModelLeaf<IDescriptor>> event ){
		for( IModelBuilderListener<IModelLeaf<IDescriptor>> listener: this.listeners ) {
			listener.notifyChange(event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		IModelBuilderListener.ModelAttributes ma = null;

		//First check for model elements
		String str = StringStyler.styleToEnum( qName );
		IModelBuilderListener.ModelAttributes attr = IModelBuilderListener.ModelAttributes.APPLICATION;
		attr = ( this.stack.isEmpty())? null : this.stack.lastElement();
		if( IModelBuilderListener.ModelAttributes.isModelAttribute( qName )){
			ma = IModelBuilderListener.ModelAttributes.valueOf( str );
			switch( ma ){
			case APPLICATION:
				if( !this.stack.isEmpty() )
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: 0");
				application = new XMLApplication();
				application.fill(attributes);
				break;
			case MODEL:
				if(( !attr.equals( IModelBuilderListener.ModelAttributes.APPLICATION )) && ( !attr.equals( IModelBuilderListener.ModelAttributes.CHILDREN )))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + attr);
				xmlModel = new XMLModel();
				xmlModel.fill(attributes);
				break;
			case CONTEXT:
				if( !attr.equals( IModelBuilderListener.ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + attr);
				IModelLeaf<T> model = this.creator.create( qName, attributes);
				ContextAieon ca = (ContextAieon) model.getDescriptor();
				application.extendContext( ca );
				xmlModel.extendContext( ca );
				this.root = (IModelNode<T>) model;
				this.root.setIdentifier( IModelBuilderListener.ModelAttributes.CONTEXT.toString());
				this.current = (M) model;
				xmlModel.fill(( ModelLeaf<IDescriptor>) this.current );
				break;
			case CHILDREN:
				if( !attr.equals( IModelBuilderListener.ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + attr);
				if(!( current instanceof IModelLeaf ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + attr);
				this.parent = (IModelLeaf<IDescriptor>) this.current;
				break;
			default:
				break; //do nothing
			}
		}else{
			if(!this.creator.isValid(qName))
				throw new IllegalArgumentException( S_ERR_NO_EXTENDER + qName );
			switch( attr ){
			case MODEL:
				ma = IModelBuilderListener.ModelAttributes.DESCRIPTOR;
				IModelLeaf<T> model = creator.create( qName, attributes);
				if( model == null  ) {
					ModelBuilderEvent<IDescribable<?>> event = new ModelBuilderEvent<IDescribable<?>>( this, ma );
					model = (IModelLeaf<T>) event.getModel();
					logger.warning( S_WRN_DESCRIPTOR_NOT_FOUND + qName );
				}

				if(!( this.current instanceof IModelNode ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + this.current.toString() + 
							" index: " + attr + S_ERR_NO_CHILDREN );

				IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) this.current;
				this.current =  (M) model;
				xmlModel.fill(( ModelLeaf<IDescriptor>) this.current );
				String strng = StringStyler.styleToEnum( qName );
				this.current.setIdentifier( StringStyler.prettyString( strng));
				node.addChild( this.current );
				break;
			case CONTEXT:
				ma = IModelBuilderListener.ModelAttributes.PROPERTIES;
				creator.setProperty( StringStyler.prettyString( qName ), attributes);				
				break;
			case DESCRIPTOR:
				ma = IModelBuilderListener.ModelAttributes.PROPERTIES;
				M created = creator.getModel();
				creator.setProperty( StringStyler.prettyString( qName ), attributes);				
				break;
			case PROPERTIES:
				creator.setProperty( StringStyler.prettyString( qName ), attributes);
				break;
			default:	
				throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + attr);
			}
		}
		this.stack.push( ma );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		IModelBuilderListener.ModelAttributes ma = null;
		ma = this.stack.pop();
		switch( ma ){
		case MODEL:
			if(( this.parent != null ) &&( this.parent != current )){
				((IModelNode<IDescriptor>) parent).addChild( current );
				current =  (M) parent;
			}
			break;
		case CONTEXT:
			break;
		case DESCRIPTOR:
			this.creator.endProperty();
			break;
		case PROPERTIES:
			this.creator.endProperty();
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
		try{
			logger.fine("Setting value for key: " + creator.getKey() );
			IDescriptor descriptor = current.getDescriptor();
			descriptor.set( creator.getKey(), value);
		}
		catch( Exception ex ){
			logger.severe( printError( this.current, "Exception thrown for [" + value + "]"));
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
	 * Print errors in the given model
	 * @param model
	 * @param message
	 * @return
	 */
	private String printError( M model, String message ){
		StringBuffer buffer = new StringBuffer();
		buffer.append("ERROR: ");
		buffer.append( message );
		buffer.append("\n");
		buffer.append( StoreModel.printModel(model, false));
		return buffer.toString();
	}

	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static void fillAttributes( ModelLeaf<IDescriptor> leaf, Attributes attributes ){
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

		public void fill( ModelLeaf<IDescriptor> leaf ){
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