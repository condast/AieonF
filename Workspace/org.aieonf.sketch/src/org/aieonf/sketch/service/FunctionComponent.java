package org.aieonf.sketch.service;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.sketch.factory.SketchFactory;

public class FunctionComponent
{	
	private SketchFactory factory = SketchFactory.getInstance(); 
	
	public FunctionComponent() {
		super();
	}

	public void activate(){};
	
	public void deactivate(){};

	public void addProvider( IFunctionProvider<String,IModelProvider<IDescriptor, IModelLeaf<IDescriptor>>> function ){
		this.factory.addProvider( function );
	}
	
	public void removeProvider( IFunctionProvider<String,IModelProvider<IDescriptor, IModelLeaf<IDescriptor>>> function ){
		this.factory.removeProvider(function);
	}

}
