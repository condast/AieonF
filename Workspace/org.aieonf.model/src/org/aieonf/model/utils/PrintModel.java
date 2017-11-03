package org.aieonf.model.utils;

import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class PrintModel {

	public static final String printModel( IModelLeaf<?> leaf, boolean addDescriptor ) {
		StringBuffer buffer = new StringBuffer();
		printModel( buffer, leaf, addDescriptor, 0 );
		return buffer.toString();
	}

	private static final void printModel( StringBuffer buffer, IModelLeaf<?> leaf, boolean addDescriptor, int depth ) {
		for( int i=0; i<depth; i++ )
			buffer.append("\t");
		buffer.append( leaf.toString());
		buffer.append("\n");
		if( leaf.isLeaf() )
			return;
		IModelNode<?> node = (IModelNode<?>) leaf;
		depth++;
		for( IModelLeaf<?> child: node.getChildren() )
			printModel( buffer, child, addDescriptor, depth );
	}

}
