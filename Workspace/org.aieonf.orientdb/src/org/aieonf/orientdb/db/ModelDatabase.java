package org.aieonf.orientdb.db;

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
import org.aieonf.orientdb.core.ModelLeaf;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.orientdb.serialisable.OrientModelTypeAdapter;

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
				IModelNode<IDescriptor> parent = getParent(graph, vertex, nodes);
				node = new ModelNode( graph, parent, vertex );
				results.add(node);
				nodes.put(vertex, node);
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		return results;
	}

	/**
	 * Get the vertices, which are with descriptors, and create the corresponding models
	 * @param vertices
	 * @return
	 * @throws FilterException
	 */
	public Collection<IModelLeaf<IDescriptor>> findOnDescriptor( long id ) throws FilterException {
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

	/**
	 * Update the given leaf
	 * @param leaf
	 */
	public void update(IModelLeaf<? extends IDescriptor> leaf) {
		OrientGraph graph = this.service.getGraph();
		Iterable<Vertex> vertices = graph.getVertices( IDescriptor.Attributes.ID.name(), String.valueOf(leaf.getID()));
		for( Vertex vertex: vertices ) {
			update(vertex, leaf );
		}
	}

	/**
	 * Update the given vertex
	 * @param leaf
	 */
	public void update( ModelLeaf leaf ) {
		update(leaf.getVertex(), leaf );
	}

	@SuppressWarnings("unchecked")
	protected boolean update( Vertex vertex, IModelLeaf<? extends IDescriptor> leaf) {
		boolean result = false;
		try {
			OrientGraph graph = this.service.getGraph();
			if( !vertex.getProperty(IDescriptor.Attributes.ID.name()).equals( String.valueOf(leaf.getID())))
				return false;
			IModelNode<IDescriptor> model = null;
			boolean found = false;

			updateProperties(leaf.getDescriptor(), vertex, false);
			Iterator<Edge> edges = vertex.getEdges(Direction.OUT).iterator();
			while( edges.hasNext()) {
				Edge edge = edges.next();
				Vertex vnode = edge.getVertex(Direction.IN);

				if( IDescriptor.DESCRIPTOR.equals(edge.getLabel())) {
					updateProperties(leaf.getData(), vnode, false);
					continue;
				}
				
				if(leaf.isLeaf() || !isChildEdge(edge))
					continue;
				//First remove edges that don't exist any more, or update the labels
				model = (IModelNode<IDescriptor>) leaf;
				Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>,String>> iterator = model.getChildren().entrySet().iterator();
				found = false;
				while( iterator.hasNext() ) {
					Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
					if( !isMatch( vnode, entry.getKey()))
						continue;
					if( !edge.getLabel().equals(entry.getValue())) {
						graph.removeEdge(edge);
						vertex.addEdge(entry.getValue(), vnode);
					}
					found = true;
				}
				if(!found)
					graph.removeEdge(edge);
			}
			if( leaf.isLeaf())
				return found;
				
			//Update the children
			model = (IModelNode<IDescriptor>) leaf;
			Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>,String>> iterator = model.getChildren().entrySet().iterator();
			result = found;
			found = false;
			while( iterator.hasNext() ) {
				Vertex vnode = null;
				Edge edge = null;
				Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
				edges = vertex.getEdges(Direction.OUT).iterator();
				while( edges.hasNext()) {
					edge = edges.next();
					if( !isChildEdge(edge)) {
						found = true;
						continue;
					}
					vnode = edge.getVertex(Direction.IN);
					if( !isMatch( vnode, entry.getKey()))
						continue;
					found = true;
					update( vnode, entry.getKey());
				}
				if( found == false ) {
					String str = domain.getDomain().replace(".", "_");
					OrientModelTypeAdapter.findOrCreateVertex(graph, str, entry.getKey().getDescriptor().getBase());				
					vnode = graph.addVertex(ModelDatabase.S_CLASS + IDescriptor.DESCRIPTORS);
					OrientModelTypeAdapter.fill(vnode, entry.getKey().getData().getBase());
					vertex.addEdge(IDescriptor.DESCRIPTOR, vnode);
				}
			}
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	protected void updateProperties( IDescriptor descriptor, Vertex vertex, boolean include ) {
		if(!include ) {
			for( String key: vertex.getPropertyKeys()) {
				if( IDescriptor.Attributes.ID.name().equals(key) || IDescriptor.Attributes.CREATE_DATE.name().equals(key))
					continue;
				Object attr = descriptor.get(key);
				if( attr != null )
					vertex.removeProperty(key);
			}
		}

		Iterator<Map.Entry<String, String>> iterator = descriptor.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			vertex.setProperty(entry.getKey(), entry.getValue());
		}
	}

	public int remove( long id ) {
		return remove( id, true );
	}
	
	public int remove( long id, boolean removeChildren ) {
		int counter = 0;
		OrientGraph graph = this.service.getGraph();
		Iterable<Vertex> iterable = graph.getVertices(IDescriptor.Attributes.ID.name(), String.valueOf(id));
		if( iterable == null )
			return counter;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			counter += remove( vertex, counter, removeChildren );
		}		
		return counter;
	}

	public int remove( long[] ids ) {
		return remove( ids, true );
	}

	public int remove( long[] ids, boolean removeChildren ) {
		int counter = 0;
		OrientGraph graph = this.service.getGraph();
		Iterable<Vertex> iterable = graph.getVertices();
		if( iterable == null )
			return counter;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			String idstr = vertex.getProperty(IDescriptor.Attributes.ID.name());
			long id = Long.parseLong(idstr);
			for( long check: ids ) {
				if( check == id )
					counter += remove( vertex, counter, removeChildren );
			}
		}		
		return counter;
	}

	protected int remove( Vertex vertex, int counter, boolean removeChildren ) {
		if( vertex == null )
			return counter;
		OrientGraph graph = this.service.getGraph();	
		Iterator<Edge> iterator = vertex.getEdges(Direction.OUT).iterator();
		int index=  counter;
		while( iterator.hasNext()) {
			Edge edge = iterator.next();
			Vertex child = edge.getVertex(Direction.IN);
			graph.removeEdge(edge);
			if(( IDescriptor.DESCRIPTOR.equals( edge.getLabel())) || ( IModelLeaf.IS_PARENT.equals( edge.getLabel())))
				continue;
			if(removeChildren && countEdges( child,  Direction.OUT) > 0)
				index += remove( child, index, removeChildren );
			}
		graph.removeVertex(vertex);
		return index;
	}

	/**
	 * Remove the children from the model with the given id.
	 * Returns true if one or more children were removed
	 * @param id
	 * @param children
	 * @return
	 */
	public boolean removeChildren( long id, long[] children ) {
		boolean result = false;
		OrientGraph graph = this.service.getGraph();
		Iterable<Vertex> iterable = graph.getVertices(IDescriptor.Attributes.ID.name(), String.valueOf(id));
		if( iterable == null )
			return false;
		Iterator<Vertex> iterator = iterable.iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();			
			Iterator<Edge> edges = vertex.getEdges(Direction.BOTH).iterator();
			while( edges.hasNext()) {
				Edge edge = edges.next();
				Vertex other = getOther( edge, vertex);
				for( long check: children ) {
					if( getId(other).equals(String.valueOf(check))) {
						graph.removeEdge(edge);
						result = true;
					}
				}
				if( !hasEdges( other))
					graph.removeVertex(other);
			}
		}		
		return result;
	}

	protected int countEdges( Vertex vertex, Direction direction ) {
		int counter = 0;
		Iterator<Edge> iterator = vertex.getEdges(direction).iterator();
		while( iterator.hasNext()) {
			iterator.next();
			counter++;
		}
		return counter;
	}
	
	public static boolean hasEdges( Vertex vertex ) {
		Iterator<Edge> edges = vertex.getEdges(Direction.BOTH).iterator();		
		return edges.hasNext(); 
	}

	public static boolean hasEdges( Vertex vertex, Direction direction ) {
		Iterator<Edge> edges = vertex.getEdges(direction).iterator();		
		return edges.hasNext(); 
	}

	public static Vertex getOther( Edge edge, Vertex vertex ) {
		Vertex check = edge.getVertex( Direction.IN); 
		return !check.equals(vertex)?check: edge.getVertex(Direction.OUT);
	}

	/**
	 * Get the id of the vertex
	 * @param vertex
	 * @return
	 */
	protected static boolean isMatch( Vertex vertex, IModelLeaf<? extends IDescriptor> leaf ) {
		String id = vertex.getProperty(IDescriptor.Attributes.ID.name());
		if( StringUtils.isEmpty(id))
			return false;
		return id.equals( leaf.get( IDescriptor.Attributes.ID.name() ));
	}

	/**
	 * Get the id of the vertex
	 * @param vertex
	 * @return
	 */
	protected static String getId( Vertex vertex ) {
		return vertex.getProperty(IDescriptor.Attributes.ID.name());
	}
	
	/**
	 * A child can have any label, except descriptor and is_parent
	 * @param edge
	 * @return
	 */
	protected static boolean isChildEdge( Edge edge) {
		String label = edge.getLabel();
		if( IModelLeaf.IS_CHILD.equals(label))
			return true;
		return !( IModelLeaf.IS_PARENT.equals(label) || IDescriptor.DESCRIPTOR.equals(label ));
	}
}
