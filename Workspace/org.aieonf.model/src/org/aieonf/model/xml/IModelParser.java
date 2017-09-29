package org.aieonf.model.xml;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface IModelParser<T extends IDescriptor, M extends IDescribable<T>> {
	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelBuilderListener( IModelBuilderListener<M> listener );

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelBuilderListener( IModelBuilderListener<M> listener );

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
