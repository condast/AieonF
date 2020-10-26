package org.aieonf.model.utils;

import java.util.Collection;
import java.util.logging.Logger;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class PrintModel {

	public static final String S_ERR_CYCLE_DETECTED = "A cycle has been detected: ";

	private static Logger logger = Logger.getLogger(PrintModel.class.getName());
	
	public static final String printModel( IModelLeaf<?> leaf, boolean addDescriptor ) {
		StringBuilder builder = new StringBuilder();
		printModel( builder, leaf, addDescriptor, 0 );
		return builder.toString();
	}

	private static final void printModel( StringBuilder builder, IModelLeaf<?> leaf, boolean addDescriptor, int depth ) {
		for( int i=0; i<depth; i++ )
			builder.append("\t");
		builder.append( leaf.toString());
		builder.append("\n");
		if( addDescriptor ) {
			builder.append("\t");
			builder.append(leaf.getData().toString());
			builder.append("\n");
		}
		logger.fine(builder.toString());
		if( leaf.isLeaf() )
			return;
		IModelNode<?> node = (IModelNode<?>) leaf;
		depth++;
		Collection<IModelLeaf<? extends IDescriptor>> children = node.getChildren().keySet(); 
		for( IModelLeaf<?> child: children ) {
			if( child.equals( node )) {
				logger.warning( S_ERR_CYCLE_DETECTED + node.getID() + "->" + child.getID());
				continue;
			}
			printModel( builder, child, addDescriptor, depth );
		}
	}
}
