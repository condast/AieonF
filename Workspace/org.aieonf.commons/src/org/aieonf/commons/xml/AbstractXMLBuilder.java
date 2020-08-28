/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.xml;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractXMLBuilder<T extends Object, E extends Object> implements IXMLBuilder<T> {

	public static String S_DEFAULT_FOLDER = "/design";
	public static String S_DEFAULT_DESIGN_FILE = "design.xml";
	public static String S_SCHEMA_LOCATION =  S_DEFAULT_FOLDER + "/rdm-schema.xsd";

	protected static final String JAXP_SCHEMA_SOURCE =
		    "http://java.sun.com/xml/jaxp/properties/schemaSource";

	//private static final String S_ERR_NO_SCHEMA_FOUND = "The XML Schema was not found";
	private static final String S_ERR_NO_SOURCE_FOUND = "The builder cannot find a source XML file";
	private static final String S_WRN_NOT_NAMESPACE_AWARE = "The parser is not validating or is not namespace aware";
	
	static final String S_DOCUMENT_ROOT = "DOCUMENT_ROOT";

	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";	

	public static enum Selection{
		DESIGN,
		DEFAULT,
		ENTRY,
		CONTROL,
		COMPOSITE,
		IMAGE_PROVIDER,
		INPUT,
		LAYOUT,
		LAYOUT_DATA,
		HORIZONTAL,
		VERTICAL,
		PREFERENCES,
		STORE,
		FRONTEND,
		LANGUAGE,
		NAVIGATION,
		TABFOLDER,
		ITEM,
		IMAGE,
		BODY,
		STATUS_BAR,
		TOOLBAR,
		REPORTS,
		ADVANCED,
		HELP;

		@Override
		public String toString() {
			return this.name();
		}
		
		public static boolean isOfSelection( String str ) {
			for( Selection selection: values() ) {
				if( selection.name().equals(str))
					return true;
			}
			return false;
		}
	}

	public enum AttributeNames{
		CLASS,
		ID,
		NAME,
		URL,
		LINK,
		SELECT,
		STYLE,
		SCOPE,
		TYPE,
		DATA,
		DESCRIPTION,
		HEIGHT,
		RWT_CUSTOM,
		WIDTH,
		USE,
		SIZE,
		TEXT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}

		public static boolean isAttribute( String value ){
			if( StringUtils.isEmpty( value ))
				return false;
			for( AttributeNames attr: values() ){
				if( attr.toString().equals( value ))
					return true;
			}
			return false;
		}
	}

	private boolean completed, failed;
	private InputStream in;
	
	private IXMLHandler<T> handler;

	private Logger logger = Logger.getLogger( AbstractXMLBuilder.class.getName() );
	
	protected AbstractXMLBuilder( IXMLHandler<T> handler, Class<?> clss, String fileName ) {
		this( handler, clss.getResourceAsStream( fileName ) );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	protected AbstractXMLBuilder( IXMLHandler<T> handler ) {
		this( handler, null );
	}
	
	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	protected AbstractXMLBuilder( IXMLHandler<T> handler, InputStream in ) {
		this.in = in;
		this.handler = handler;
		this.completed = false;
		this.failed = false;
	}

	protected void setInputStram(InputStream in) {
		this.in = in;
	}

	public void addListener( IBuildListener<T> listener ){
		this.handler.addListener( listener);
	}

	public void removeListener( IBuildListener<T> listener ){
		this.handler.removeListener( listener);
	}

	public IXMLHandler<T>  getHandler(){
		return handler;
	}
		
	/* (non-Javadoc)
	 * @see org.condast.commons.xml.IXMLBuilder#canCreate()
	 */
	@Override
	public boolean canCreate(){
		return ( in != null );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#build()
	 */
	/* (non-Javadoc)
	 * @see org.condast.commons.xml.IXMLBuilder#build()
	 */
	@Override
	public void build() {
		if( !canCreate() )
			throw new NullPointerException( S_ERR_NO_SOURCE_FOUND);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//URL schema_in = XMLFactoryBuilder.class.getResource( S_SCHEMA_LOCATION); 
		//if( schema_in == null )
		//	throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		//SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		//Source schemaFile = new StreamSource( this.getClass().getResourceAsStream( S_SCHEMA_LOCATION ));		
		//First parse the XML file
		try {
			logger.info("Parsing code: " );
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			saxParser.parse( in, ( DefaultHandler )handler);
		} catch( SAXNotRecognizedException e ){
			failed = true;
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			failed = true;
			e.printStackTrace();
		} catch (SAXException e) {
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

	/* (non-Javadoc)
	 * @see org.condast.commons.xml.IXMLBuilder#getParsedUnit(java.lang.String)
	 */
	@Override
	public T getParsedUnit( String id ) {
		return handler.getUnit(id);
	}

	public boolean complete() {
		this.completed = true;
		return completed;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	/* (non-Javadoc)
	 * @see org.condast.commons.xml.IXMLBuilder#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return completed;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.xml.IXMLBuilder#hasFailed()
	 */
	@Override
	public boolean hasFailed() {
		return failed;
	}

	@Override
	public T[] getUnits() {
		return handler.getUnits();
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
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