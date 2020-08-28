/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.xml;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.logging.Logger;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.xml.IBuildListener.BuildEvents;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

//
public abstract class AbstractSimpleXmlHandler<T extends Object, E extends Object> extends DefaultHandler implements IXMLHandler<T>{

	private enum AttributeNames{
		ID,
		NAME,
		CLASS,
		TYPE,
		ACTIVE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public static final int MAX_COUNT = 200;	

	private Stack<Node<E,T>> stored;
	private Collection<T> units;

	private Collection<IBuildListener<T>> listeners;
	
	protected AbstractSimpleXmlHandler() {
		stored = new Stack<Node<E,T>>();
		units = new ArrayList<T>();
		this.listeners = new ArrayList<IBuildListener<T>>();
	}

	@Override
	public void addListener( IBuildListener<T> listener ){
		this.listeners.add( listener);
	}

	@Override
	public void removeListener( IBuildListener<T> listener ){
		this.listeners.remove( listener);
	}
	
	void notifyListeners( BuildEvent<T> event ){
		for( IBuildListener<T> listener: this.listeners )
			listener.notifyTestEvent(  event );
	}
		
	/**
	 * Parse the given node
	 * @param node
	 * @param attributes
	 */
	protected abstract T parseNode( E node, Attributes attributes );
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		String componentName = StringStyler.styleToEnum( qName );
		String id = getAttribute(attributes, AttributeNames.ID);
		E enm = getNode( componentName );
		if( enm == null )
			return;

		T unit = this.parseNode( enm, attributes);
		if( unit != null )
			this.units.add( unit);
		
		Node<E,T> node = null;
		if( stored.isEmpty())
			node = new Node<E,T>( id, enm, unit, attributes );
		else{
			if( StringUtils.isEmpty(id))
				id = stored.peek().getID(); 
			node = new Node<E,T>( id, stored.peek(), enm, unit, attributes ); 
		}
		stored.push( node );
		notifyListeners( new BuildEvent<T>( this, id, componentName, BuildEvents.PREPARE.name(), attributes, unit ));
	}
	
	/**
	 * Get the parent of the node that is currently processed
	 * @return
	 */
	protected Node<E,T> getParent(){
		return stored.lastElement();		
	}
	
	protected E getCurrent(){
		return stored.lastElement().key;
	}
	
	protected Object getCurrentData(){
		return ( stored.isEmpty() )? null: stored.lastElement().data;
	}
	
	/**
	 * completion just before a node is parsed
	 * @param node
	 */
	protected abstract void completeNode( E node );

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		String componentName = StringStyler.styleToEnum( qName );
		E node = getNode( componentName );
		if( node == null )
			return;
		this.completeNode(node);
		Node<E,T> unit = stored.pop();
		notifyListeners( new BuildEvent<T>( this, componentName, BuildEvents.COMPLETE.name(), unit.getData()));
	}

	/**
	 * Add a value to the given node
	 * @param node
	 * @param value
	 */
	protected abstract void addValue( E node, String value );

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if( StringUtils.isEmpty( value ))
			return;
		E current = stored.lastElement().key;
		this.addValue(current, value);
	}

	private void print(SAXParseException x)
	{
		Logger.getLogger( this.getClass().getName()).info(x.getMessage());
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
			
	protected abstract E getNode( String name );
	
	protected Collection<T> getResults() {
		return units;
	}

	@SuppressWarnings("unchecked")
	protected Object createObject( Class<?> clss, String className){
		if( StringUtils.isEmpty( className ))
			return null;
		Class<Object> builderClass;
		Object builder = null;
		try {
			builderClass = (Class<Object>) clss.getClassLoader().loadClass( className );
			Constructor<?> constructor = builderClass.getConstructor();
			builder = constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	protected static class Node<E extends Object, T extends Object>{
		private String id;
		private Node<E, T> parent;
		private E key;
		private T data;
		private Attributes attributes;

		Node( String id, E key, T data, Attributes attributes) {
			this( id, null, key, data, attributes );
		}

		Node( String id, Node<E, T> parent, E key, T data, Attributes attributes) {
			super();
			this.id = id;
			this.parent = parent;
			this.key = key;
			this.data = data;
			this.attributes = attributes;
		}

		public String getID() {
			return id;
		}

		public Node<E, T> getParent() {
			return parent;
		}

		public E getKey(){
			return key;
		}
		
		public T getData() {
			return data;
		}
		
		public String getAttribute( Enum<?> enm ) {
			return AbstractSimpleXmlHandler.getAttribute( this.attributes, enm );
		}
	}
	
	/**
	 * Get the attribute value for the given enum
	 * @param attributes
	 * @param enm
	 * @return
	 */
	protected static String getAttribute( Attributes attributes, Enum<?> enm ){
		return attributes.getValue( StringStyler.xmlStyleString( enm.name() ));
	}
}