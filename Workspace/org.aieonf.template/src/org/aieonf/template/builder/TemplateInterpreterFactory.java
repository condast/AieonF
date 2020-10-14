package org.aieonf.template.builder;

import java.io.InputStream;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.xml.IModelInterpreterFactory;
import org.aieonf.model.xml.IXMLModelInterpreter;

public class TemplateInterpreterFactory<D extends IDescriptor> implements IModelInterpreterFactory<D> {

	private Class<?> clss;

	public TemplateInterpreterFactory( Class<?> clss ){
		this.clss = clss;
	}

	@Override
	public InputStream createInputStream(String resource) {
		return this.clss.getResourceAsStream(resource);
	}


	@SuppressWarnings("unchecked")
	@Override
	public IXMLModelInterpreter<D> createInterpreter( InputStream in ) {
		TemplateInterpreter result = new TemplateInterpreter( this.clss, in);
		return (IXMLModelInterpreter<D>) result;
	}

}
