/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.filter.xml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.filter.FilterChain;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.xml.IFilterBuilderListener.FilterNodes;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLFilterParser<D extends Object> extends DefaultHandler{

	private static final String S_ERR_MALFORMED_XML = "The XML code is malformed at: ";
	
	private Collection<IFilter<D>> filters;

	private IFilter<D>  parent;
	private IFilter<D>  current;
	
	private Stack<FilterNodes> stack;
	
	private Collection<IFilterBuilderListener<IFilter<D>>> listeners;

	private Logger logger = Logger.getLogger( XMLFilterParser.class.getName() );

	public XMLFilterParser( ) {
		this.stack = new Stack<>();
		this.filters = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	
	protected synchronized Stack<FilterNodes> getStack() {
		return stack;
	}

	
	protected IFilter<D> getParent() {
		return parent;
	}

	protected synchronized IFilter<D> getCurrent() {
		return current;
	}

	/**
	 * Get the root factory 
	 * @return
	 */
	public Collection<IFilter<D>> getFilters() {
		return filters;
	}

	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelBuilderListener( IFilterBuilderListener<IFilter<D>> listener ){
		this.listeners.add( listener );
	}

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelBuilderListener( IFilterBuilderListener<IFilter<D>> listener ){
		this.listeners.remove( listener );
	}

	protected final void notifyListeners( FilterBuilderEvent<IFilter<D>> event ){
		for( IFilterBuilderListener<IFilter<D>> listener: this.listeners )
			listener.notifyChange(event);
	}

	/**
	 * Check if the model is well-formed
	 * @param index
	 * @param qName
	 */
	protected void checkModel( FilterNodes index, String qName ) {
		if( !index.equals( FilterNodes.FILTERS ) && !index.equals( FilterNodes.FILTER))
			throw new IllegalArgumentException( S_ERR_MALFORMED_XML + qName + " index: " + index);
	}
		
	@Override
	public synchronized void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		//First check for model elements
		String str = StringStyler.styleToEnum(qName);
		FilterNodes node = null;
		FilterNodes index = FilterNodes.FILTERS;
		String typestr = getAttributeValue(IFilterBuilderListener.FilterAttributes.TYPE.name().toLowerCase(), attributes);
		IFilterBuilderListener.FilterTypes type = StringUtils.isEmpty(typestr)?IFilterBuilderListener.FilterTypes.AND:
			IFilterBuilderListener.FilterTypes.valueOf(StringStyler.styleToEnum(typestr));
		if( FilterNodes.isNode( qName )){
			node = FilterNodes.valueOf( str );
			switch( node ){
			case FILTERS:
				break;
			case CHAIN:
				switch( type) {
				case AND:
					this.current = new FilterChain<D>( FilterChain.Rules.AND_CHAIN);
					break;
				case OR:
					this.current = new FilterChain<D>( FilterChain.Rules.OR_CHAIN);
					break;
				default:
					break;
				}
				break;
			case FILTER:
				switch( type) {
				case AND:
					this.current = new FilterChain<D>( FilterChain.Rules.AND_CHAIN);
					break;
				case OR:
					this.current = new FilterChain<D>( FilterChain.Rules.OR_CHAIN);
					break;
				default:
					break;
				}
				index = this.stack.lastElement();
				/*
				String location = getAttributeValue(IFilterBuilderListener.FilterAttributes.LOCATION.name().toLowerCase(), attributes);
				if( StringUtils.isEmpty(location)) {
					this.checkModel(index, qName);
					this.current = this.createModel(attributes);
				}else 
					this.notifyListeners( new FilterBuilderEvent<IFilter<D>>( this, FilterAttributes.LOCATION, (M) this.current ));
				if( this.parent != null )
					this.parent.addChild( this.current );
					*/
				this.filters.add( this.current);
				break;
			default:
				break; //do nothing
			}
		}else{
			index = this.stack.lastElement();
			switch( index ){
			case FILTER:
				break;
			case CHAIN:
			default:	
				break;
			}
		}
		this.stack.push(node);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		FilterNodes ma = this.stack.pop();
		switch( ma ){
		case FILTER:
			this.notifyListeners( new FilterBuilderEvent<IFilter<D>>( this, ma, this.current ));
			break;
		case CHAIN:
			//this.interpreter.endProperty();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if( StringUtils.isEmpty( value  ))
			return;
		try{
			logger.fine("Setting value for key: " );
			if( StringUtils.isEmpty(value))
				return;
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
				x.getLineNumber(),
				x.getColumnNumber(),
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
	protected String printError( IFilter<D> model, String message ){
		StringBuffer buffer = new StringBuffer();
		buffer.append("ERROR: ");
		buffer.append( message );
		buffer.append("\n");
		buffer.append( model.toString());
		return buffer.toString();
	}
	
	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	public static <D> void fillAttributes( IFilter<D> filter, Attributes attributes ){
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				String str = StringStyler.styleToEnum( attributes.getLocalName( i ));
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
			if( !StringUtils.isEmpty( attributes.getLocalName(i))){
				attrs.put( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return attrs;
	}
}