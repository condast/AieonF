/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.model.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.aieonf.commons.Utils;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilder;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.xml.XMLModelParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


public class XMLModelBuilder<T extends IDescriptor> implements IModelBuilder<T> {

	protected static final String JAXP_SCHEMA_SOURCE =
		    "http://java.sun.com/xml/jaxp/properties/schemaSource";
	protected static final String JP2P_XSD_SCHEMA = "http://www.condast.com/saight/saight-schema.xsd";

	private static final String S_ERR_NO_SCHEMA_FOUND = "The XML Schema was not found";
	private static final String S_ERR_NO_TEMPLATE_FOUND = "The template is not found: ";
	private static final String S_WRN_NOT_NAMESPACE_AWARE = "The parser is not validating or is not namespace aware";
	
	static final String S_DOCUMENT_ROOT = "DOCUMENT_ROOT";

	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";	
		
	private boolean completed, failed;
	
	private String modelId;
	private IXMLModelBuilder<T, IModelLeaf<T>> builder;

	private Collection<IModelBuilderListener> listeners;
		
	private Logger logger = Logger.getLogger( XMLModelBuilder.class.getName() );
	
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param modelId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public XMLModelBuilder( String modelId, IXMLModelBuilder<T, IModelLeaf<T>> builder ) {
		this.modelId = modelId;
		this.builder = builder;
		this.completed = false;
		this.failed = false;
		this.listeners = new ArrayList<IModelBuilderListener>();
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.factory.ICompositeFactory#addListener(net.osgi.jxta.factory.ICompositeFactoryListener)
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#addListener(net.jp2p.container.builder.IModelBuilderListener)
	 */
	public void addListener( IModelBuilderListener listener ){
		this.listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.factory.ICompositeFactory#removeListener(net.osgi.jxta.factory.ICompositeFactoryListener)
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#removeListener(net.jp2p.container.builder.IModelBuilderListener)
	 */
	public void removeListener( IModelBuilderListener listener ){
		this.listeners.remove( listener);
	}

	/**
	 * Returns true if the url points to a valid resource
	 * @return
	 */
	public boolean canCreate(){
		URL url = this.builder.getURL();
		if( url == null )
			return false;
		try {
			return ( url.openConnection().getContentLengthLong() > 0 );
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#build()
	 */
	@Override
	public IModelLeaf<T> build() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		URL schema_in = XMLModelBuilder.class.getResource( S_SCHEMA_LOCATION); 
		if( schema_in == null )
			throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		Source schemaFile = new StreamSource( XMLModelParser.class.getResourceAsStream( S_SCHEMA_LOCATION ));
		InputStream in;
		URL url = this.builder.getURL();
		try {
			in = url.openStream();
		} catch (Exception e1) {
			logger.severe( S_ERR_NO_TEMPLATE_FOUND + this.builder.getLocation() + "\n");
			e1.printStackTrace();
			return null;
		}
		
		//First parse the XML file
		IModelLeaf<T> root = null;
		try {
			logger.info("Parsing SAIGHT Bundle: " + this.modelId + "\n");
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE + "\n" );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			XMLModelParser<T> parser = new XMLModelParser<T>();
			IModelBuilderListener listener = new IModelBuilderListener(){

				@Override
				public void notifyChange(ModelBuilderEvent event) {
					for( IModelBuilderListener listener: listeners )
						listener.notifyChange(event);
				}	
			};
			parser.addModelBuilderListener(listener);
			parser.addDescriptorCreator( builder);
			saxParser.parse( in, parser );
			parser.removeModelBuilderListener(listener);
			root = parser.getRoot();
			parser.removeDescriptorCreator( builder );
			parser.clear();
			logger.info("AIEONF Bundle Parsed: " + this.modelId + "\n");
		} catch( SAXNotRecognizedException e ){
			failed = true;
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			failed = true;
			e.printStackTrace();
		} catch (SAXException e) {
			failed = true;
			e.printStackTrace();
		} catch (IOException e) {
			failed = true;
			e.printStackTrace();
		}
		finally{
			IOUtils.closeInputStream(in);
		}
		
		this.completed = true;
		return root;
	}

	public boolean complete() {
		this.completed = true;
		return completed;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return completed;
	}

	public boolean hasFailed() {
		return failed;
	}

	public static String getLocation( String defaultLocation ){
		if( !Utils.isNull( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}
}