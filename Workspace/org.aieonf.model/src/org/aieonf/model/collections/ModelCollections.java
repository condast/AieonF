package org.aieonf.model.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelNode;

public class ModelCollections {

	/**
	 * clean up the given collection of nodes by removing those without children
	 * @param nodes
	 */
	public static void cleanup( Collection<IModelNode<IDescriptor>> nodes ){
		Collection<IModelNode<IDescriptor>> cleanup = new ArrayList<IModelNode<IDescriptor>>( nodes );
		for( IModelNode<IDescriptor> cty: cleanup ){
			if( !cty.hasChildren())
				nodes.remove(cty);
		}
	}
}
