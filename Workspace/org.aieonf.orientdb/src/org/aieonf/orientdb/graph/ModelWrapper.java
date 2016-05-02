package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.util.graph.IEdge;
import org.aieonf.util.graph.IModel;
import org.aieonf.util.graph.IVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelWrapper<T extends IDescriptor, U extends IDescriptor> implements IModel<T, U> {

	public static final String S_RELATION = "RelatesTo";

	private Vertex vertex;
	private IVertex<T> root;
	private OrientGraph graph;
	
	public ModelWrapper( OrientGraph graph, Vertex vertex ) {
		this.vertex = vertex;
		this.graph = graph;
		this.root = new VertexImpl<T>( vertex );
	}

	protected Collection<IVertex<T>> vertices() {
		Collection<IVertex<T>> results = new ArrayList<IVertex<T>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<T,U> edge = new EdgeImpl<T,U>( e); 
			IVertex<T> vtx = edge.opposite( root );
		    if( vtx != null )
				results.add( vtx );
		}
		return results;
	}

	@Override
	public Collection<IVertex<T>> getVertices(T obj) {
		Collection<IVertex<T>> results = new ArrayList<IVertex<T>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<T,U> edge = new EdgeImpl<T,U>( e); 
			IVertex<T> vtx = edge.opposite( root );
		    if(( vtx != null ) && ( vtx.get().equals( obj )))
				results.add( vtx );
		}
		return results;
	}

	@Override
	public Collection<IEdge<T, U>> getEdges(U obj) {
		Collection<IEdge<T,U>> results = new ArrayList<IEdge<T,U>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<T,U> edge = new EdgeImpl<T,U>( e); 
			if( edge.get().equals( obj ))
				results.add( edge );
		}
		return results;
	}

	protected Collection<IEdge<T, U>> edges() {
		Collection<IEdge<T,U>> results = new ArrayList<IEdge<T,U>>();
		for (Edge e : vertex.getEdges( Direction.BOTH, new String[0])) {
		    IEdge<T,U> edge = new EdgeImpl<T,U>( e); 
			results.add( edge );
		}
		return results;
	}

	@Override
	public IVertex<T> getRoot() {
		return root;
	}

	@Override
	public void add(IEdge<T, U> edge) {
		Vertex first = VertexImpl.convert( graph, edge.first(), S_RELATION );
		Vertex last = VertexImpl.convert( graph, edge.last(), S_RELATION );
		graph.addEdge(null, first, last, S_RELATION );
	}

	@Override
	public void remove(IEdge<T, U> edge) {
		Edge edg = graph.getEdge( edge.getID() );
		graph.removeEdge( edg );
	}

	@Override
	public IEdge<T, U> insert( IVertex<T> vertexV, IVertex<T> vertexW, U object) {
		Vertex first = graph.getVertex( vertexV.getID() );
		Vertex last = VertexImpl.convert( graph, vertexW, S_RELATION );
		Edge edge = graph.addEdge(null, first, last, S_RELATION );
		return new EdgeImpl<T,U>( edge );
	}
}
