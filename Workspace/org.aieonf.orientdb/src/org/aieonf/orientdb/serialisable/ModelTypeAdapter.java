package org.aieonf.orientdb.serialisable;

import java.util.logging.Logger;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.serialisable.core.AbstractModelTypeAdapter;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelTypeAdapter extends AbstractModelTypeAdapter {

	private OrientGraph graph;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelTypeAdapter( OrientGraph graph ) {
		super( Types.MODEL.toString() );
		this.graph = graph;
	}

	@Override
	protected IConceptBase onCreateConceptBase(boolean leaf) {
		Vertex vertex = graph.getVertex(null);
		IConceptBase base = new VertexConceptBase( vertex );
		return base;
	}

	@Override
	protected void onAddChild(IModelLeaf<IDescriptor> parent, IModelLeaf<IDescriptor> child, String arg1) {
		VertexConceptBase vcb = (VertexConceptBase) parent.getDescriptor().getBase(); 
		Vertex vparent = vcb.getVertex();
	    vcb = (VertexConceptBase) child.getDescriptor().getBase(); 
		Vertex vchild = vcb.getVertex();	
		graph.addEdge(null, vparent, vchild, arg1);
	}
}