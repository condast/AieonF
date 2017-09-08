package org.aieonf.model.xml;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.core.IModelLeaf;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface IModelParser<T extends IDescriptor> {

	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelBuilderListener( IModelBuilderListener<IModelLeaf<T>> listener );

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelBuilderListener( IModelBuilderListener<IModelLeaf<T>> listener );

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
