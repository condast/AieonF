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
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.xml.IModelParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLModelParser<T extends IDescriptor> extends DefaultHandler implements IModelParser{

	public static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	public static final String S_ERR_NO_DESCRIPTOR_FOUND = "No descriptor found for: ";
	public static final String S_WRN_DESCRIPTOR_NOT_FOUND = "The descriptor was not found. Defaulting to standard concept: ";
	
	/**
	 * The keys of attributes with special meaning
	 * @author Kees
	 *
	 */
	public enum ModelAttributes{
		APPLICATION,
		MODEL,
		CONTEXT,
		DESCRIPTOR,
		CHILDREN,
		PROPERTIES;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		/**
		 * Returns true if the given string is a model attribute
		 * @param attr
		 * @return
		 */
		public static boolean isModelAttribute( String attr ){
			String str = StringStyler.styleToEnum( attr );
			if( Utils.isNull(str))
				return false;
			for( ModelAttributes ma: ModelAttributes.values() ){
				if( str.equals( ma.name()))
					return true;
			}
			return false;
		}
	}

	private IModelNode<T> root;

	private IModelLeaf<IDescriptor> parent;
	private IModelLeaf<IDescriptor> current;
	
	private Stack<ModelAttributes> stack;
	
	private Collection<IModelCreator<T,IModelLeaf<T>>> creators;	
	private Collection<IModelBuilderListener> listeners;

	private XMLApplication application;
	private XMLModel xmlModel;
	
	//Allows transformation of the strings to the correct properties
	private IModelCreator<T,IModelLeaf<T>> creator;

	private Logger logger = Logger.getLogger( XMLModelParser.class.getName() );

	public XMLModelParser() {
		this.stack = new Stack<ModelAttributes>();
		this.creators = new ArrayList<IModelCreator<T,IModelLeaf<T>>>();
		listeners = new ArrayList<IModelBuilderListener>();
	}

	public void clear(){
		this.creators.clear();
		this.listeners.clear();
	}
	
	/**
	 * Get the root factory 
	 * @return
	 */
	public IModelLeaf<T> getRoot() {
		return root;
	}
	
	/**
	 * Add a model parser
	 * @param parser
	 */
	public void addDescriptorCreator( IModelCreator<T,IModelLeaf<T>> creator ){
		this.creators.add( creator );
	}

	/**
	 * Add a model parser
	 * @param parser
	 */
	public void removeDescriptorCreator( IModelCreator<T,IModelLeaf<T>> creator ){
		this.creators.remove( creators );
	}


	/**
	 * Add a model builder listener
	 * @param event
	 */
	@Override
	public void addModelBuilderListener( IModelBuilderListener listener ){
		this.listeners.add( listener );
	}

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	@Override
	public void removeModelBuilderListener( IModelBuilderListener listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent event ){
		for( IModelBuilderListener listener: this.listeners )
			listener.notifyChange(event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		//First check for model elements
		String str = StringStyler.styleToEnum(qName);
		ModelAttributes ma = null;
		ModelAttributes index = ModelAttributes.APPLICATION;
		if( ModelAttributes.isModelAttribute( qName )){
			ma = ModelAttributes.valueOf( str );
			switch( ma ){
			case APPLICATION:
				if( !this.stack.isEmpty() )
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: 0");
				application = new XMLApplication();
				application.fill(attributes);
				break;
			case MODEL:
				index = this.stack.lastElement();
				if(( !index.equals( ModelAttributes.APPLICATION )) && ( !index.equals( ModelAttributes.CHILDREN )))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
				xmlModel = new XMLModel();
				xmlModel.fill(attributes);
				break;
			case CONTEXT:
				index = this.stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				this.creator = this.getModelCreator( qName, attributes);
				IModelLeaf<T> model = this.creator.create( qName, attributes);
				ContextAieon ca = (ContextAieon) model.getDescriptor();
				application.extendContext( ca );
				xmlModel.extendContext( ca );
				this.root = (IModelNode<T>) model;
				this.root.setIdentifier( ModelAttributes.CONTEXT.toString());
				this.current = (IModelLeaf<IDescriptor>) model;
				xmlModel.fill( this.current );
				break;
			case CHILDREN:
				index = this.stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				if(!( current instanceof IModelNode ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				//	this.current = new Model<IDescriptor>( this.current.getDescriptor() );
				//	this.completeModel( (ModelLeaf<IDescriptor>) this.current, attributes);
					this.parent = this.current;
				//}
				break;
			default:
				break; //do nothing
			}
		}else{
			index = this.stack.lastElement();
			switch( index ){
			case MODEL:
				ma = ModelAttributes.DESCRIPTOR;
				this.creator = this.getModelCreator( qName, attributes);
				if( this.creator == null )
					throw new IllegalArgumentException( S_ERR_NO_DESCRIPTOR_FOUND + qName );
				IModelLeaf<T> model = creator.create( qName, attributes);
				if( model == null  )
					logger.warning( S_WRN_DESCRIPTOR_NOT_FOUND + qName );
				
				IModelNode<IDescriptor> node = (IModelNode<IDescriptor>) this.current;
				this.current =  (IModelLeaf<IDescriptor>) model;
				xmlModel.fill( this.current );
				String strng = StringStyler.styleToEnum( qName );
				this.current.setIdentifier( StringStyler.prettyString( strng));
				node.addChild( this.current );
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
		this.stack.push(ma);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		ModelAttributes ma = this.stack.pop();
		switch( ma ){
		case MODEL:
			this.notifyListeners( new ModelBuilderEvent( this, this.current ));
			if(( this.parent != null ) &&( this.parent != current )){
				((IModelNode<IDescriptor>) parent).addChild( current );
				current = parent;
			}
			if( this.creator != null )
				this.creator.clear();
			this.creator = null;
			break;
		case CONTEXT:
			this.notifyDescriptorCreated( this.current.getDescriptor());
			break;
		case DESCRIPTOR:
			this.notifyDescriptorCreated( this.current.getDescriptor());
			if( this.creator != null )
				this.creator.clear();
			this.creator = null;
			break;
		case PROPERTIES:
			this.creator.endProperty(ma);
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the correct descriptor from the creators
	 * @param name
	 * @param attributes
	 * @return
	 */
	protected final IModelCreator<T,IModelLeaf<T>> getModelCreator( String name, Attributes attributes ){
		for( IModelCreator<T,IModelLeaf<T>> creator: this.creators ){
			if( creator.canCreate( name, attributes))
				return creator;
		}
		return null;	
	}
	
	private void notifyDescriptorCreated( IDescriptor descriptor ){
		for( IModelCreator<T,IModelLeaf<T>> creator: this.creators ){
			creator.notifyDescriptorCreated( new ModelCreatorEvent(this, descriptor));
		}
	}

		@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if( Utils.isNull( value  ))
			return;
		creator.setValue( value);
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
			if( !Utils.isNull( attributes.getLocalName(i))){
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
			if( !Utils.isNull( attributes.getLocalName(i))){
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
			if(!Utils.isNull( name ))
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
			DOMAIN,
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

		public String getDomain(){
			return properties.get( Fields.DOMAIN.name().toLowerCase());
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
			context.setApplicationDomain( this.getDomain() );
			context.setApplicationName( this.getName() );
			context.setApplicationVersion( this.getVersion() );
			String id = this.getID();
			if( !Utils.isNull(id))
				context.setApplicationID(id);
		}
	}
}