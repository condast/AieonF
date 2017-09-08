package org.aieonf.orientdb.graph;

import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.graph.IVertex;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.graph.core.IAieonFVertex;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class VertexImpl implements IAieonFVertex<IDescriptor> {

	private Vertex vertex;
	private IDescriptor descriptor;
	
	
	public VertexImpl( Vertex vertex ) {
		this( vertex, new VertexDescriptor( vertex ));
	}

	public VertexImpl( Vertex vertex, IDescriptor descriptor ) {
		super();
		this.vertex = vertex;
		this.descriptor = descriptor;
	}

	@Override
	public Object getID() {
		return this.descriptor.getID() + ":" + vertex.getId();
	}

	@Override
	public void put(IDescriptor obj) {
		Iterator<String> iterator = obj.iterator();
		vertex.setProperty( "Name", obj.getName() );
		while( iterator.hasNext() ){
			String key = StringStyler.fromPackageString( iterator.next() );
			String attr = key.replace(".", "_8");
			vertex.setProperty( attr, obj.get(key));
		}
	}

	@Override
	public IDescriptor get() {
		return descriptor;
	}

	@Override
	public void remove() {
		Iterator<String> iterator = vertex.getPropertyKeys().iterator();
		while( iterator.hasNext() ){
			vertex.removeProperty( iterator.next());
		}
	}

	@Override
	public int addDegree() {
		String key = IVertex.Attributes.DEGREE.toString();
		String ds  = vertex.getProperty( key );
		int degree = 0;
		if( !Utils.assertNull( ds ))
			degree = Integer.parseInt( ds );
		vertex.setProperty( key, ++degree );
		return degree;
	}

	@Override
	public void removeDegree() {
		String key = IVertex.Attributes.DEGREE.toString();
		String ds  = vertex.getProperty( key );
		int degree = 0;
		if( !Utils.assertNull( ds ))
			degree = Integer.parseInt( ds );
		vertex.setProperty( key, --degree );
	}

	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("VERTEX:\n");
		Iterator<String> iterator = vertex.getPropertyKeys().iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			String id = StringStyler.toPackageString( key );
			buffer.append( "\t" + id + "=" + vertex.getProperty( key ) + ",\n");
		}		
		return buffer.toString();
	}

	/**
	 * Convert an IVertex to a tinkerpop vertex
	 * @param graph
	 * @param vertex
	 * @param meaning
	 * @return
	 */
	public static Vertex convert( OrientGraph graph, IVertex<? extends IDescriptor> vertex, String meaning ){
		  IDescriptor descriptor = vertex.get();
		  Vertex vtx = graph.addVertex(descriptor.getID()); 
		  while( descriptor.iterator().hasNext() ){
			  String key = descriptor.iterator().next();
			  vtx.setProperty(key, descriptor.get( key ));
		  }
		  
		  return vtx;
	}
	
	/**
	 * Create a descriptor from the concept base
	 * @author Kees
	 *
	 */
	private static class VertexDescriptor extends Descriptor{
		private static final long serialVersionUID = 1L;

		private VertexDescriptor( Vertex vertex ) {
			super(new VertexConceptBase( vertex ));
		}		
	}

	/**
	 * Create a descriptor from the concept base
	 * @author Kees
	 *
	 */
	public static class VertexConcept extends Concept{
		private static final long serialVersionUID = 1L;

		public VertexConcept( Vertex vertex ) {
			super(new VertexConceptBase( vertex ));
		}
		
	}

	@Override
	public IDescriptor getDescriptor() {
		return this.descriptor;
	}

	@Override
	public boolean hasChanged() {
		// TODO Auto-generated method stub
		return false;
	}

}
