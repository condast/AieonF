package org.aieonf.model.xml;

import java.io.InputStream;

import org.aieonf.concept.IDescriptor;

public interface IModelInterpreterFactory<D extends IDescriptor> {

	public InputStream createInputStream( String resource );
	
	public IXMLModelInterpreter<D> createInterpreter( InputStream in);
}
