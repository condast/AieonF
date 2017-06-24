package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.graph.IEdge;
import org.aieonf.commons.graph.IModel;
import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelWrapper implements IModel<IDescriptor, IDescriptor> {

	public static final String S_RELATION = "RelatesTo";

	private Vertex vertex;
	private IVertex<IDescriptor> root;
	private OrientGraph graph;
	
	public ModelWrapper( OrientGraph graph, Vertex vertex ) {
		this.vertex = vertex;
		this.graph = graph;
		this.root = (IVertex<IDescriptor>) new VertexImpl( vertex );
	}

	protected Collection<IVertex<IDescriptor>> vertices() {
		Collection<IVertex<IDescriptor>> results = new ArrayList<IVertex<IDescriptor>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<IDescriptor,IDescriptor> edge = new EdgeImpl( e); 
			IVertex<IDescriptor> vtx = edge.opposite( root );
		    if( vtx != null )
				results.add( vtx );
		}
		return results;
	}

	@Override
	public Collection<IVertex<IDescriptor>> getVertices(IDescriptor obj) {
		Collection<IVertex<IDescriptor>> results = new ArrayList<IVertex<IDescriptor>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<IDescriptor,IDescriptor> edge = new EdgeImpl( e); 
			IVertex<IDescriptor> vtx = edge.opposite( root );
		    if(( vtx != null ) && ( vtx.get().equals( obj )))
				results.add( vtx );
		}
		return results;
	}

	@Override
	public Collection<IEdge<IDescriptor, IDescriptor>> getEdges(IDescriptor obj) {
		Collection<IEdge<IDescriptor,IDescriptor>> results = new ArrayList<IEdge<IDescriptor,IDescriptor>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<IDescriptor,IDescriptor> edge = new EdgeImpl( e); 
			if( edge.get().equals( obj ))
				results.add( edge );
		}
		return results;
	}

	protected Collection<IEdge<IDescriptor, IDescriptor>> edges() {
		Collection<IEdge<IDescriptor,IDescriptor>> results = new ArrayList<IEdge<IDescriptor,IDescriptor>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<IDescriptor,IDescriptor> edge = new EdgeImpl( e); 
			results.add( edge );
		}
		return results;
	}

	@Override
	public IVertex<IDescriptor> getRoot() {
		return root;
	}

	@Override
	public void add(IEdge<IDescriptor, IDescriptor> edge) {
		Vertex first = VertexImpl.convert( graph, edge.first(), S_RELATION );
		Vertex last = VertexImpl.convert( graph, edge.last(), S_RELATION );
		graph.addEdge(null, first, last, S_RELATION );
	}

	@Override
	public void remove(IEdge<IDescriptor, IDescriptor> edge) {
		Edge edg = graph.getEdge( edge.getID() );
		graph.removeEdge( edg );
	}

	@Override
	public IEdge<IDescriptor, IDescriptor> insert( IVertex<IDescriptor> vertexV, IVertex<IDescriptor> vertexW, IDescriptor object) {
		Vertex first = graph.getVertex( vertexV.getID() );
		Vertex last = VertexImpl.convert( graph, vertexW, S_RELATION );
		Edge edge = graph.addEdge(null, first, last, S_RELATION );
		return new EdgeImpl( edge );
	}
}
