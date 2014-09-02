package org.aieonf.model.xml;

import org.aieonf.model.builder.IModelBuilderListener;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface IModelParser {

	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelBuilderListener( IModelBuilderListener listener );

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelBuilderListener( IModelBuilderListener listener );

	/**
	 * Provide the start element of a SAX parser
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException
	 */
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException;	

	/**
	 * Provide the end element of a SAX parser
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException;
}
