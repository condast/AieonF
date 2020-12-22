package org.aieonf.orientdb.serialisable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.serialise.AbstractModelTypeAdapter;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.orientdb.db.ModelDatabase;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;


/**
 * The model type adapter creates the graph structure in terms of vertices and edges from serialised data.
 * At the end it wraps this structure in a tree structure (IModelLeaf<IDescriptor>) 
 * @author Kees
 *
 */
public class OrientModelTypeAdapter extends AbstractModelTypeAdapter<Vertex, Vertex> {

	private OrientGraph graph;
		
	private IDomainAieon domain; 

	public OrientModelTypeAdapter( IDomainAieon domain, OrientGraph graph) {
		super();
		this.domain = domain;
		this.graph = graph;
	}
	
	@Override
	protected String getID(Vertex node) {
		return node.getId().toString();
	}

	/**
	 * The model type adapter FIRST creates the graph structure, and only once this is
	 * completed makes this into a model
	 */
	@Override
	protected IModelLeaf<IDescriptor> onTransform(Vertex node) {
		ModelNode model = new ModelNode( this.graph, node );
		return model;
	}

	@Override
	protected Vertex onCreateNode( long id, IConceptBase base, boolean leaf, Vertex descriptor ) {
		String str = domain.getDomain().replace(".", "_");
		
		Vertex vertex = getParent( this.graph, str, descriptor, IDescriptor.DESCRIPTOR);
		if( vertex != null )
			return vertex;
			
		vertex = findOrCreateVertex( graph, str, base);
		Edge edge = vertex.addEdge(IDescriptor.DESCRIPTOR, descriptor);
		edge.setProperty(IDescriptor.Attributes.CREATE_DATE.name(), Calendar.getInstance().getTimeInMillis());
		return vertex;
	}

	@Override
	protected void handleCycle(Vertex parent, Vertex child, String label) {
		Iterator<Edge> edges = parent.getEdges(Direction.BOTH, label).iterator();
		while( edges.hasNext())
			this.graph.removeEdge(edges.next());
		super.handleCycle(parent, child, label);
	}

	@Override
	protected boolean onAddChild(Vertex model, Vertex child, boolean reversed, String label) {
		if( exists( model, child ))
			return false;
		addChild( model, child, reversed, label);
		return true;
	}

	@Override
	protected Vertex onCreateDescriptor(IConceptBase base) {
		Iterator<Vertex> vertices = graph.getVertices().iterator();
		Vertex vertex = null;
		ImplicitAieon implicit = null;
		while( vertices.hasNext() ) {
			vertex = vertices.next();
			IDescriptor vdescriptor = VertexConceptBase.createDescriptor( vertex );
			if( ImplicitAieon.isImplicit(vdescriptor)) {
				implicit = new ImplicitAieon( vdescriptor.getBase() );
				if( implicit.test( new Descriptor( base )))
					return vertex;
			}else if( ImplicitAieon.isImplicit(base)){
				implicit = new ImplicitAieon( base );
				if( implicit.test( vdescriptor)) {
					ImplicitAieon.setImplicit(vdescriptor, implicit.getImplicit());
					return vertex;
				}			
			}
		}
		
		//The new descriptor is not already available, so create a new vertex and add the properties of the base
		String idStr = IDescriptor.Attributes.ID.name();
		vertex = graph.addVertex( ModelDatabase.S_CLASS + IDescriptor.DESCRIPTORS);
		vertex.setProperty(idStr, createId( vertex.getId()));
		vertex.setProperty(IDescriptor.Attributes.CREATE_DATE.name(), Calendar.getInstance().getTimeInMillis());
		
		Iterator<Map.Entry<String, String>> iterator = base.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			if( !idStr.equals(entry.getKey()) )
				vertex.setProperty(entry.getKey(), entry.getValue());
			else {
				long id = Long.parseLong( entry.getValue());
				if( id < 0 )
					vertex.setProperty(idStr, createId( vertex.getId() ));
			}
		}		
		return vertex;
	}
		
	protected boolean areImplicit( Vertex vreference, Vertex vtest ) {
		IConceptBase base = new VertexConceptBase( vreference );
		IConceptBase test = new VertexConceptBase( vreference );
		ImplicitAieon implicit = null;
		boolean result = false;
		if( ImplicitAieon.isImplicit(base)) {
			implicit = new ImplicitAieon( base );
			result = implicit.accept(new Descriptor( test ));
		}else if( ImplicitAieon.isImplicit(test)) {
			implicit = new ImplicitAieon( test );
			result = implicit.accept(new Descriptor( base ));
			if( result )
				base.set(IImplicit.Attributes.IMPLICIT.name(), test.get(IImplicit.Attributes.IMPLICIT.name()));
		}
		return result;
	}
	
	/**
	 * Create an id from the given vertex or edge
	 * @param obj
	 * @return
	 */
	public static String createId( Object obj ) {
		String str = obj.toString().replaceAll("[^0-9]", "");
		return str;
	}
	
	public static Direction opposite( Direction direction ) {
		Direction result = Direction.BOTH;
		switch( direction ) {
		case IN:
			result = Direction.OUT;
			break;
		case OUT:
			result = Direction.IN;
			break;
		default:
			break;
		}
		return result;
	}
	
	/**
	 * Get the parent vertex for the given label
	 * @param vertex
	 * @param label
	 * @return
	 */
	protected static Vertex getParent( OrientGraph graph, String domain, Vertex vertex, String label ) {
		Collection<Vertex> results = getConnected( graph, domain, vertex, Direction.IN, label);
		return Utils.assertNull(results)? null: results.iterator().next();
	}

	/**
	 * Get the vertices that are connected to the given vertex, by domain, direction and label
	 * @param vertex
	 * @param label
	 * @return
	 */
	protected static Collection<Vertex> getConnected( OrientGraph graph, String domain, Vertex vertex, Direction direction, String label ) {
		Collection<Vertex> results = new ArrayList<>();
		Collection<Object> ids = new ArrayList<>();
		Iterator<Edge> edges = vertex.getEdges(direction, label).iterator();
		Vertex connected = null;
		while( edges.hasNext()) {
			Edge edge = edges.next();
			connected = edge.getVertex( opposite( direction ));
			ids.add(connected.getId());
		}
		Iterator<Vertex> vertices = graph.getVerticesOfClass(domain).iterator();
		while( vertices.hasNext()) {
			Vertex test = vertices.next();
			if( ids.contains(test.getId()))
				results.add(test);
		}
		return results;
	}

	/**
	 * If the parent/child relationship exists, return true;
	 * @param vertex
	 * @param label
	 * @return
	 */
	protected static boolean exists( Vertex parent, Vertex child ) {
		Iterator<Edge> iterator = parent.getEdges(Direction.BOTH ).iterator();
		while( iterator.hasNext() ) {
			Edge edge = iterator.next();
			if( edge.getVertex( Direction.IN).getId() == parent.getId() ){
				if( edge.getVertex( Direction.OUT).getId() == child.getId())
					return true;
			}else if( edge.getVertex( Direction.OUT).getId() == parent.getId() ){
				if( edge.getVertex( Direction.IN).getId() == child.getId())
					return true;
			}
		}
		return false;
	}

	/**
	 * Find the vertices that belong to the given domain and have equal (positive) id as the reference, or are implicit
	 * @param graph
	 * @param domain
	 * @param base
	 * @return
	 */
	protected static Collection<Vertex> findVertices( OrientGraph graph, String domain, IConceptBase base ){
		Collection<Vertex> results=  new ArrayList<>();
		Iterator<Vertex> iterator = null;
		long id = Descriptor.parseId(base);
		try{
			iterator = graph.getVerticesOfClass(domain).iterator();
			while( iterator.hasNext() ) {
				Vertex vertex = iterator.next();
				String idstr = vertex.getProperty(IDescriptor.Attributes.ID.name());
				long refId = Descriptor.parseId( idstr);
				if(( id >= 0 ) &&  ( refId == id )) {
					results.add(vertex);
				}else {
					IConceptBase vbase = new VertexConceptBase( vertex );
					if( ImplicitAieon.areImplicit(base, vbase))
						results.add(vertex);
				}
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return results;
	}

	public static Vertex findOrCreateVertex( OrientGraph graph, String domain, IConceptBase base ) {
		Vertex vertex = null;
		Collection<Vertex> vertices = findVertices( graph, domain, base);
		if( !Utils.assertNull(vertices)) {
			vertex = vertices.iterator().next();
			return vertex;
		}else {
			vertex = graph.addVertex( ModelDatabase.S_CLASS + domain );				
			vertex.setProperty(IDescriptor.Attributes.ID.name(), createId( vertex.getId()));
			vertex.setProperty(IDescriptor.Attributes.CREATE_DATE.name(), Calendar.getInstance().getTimeInMillis());
		}
		fill(vertex, base );
		return vertex;
	}

	protected static Collection<Vertex> findVertices( OrientGraph graph, long id ){
		Collection<Vertex> results=  new ArrayList<>();
		Iterator<Vertex> iterator = graph.getVertices().iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();
			String check = vertex.getProperty(IDescriptor.Attributes.ID.name());
			if( StringUtils.isEmpty(check) ||( Long.parseLong(check) != id))
				continue;
			results.add(vertex);
		}
		return results;
	}

	protected static Collection<Vertex> findVertices( OrientGraph graph, String domain, long id ){
		Collection<Vertex> results=  new ArrayList<>();
		Iterator<Vertex> iterator = null;
		try{
			iterator = graph.getVerticesOfClass(domain).iterator();
			while( iterator.hasNext() ) {
				Vertex vertex = iterator.next();
				String check = vertex.getProperty(IDescriptor.Attributes.ID.name());
				if( StringUtils.isEmpty(check) ||( Long.parseLong(check) != id))
					continue;
				results.add(vertex);
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return results;
	}

	public static void addChild( Vertex parent, Vertex child, boolean reverse, String label ) {
		Edge edge = null;
		if( !reverse ) {
			edge = parent.addEdge(label,child);
			child.addEdge(IModelLeaf.IS_PARENT, parent);
			edge.setProperty(IDescriptor.Attributes.CREATE_DATE.name(), Calendar.getInstance().getTimeInMillis());
			return;
		}
		edge = child.addEdge(label, parent);
		edge.setProperty(IDescriptor.Attributes.CREATE_DATE.name(), Calendar.getInstance().getTimeInMillis());
		parent.addEdge( IModelLeaf.IS_PARENT, child );
		String depth = IModelLeaf.Attributes.DEPTH.name();
		String pdepth = parent.getProperty( depth);
		String cdepth = child.getProperty(  depth );
		parent.setProperty( depth, cdepth);
		child.setProperty( depth, pdepth);
	}
	
	public static void fill( Vertex vertex, IConceptBase base ) {
		Iterator<Map.Entry<String, String>> iterator = base.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			vertex.setProperty(entry.getKey(), entry.getValue());
		}
		vertex.setProperty(IDescriptor.Attributes.UPDATE_DATE.name(), Calendar.getInstance().getTimeInMillis());			
	}
}