package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.graph.IEdge;
import org.aieonf.commons.graph.IVertex;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Concept;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class EdgeImpl implements IEdge<IDescriptor,IDescriptor> {

	private Edge edge;
	private IVertex<IDescriptor> first;
	private IVertex<IDescriptor> last;
	
	
	public EdgeImpl( Edge edge ) {
		super();
		this.edge = edge;
		this.first = new VertexImpl( edge.getVertex( Direction.IN ));
		this.last = new VertexImpl( edge.getVertex( Direction.OUT ));
	}


	@Override
	public Object getID() {
		return edge.getId();
	}

	
	@Override
	public void put(IVertex<IDescriptor> v, IVertex<IDescriptor> w, IDescriptor obj) {
		this.first = v;
		this.last = w;
		this.replace(obj);
	}


	@SuppressWarnings("unchecked")
	@Override
	public IDescriptor get() {
		Iterator<String> iterator = edge.getPropertyKeys().iterator();
		IDescriptor descriptor = new Concept();
		while( iterator.hasNext() ){
			String key = iterator.next();
			descriptor.set( key, (String) edge.getProperty( key ));
		}
		return descriptor;
	}


	@Override
	public void replace(IDescriptor obj) {
		this.remove();
		Iterator<String> iterator = obj.iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			edge.setProperty( key, obj.get(key));
		}
	}

	protected void remove() {
		Iterator<String> iterator = edge.getPropertyKeys().iterator();
		while( iterator.hasNext() ){
			edge.removeProperty( iterator.next());
		}
	}


	@Override
	public Collection<IVertex<IDescriptor>> endVertices() {
		Collection<IVertex<IDescriptor>> vertices = new ArrayList<IVertex<IDescriptor>>();
		vertices.add( this.first );
		vertices.add( this.last );
		return vertices;
	}


	@Override
	public boolean containsVertex(IVertex<IDescriptor> vertex) {
		return ( this.first.equals( vertex )) || ( this.last.equals( vertex ));
	}


	@Override
	public IVertex<IDescriptor> first() {
		return this.first;
	}


	@Override
	public IVertex<IDescriptor> last() {
		return this.last;
	}


	@Override
	public IVertex<IDescriptor> opposite(IVertex<IDescriptor> vertex) {
		if( this.first.equals( vertex ))
			return this.last;
		if( this.last.equals( vertex ))
			return this.first;
		return null;
	}

	/**
	 * Convert an IVertex to a tinkerpop vertex
	 * @param graph
	 * @param vertex
	 * @param meaning
	 * @return
	 */
	public static Edge convert( OrientGraph graph, IEdge<IDescriptor, IVertex<IDescriptor>> edge, String meaning ){
		  Vertex first = graph.getVertex( edge.first().getID() );
		  Vertex last = graph.getVertex( edge.last().getID() );
		  Edge edg = graph.addEdge(null, first, last, meaning );
		  IDescriptor descriptor = (IDescriptor) edge.get();
		  while( descriptor.iterator().hasNext() ){
			  String key = descriptor.iterator().next();
			  edg.setProperty(key, descriptor.get( key ));
		  }
		  return edg;
	}

}
