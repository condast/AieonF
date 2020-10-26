package org.aieonf.orientdb.serialisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.implicit.IImplicitAieon;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.serialise.AbstractModelTypeAdapter;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.orientdb.graph.ModelFactory;

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
public class ModelTypeAdapter extends AbstractModelTypeAdapter<Vertex, Vertex> {

	private OrientGraph graph;
		
	private IDomainAieon domain; 
	
	public ModelTypeAdapter( IDomainAieon domain, OrientGraph graph) {
		super();
		this.domain = domain;
		this.graph = graph;
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
	protected Vertex onCreateNode(long id, boolean leaf) {
		String str = domain.getDomain().replace(".", "_");
		return findOrCreateVertex( this.graph, str, id);
	}

	@Override
	protected boolean onAddChild(Vertex model, Vertex child, boolean reversed, String label) {
		if( child == null )
			return  false;
		addChild( model, child, reversed, label);
		return true;
	}

	@Override
	protected Vertex onSetDescriptor(Vertex vertex, IConceptBase base) {
		Iterator<Edge> edges = vertex.getEdges(Direction.IN, IDescriptor.DESCRIPTOR).iterator();
		Vertex descriptor = null;
		while(( descriptor == null ) &&  edges.hasNext()) {
			Edge edge = edges.next();
			descriptor = edge.getVertex(Direction.IN);
		}
		if( descriptor == null ) {
			if( ImplicitAieon.isImplicit(base)) {
				IImplicitAieon<IDescriptor> implicit = new ImplicitAieon( base );
				Iterator<Vertex> iterator = graph.getVerticesOfClass(IDescriptor.DESCRIPTORS ).iterator();
				while(( descriptor == null ) && iterator.hasNext()) {
					Vertex test=  iterator.next();
					IDescriptor concept = new Descriptor( new VertexConceptBase( test ));
					if( implicit.accept(concept) )
						descriptor = test;
				}
			}
		}
		if( descriptor == null ) {
			descriptor = graph.addVertex( ModelFactory.S_CLASS + IDescriptor.DESCRIPTORS);
			descriptor.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(graph.countVertices()));
			Iterator<Map.Entry<String, String>> iterator = base.iterator();
			while( iterator.hasNext() ) {
				Map.Entry<String, String> entry = iterator.next();
				descriptor.setProperty(entry.getKey(), entry.getValue());
			}		
		}
		vertex.addEdge(IDescriptor.DESCRIPTOR, descriptor);
		return descriptor;
	}
	
	@Override
	protected boolean onAddProperty(Vertex node, String key, String value) {
		node.setProperty(key, value);
		return true;
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

	protected static Vertex findOrCreateVertex( OrientGraph graph, String domain, long id ) {
		Vertex vertex = null;
		long newid = id;
		if( id>0) {
			Collection<Vertex> vertices = findVertices( graph, domain, id);
			if( !Utils.assertNull(vertices)) {
				vertex = vertices.iterator().next();
				return vertex;
			}else {
				newid = graph.countVertices();
				vertex = graph.addVertex( domain);				
			}
		}else {
			newid = graph.countVertices();
			vertex = graph.addVertex( domain );
		}
		vertex.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(newid));
		return vertex;
	}
	
	public static void addChild( Vertex parent, Vertex child, boolean reverse, String label ) {
		if( !reverse ) {
			parent.addEdge(label,child);
			child.addEdge(IModelNode.IS_PARENT, parent);
			return;
		}
		child.addEdge(label, parent);
		parent.addEdge( IModelNode.IS_PARENT, child );
		String depth = IModelLeaf.Attributes.DEPTH.name();
		String pdepth = parent.getProperty( depth);
		String cdepth = child.getProperty(  depth );
		parent.setProperty( depth, cdepth);
		child.setProperty( depth, pdepth);
	}
}