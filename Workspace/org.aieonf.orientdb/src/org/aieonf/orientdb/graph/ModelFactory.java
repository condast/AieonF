package org.aieonf.orientdb.graph;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory {

	private OrientGraph graph;
	
	public ModelFactory( OrientGraph graph ) {
		this.graph = graph;
	}
	
	public IModelLeaf<IDescriptor> transform( IModelLeaf<IDescriptor> model ) {
		return null;
	}

}
