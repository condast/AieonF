package org.aieonf.osgi.sketch.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.osgi.sketch.context.SketchFactory;

public class FunctionComponent
{
	
	private SketchFactory factory = SketchFactory.getInstance(); 
	
	public void activate(){};
	
	public void deactivate(){};

	public void addProvider( IFunctionProvider<IDescriptor,IModelProvider<IModelLeaf<IDescriptor>>> function ){
		this.factory.addProvider( function );
	}
	
	public void removeProvider( IFunctionProvider<IDescriptor,IModelProvider<IModelLeaf<IDescriptor>>> function ){
		this.factory.removeProvider(function);
	}
}