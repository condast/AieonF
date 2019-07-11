package org.aieonf.template.xml;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.xml.IModelParser;
import org.aieonf.model.xml.IXMLModelInterpreter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface ITemplateParser<T extends IDescriptor, M extends IModelLeaf<T>> extends IModelParser<T,M>{

	/**
	 * Get the root factory 
	 * @return
	 */
	IModelLeaf<T> getRoot();
	
	/**
	 * Get the interpreter
	 * @return
	 */
	public IXMLModelInterpreter<T,M> getCreator();

	void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

}