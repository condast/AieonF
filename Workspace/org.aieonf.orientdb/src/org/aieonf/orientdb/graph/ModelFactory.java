package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.db.DatabaseService;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelFactory< T extends IDescriptor > {

	public static final String S_CLASS = "class:";
	
	public static final String S_ERR_CYCLE_DETECTED = "A cycle has been detected: ";

	public enum Attributes{
		P,//Properties of the model
		D,//Descriptor
		C;//Children

		public static boolean isAttribute( String str ) {
			for( Attributes attr: values() ){
				if( attr.name().equals(str.toUpperCase()))
					return true;
			}
			return false;
		}
	}

	private DatabaseService service;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelFactory( DatabaseService service ) {
		this.service = service;
	}
	
	/**
	 * Get the vertices, which are with descriptors, and create the corresponding models
	 * @param vertices
	 * @return
	 * @throws FilterException
	 */
	public Collection<IModelLeaf<IDescriptor>> get( Collection<Vertex> vertices ) throws FilterException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		Map<Object, IModelNode<IDescriptor>> nodes = new HashMap<>();
		try {
			OrientGraph graph = this.service.getGraph();
			IModelNode<IDescriptor> node = null;
			for( Vertex vertex: vertices ) {
				try {
					Iterator<Edge> edges = vertex.getEdges(Direction.IN, IDescriptor.DESCRIPTOR).iterator();
					while( edges.hasNext()) {
						Edge edge = edges.next();
						Vertex vnode = edge.getVertex(Direction.OUT);
						IModelNode<IDescriptor> parent = getParent(graph, vnode, nodes);
						node = new ModelNode( graph, parent, vnode );
						results.add(node);
						nodes.put(vnode, node);
					}
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return results;
	}
	
	protected IModelNode<IDescriptor> getParent( OrientGraph graph, Vertex vertex, Map<Object, IModelNode<IDescriptor>> parents ) {
		Iterator<Edge> edges = vertex.getEdges(Direction.IN, IModelLeaf.IS_PARENT).iterator();
		IModelNode<IDescriptor> parent = null;
		while( edges.hasNext()) {
			Edge edge = edges.next();
			Vertex vparent = edge.getVertex(Direction.OUT);
			if( vparent == null )
				continue;
			if( vparent.getId().equals(vertex.getId())) {
				graph.removeEdge(edge);
				logger.warning( S_ERR_CYCLE_DETECTED + vparent.getId() + "->" + vertex.getId());
				continue;
			}
			if( parents.containsKey(vparent.getId()))
				parent = parents.get(vparent.getId());
			else {
				parent = new ModelNode( graph, getParent( graph, vparent, parents ), vparent );
				if( parent != null )
					parents.put(vparent.getId(), parent);
			}
		}
		return parent;
	}
}
