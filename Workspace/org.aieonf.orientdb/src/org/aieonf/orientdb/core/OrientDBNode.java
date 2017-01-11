package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelNode;
import org.aieonf.model.ModelLeaf;
import org.aieonf.orientdb.graph.AbstractOrientGraphModel;
import org.aieonf.orientdb.graph.VertexConceptBase;
import org.aieonf.orientdb.graph.VertexImpl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientDBNode<T extends IDescriptor> extends ModelLeaf<T> implements IModelNode<T> {

	private static final String S_ERR_NULL_IDENTIFITER = "The identifier of this model may not be null: ";
	private Graph graph;
	private Vertex vertex;
	
	public OrientDBNode( Graph graph, Vertex vertex ) {
		super( new VertexImpl<T>( vertex, new VertexImpl.VertexConcept( vertex ) ).get() );
		super.setIdentifier( super.getDescriptor().get( IModelLeaf.Attributes.IDENTIFIER ));
		this.graph = graph;
		this.vertex = vertex;
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.BOTH );
		super.setLeaf( !iterable.iterator().hasNext() );
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child) {
		Vertex last = null;
		if( Utils.assertNull( child.getIdentifier()))
			throw new NullPointerException( S_ERR_NULL_IDENTIFITER + child );
		for (Vertex v : graph.getVertices()) {
		    IDescriptor descriptor = new VertexDescriptor( v );
			if( !child.getDescriptor().equals( descriptor ))
				continue;	
			last = v;
			break;
		}		
		if( last == null ){
			last = AbstractOrientGraphModel.convert((OrientGraph) graph, child.getDescriptor(), child.getIdentifier() );
		}
		graph.addEdge(null, vertex, last, child.getIdentifier());
		return true;
	}

	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child) {
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.OUT );
		Iterator<Edge> iterator = iterable.iterator();
		Edge edge = null;
		while( iterator.hasNext() ){
			edge = iterator.next();
			IVertex<IDescriptor> vtx= new VertexImpl<IDescriptor>( edge.getVertex(com.tinkerpop.blueprints.Direction.OUT ));
			if( vtx.equals( child.getDescriptor() ))
				break;
		}
		if( edge != null ){
			graph.removeEdge( edge );
			return true;
		}
		return false;
	}

	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren() {
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.OUT );
		Iterator<Edge> iterator = iterable.iterator();
		Edge edge = null;
		Collection<IModelLeaf<? extends IDescriptor>> children = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		while( iterator.hasNext() ){
			edge = iterator.next();
			Vertex vtx= edge.getVertex(com.tinkerpop.blueprints.Direction.IN );
			IModelLeaf<IDescriptor> child = new OrientDBNode<IDescriptor>( graph, vtx ); 
			children.add( child );
		}
		return children;
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getChild(IDescriptor descriptor) {
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.OUT );
		Iterator<Edge> iterator = iterable.iterator();
		Edge edge = null;
		while( iterator.hasNext() ){
			edge = iterator.next();
			Vertex vtx= edge.getVertex(com.tinkerpop.blueprints.Direction.OUT );
			IVertex<IDescriptor> vertex = new VertexImpl<IDescriptor>( vtx, new VertexImpl.VertexConcept( vtx ));
			if( vertex.get().equals( descriptor ))
				return new OrientDBNode<IDescriptor>( graph, vtx ); 
		}
		return null;
	}

	@Override
	public boolean hasChildren() {
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.OUT );
		Iterator<Edge> iterator = iterable.iterator();
		return iterator.hasNext() ;
	}

	@Override
	public int nrOfchildren() {
		Iterable<Edge> iterable = vertex.getEdges( com.tinkerpop.blueprints.Direction.OUT );
		Iterator<Edge> iterator = iterable.iterator();
		int index = 0;
		while( iterator.hasNext() ){
			index++;
		}
		return index;
	}

	private static class VertexDescriptor extends Descriptor{
		private static final long serialVersionUID = 1L;

		private VertexDescriptor( Vertex vertex ) {
			super(new VertexConceptBase( vertex ));
		}
		
	}

}
