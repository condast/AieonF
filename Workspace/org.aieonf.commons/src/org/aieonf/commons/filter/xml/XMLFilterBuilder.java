/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.filter.xml;

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
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.io.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

public class XMLFilterBuilder<D extends Object> {

	public static String S_DEFAULT_FOLDER = "/AIEONF-INF";
	public static String S_DEFAULT_LOCATION = S_DEFAULT_FOLDER + "/filters.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/filters-schema.xsd";
	public static String S_DEFAULT_FILTER_LOCATION = S_DEFAULT_FOLDER + "/filters.xml";

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
	
	private String identifier;

	private Collection<IFilterBuilderListener<IFilter<D>>> listeners;
	
	private XMLFilterParser<D> parser;
	
	//First parse the XML file
	private Collection<IFilter<D>> filters = null;
	
	private Class<?> clss;
	
	private Logger logger = Logger.getLogger( XMLFilterBuilder.class.getName() );
	
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param identifier
	 * @param clss
	 * @param location
	 * @param interpreter
	 */
	public XMLFilterBuilder( XMLFilterParser<D> parser, Class<?> clss, String identifier ) {
		this.identifier = identifier;
		this.clss = clss;
		this.parser = parser;
		this.completed = false;
		this.failed = false;
		this.listeners = new ArrayList<IFilterBuilderListener<IFilter<D>>>();
	}

	public void addModelBuilderListener( IFilterBuilderListener<IFilter<D>> listener ){
		this.listeners.add( listener );
	}

	public void removeodelBuilderListener( IFilterBuilderListener<IFilter<D>> listener ){
		this.listeners.remove( listener );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#build()
	 */
	@SuppressWarnings("unused")
	public void build() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		URL schema_in = XMLFilterBuilder.class.getResource( XMLFilterBuilder.S_SCHEMA_LOCATION); 
		if( schema_in == null )
			throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		Source schemaFile = new StreamSource(clss.getResourceAsStream( S_SCHEMA_LOCATION ));
		InputStream in = null;
		try {
			in = clss.getResourceAsStream(S_DEFAULT_LOCATION);
		} catch (Exception e1) {
			logger.severe( S_ERR_NO_TEMPLATE_FOUND + S_DEFAULT_LOCATION + "\n");
			e1.printStackTrace();
		}
		
		try {
			logger.info("Parsing SAIGHT Bundle: " + this.identifier + "\n");
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE + "\n" );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			for( IFilterBuilderListener<IFilter<D>> listener: this.listeners )
				parser.addModelBuilderListener( listener );
			saxParser.parse( in, parser );
			filters = parser.getFilters();
			for( IFilterBuilderListener<IFilter<D>> listener: this.listeners )
				parser.removeModelBuilderListener( listener );
			logger.info("AIEONF Bundle Parsed: " + this.identifier + "\n");
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
		} catch (Exception e) {
			failed = true;
			e.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly(in);
		}
		
		this.completed = true;
	}
	
	public Collection<IFilter<D>> getFilters() {
		return filters;
	}

	public boolean complete() {
		this.completed = true;
		return completed;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	public boolean isCompleted() {
		return completed;
	}

	public boolean hasFailed() {
		return failed;
	}

	public static String getLocation( String defaultLocation ){
		if( !Utils.assertNull( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}
}