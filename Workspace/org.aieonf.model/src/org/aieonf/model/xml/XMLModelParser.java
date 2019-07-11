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
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.IModelBuilderListener.ModelAttributes;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.xml.IModelParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLModelParser<T extends IDescriptor, M extends IDescribable<T>> extends DefaultHandler implements IModelParser<T,M>{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	
	private Collection<M> models;

	private IModelNode<T>  parent;
	private IModelNode<T>  current;
	
	private Stack<ModelAttributes> stack;
	
	private IXMLModelInterpreter<T,M> interpreter;	
	private Collection<IModelBuilderListener<M>> listeners;

	private Logger logger = Logger.getLogger( XMLModelParser.class.getName() );

	public XMLModelParser( IXMLModelInterpreter<T,M> interpreter) {
		this.stack = new Stack<ModelAttributes>();
		this.interpreter = interpreter;
		this.models = new ArrayList<M>();
		listeners = new ArrayList<IModelBuilderListener<M>>();
	}
	
	protected Stack<ModelAttributes> getStack() {
		return stack;
	}

	public IXMLModelInterpreter<T,M> getCreator() {
		return interpreter;
	}

	protected void setCurrent(XMLModel<T> current) {
		this.current = current;
	}

	protected IModelLeaf<T> getCurrent() {
		return current;
	}

	/**
	 * Get the root factory 
	 * @return
	 */
	public Collection<M> getModels() {
		return models;
	}

	/**
	 * Add a model builder listener
	 * @param event
	 */
	@Override
	public void addModelBuilderListener( IModelBuilderListener<M> listener ){
		this.listeners.add( listener );
	}

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	@Override
	public void removeModelBuilderListener( IModelBuilderListener<M> listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( ModelBuilderEvent<M> event ){
		for( IModelBuilderListener<M> listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Check if the model is well-formed
	 * @param index
	 * @param qName
	 */
	protected void checkModel( ModelAttributes index, String qName ) {
		if( !index.equals( ModelAttributes.MODELS ) && !index.equals( ModelAttributes.CHILDREN))
			throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
	}
	
	protected IModelNode<T> createModel( Attributes attributes ){
		return new XMLModel<T>( attributes );
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
				if( !stack.isEmpty() )
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: 0");
				break;
			case MODELS:
				if( this.stack.isEmpty() )
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: 0");
				break;
			case MODEL:
				index = this.stack.lastElement();
				this.checkModel(index, qName);
				this.current = this.createModel(attributes);
				if( this.parent != null )
					this.parent.addChild( this.current );
				this.models.add( (M) this.current);
				break;
			case CONTEXT:
				index = stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				break;
			case CHILDREN:
				index = this.stack.lastElement();
				if( !index.equals( ModelAttributes.MODEL ))
					throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " The parent is: " + index);
				this.current.setLeaf(false);
				this.parent = this.current;
				break;
			default:
				break; //do nothing
			}
		}else{
			index = this.stack.lastElement();
			switch( index ){
			case MODEL:
				ma = ModelAttributes.DESCRIPTOR;
				this.current.init( interpreter.create( qName, attributes).getDescriptor());
				break;
			case CONTEXT:
			case DESCRIPTOR:
				ma = ModelAttributes.PROPERTIES;
				interpreter.setProperty( StringStyler.prettyString( qName ), attributes);				
				break;
			default:	
				break;
			}
		}
		this.stack.push(ma);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		ModelAttributes ma = this.stack.pop();
		switch( ma ){
		case MODEL:
			this.notifyListeners( new ModelBuilderEvent<M>( this, ma, (M) this.current ));
			break;
		case CHILDREN:
			if(( this.parent != null ) &&( this.parent != current )){
				current = parent;
				parent = (IModelNode<T>) current.getParent();
			}
			break;
		case CONTEXT:
		case DESCRIPTOR:
		case PROPERTIES:
			this.interpreter.endProperty();
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
			logger.fine("Setting value for key: " + interpreter.getKey() );
			IDescriptor descriptor = current.getDescriptor();
			descriptor.set( interpreter.getKey(), value);
		}
		catch( Exception ex ){
			ex.printStackTrace();
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
	protected String printError( IModelLeaf<T> model, String message ){
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
}