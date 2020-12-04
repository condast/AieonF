package org.aieonf.orientdb.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.orientdb.db.DatabaseService;
import org.aieonf.orientdb.serialisable.ModelTypeAdapter;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelDatabase< T extends IDescriptor > {

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
	
	private IDomainAieon domain; 
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public ModelDatabase( DatabaseService service, IDomainAieon domain ) {
		this.service = service;
		this.domain = domain;
	}

	/**
	 * Get the vertices, which are with descriptors, and create the corresponding models
	 * @param vertices
	 * @return
	 * @throws FilterException
	 */
	public Collection<IModelLeaf<IDescriptor>> find( long id ) throws FilterException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		Map<Object, IModelNode<IDescriptor>> nodes = new HashMap<>();
		try {
			OrientGraph graph = this.service.getGraph();
			Iterable<Vertex> vertices = graph.getVertices( IDescriptor.Attributes.ID.name(), String.valueOf(id));
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

	/**
	 * Get the models that have the given key-value pair
	 * @param vertices
	 * @return
	 * @throws FilterException
	 */
	public Collection<IModelLeaf<IDescriptor>> find( String key, String value ) throws FilterException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<>();
		Map<Object, IModelNode<IDescriptor>> nodes = new HashMap<>();
		try {
			OrientGraph graph = this.service.getGraph();
			Iterable<Vertex> vertices = graph.getVertices( service.getDomainClass(domain) + "." + key, value );
			IModelNode<IDescriptor> node = null;
			for( Vertex vertex: vertices ) {
				try {
					Iterator<Edge> edges = vertex.getEdges(Direction.OUT, IDescriptor.DESCRIPTOR).iterator();
					if( edges.hasNext()) {
						IModelNode<IDescriptor> parent = getParent(graph, vertex, nodes);
						node = new ModelNode( graph, parent, vertex );
						results.add(node);
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

	/**
	 * Add the given node to the model with the given model id
	 * @param vertices
	 * @return
	 * @throws FilterException
	 */
	public boolean addNode( long modelId, IModelLeaf<IDescriptor> node, String label) throws FilterException {
		try {
			OrientGraph graph = this.service.getGraph();
			Iterator<Vertex> vertices = graph.getVertices( IDescriptor.Attributes.ID.name(), String.valueOf(modelId)).iterator();
			if( !vertices.hasNext())
				return false;
			Vertex vertex = vertices.next();
			VertexConceptBase base = (VertexConceptBase) node.getDescriptor().getBase();
			Vertex child = base.getVertex();
			vertex.addEdge( label, child);
			return true;
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return false;
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

	@SuppressWarnings("unchecked")
	public void update(IModelLeaf<? extends IDescriptor> leaf) {
		try {
			OrientGraph graph = this.service.getGraph();
			Iterable<Vertex> vertices = graph.getVertices( IDescriptor.Attributes.ID.name(), String.valueOf(leaf.getID()));
			IModelNode<IDescriptor> model = null;
			boolean found = false;
			for( Vertex vertex: vertices ) {
				updateProperties(leaf.getDescriptor(), vertex, false);
				Iterator<Edge> edges = vertex.getEdges(Direction.IN).iterator();
				while( edges.hasNext()) {
					Edge edge = edges.next();
					Vertex vnode = edge.getVertex(Direction.OUT);
					if( IDescriptor.DESCRIPTOR.equals(edge.getLabel())) {
						updateProperties(leaf.getData(), vnode, false);
					}
					if(leaf.isLeaf())
						continue;
					//First remove edges that don't exist any more, or update the labels
					model = (IModelNode<IDescriptor>) leaf;
					Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>,String>> iterator = model.getChildren().entrySet().iterator();
					found = false;
					while( iterator.hasNext() ) {
						Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
						String id = vnode.getProperty(IDescriptor.Attributes.ID.name());
						if( id.equals( String.valueOf(entry.getKey().getID()))){
							if( !edge.getLabel().equals(entry.getValue())) {
								graph.removeEdge(edge);
								vertex.addEdge(entry.getValue(), vnode);
							}
						}
					}
					if(!found)
						graph.removeEdge(edge);
				}
				if( leaf.isLeaf())
					return;
				model = (IModelNode<IDescriptor>) leaf;
				Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>,String>> iterator = model.getChildren().entrySet().iterator();
				found = false;
				while( iterator.hasNext() ) {
					Vertex vnode = null;
					Edge edge = null;
					Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
					edges = vertex.getEdges(Direction.IN).iterator();
					while( edges.hasNext()) {
						edge = edges.next();
						vnode = edge.getVertex(Direction.OUT);
						if( IDescriptor.DESCRIPTOR.equals(edge.getLabel())) {
							found = true;
							update( entry.getKey());
						}
					}
					String str = domain.getDomain().replace(".", "_");
					if( found == false ) {
						ModelTypeAdapter.findOrCreateVertex(graph, str, entry.getKey().getDescriptor().getBase());				
						vnode = graph.addVertex(ModelDatabase.S_CLASS + IDescriptor.DESCRIPTORS);
						ModelTypeAdapter.fill(vnode, entry.getKey().getData().getBase());
						vertex.addEdge(IDescriptor.DESCRIPTOR, vnode);
					}
				}
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	protected void updateProperties( IDescriptor descriptor, Vertex vertex, boolean include ) {
		if(!include ) {
			for( String key: vertex.getPropertyKeys()) {
				String attr = descriptor.get(key);
				if( IDescriptor.Attributes.ID.name().equals(attr) || IDescriptor.Attributes.CREATE_DATE.name().equals(attr))
					continue;
				if(StringUtils.isEmpty(attr))
					vertex.removeProperty(key);
			}
		}
		Iterator<Map.Entry<String, String>> iterator = descriptor.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			vertex.setProperty(entry.getKey(), entry.getValue());
		}
	}
}
