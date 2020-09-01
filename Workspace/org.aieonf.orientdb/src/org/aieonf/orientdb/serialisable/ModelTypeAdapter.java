package org.aieonf.orientdb.serialisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.implicit.IImplicitAieon;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.serialise.AbstractModelTypeAdapter;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.core.VertexConceptBase;
import org.aieonf.orientdb.graph.ModelFactory;
import org.aieonf.osgi.selection.IActiveDomainProvider;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelTypeAdapter extends AbstractModelTypeAdapter<Vertex, Vertex> {

	private OrientGraph graph;
	
	private IActiveDomainProvider provider;
	
	public ModelTypeAdapter( IActiveDomainProvider provider, OrientGraph graph) {
		super();
		this.graph = graph;
		this.provider = provider;
	}

	@Override
	protected IModelLeaf<IDescriptor> onTransform(Vertex node) {
		IDomainAieon domain = this.provider.getActiveDomain();
		return new ModelNode( this.graph, domain, node );
	}

	@Override
	protected Vertex onCreateNode(long id, boolean leaf) {
		IDomainAieon domain = this.provider.getActiveDomain();
		String str = domain.getDomain().replace(".", "_");
		return findOrCreateVertex( this.graph, str, id);
	}

	@Override
	protected boolean onAddChild(Vertex model, Vertex child, String label) {
		IDomainAieon domain = this.provider.getActiveDomain();
		ModelNode.addChild(domain, graph, model, child, label);
		return true;
	}

	@Override
	protected Vertex onSetDescriptor(Vertex node, IConceptBase base) {
		Iterator<Edge> edges = node.getEdges(Direction.IN, IDescriptor.DESCRIPTOR).iterator();
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
		}
		node.addEdge(IDescriptor.DESCRIPTOR, descriptor);
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
		Iterator<Vertex> iterator = graph.getVerticesOfClass(domain).iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();
			String check = vertex.getProperty(IDescriptor.Attributes.ID.name());
			if( StringUtils.isEmpty(check) ||( Long.parseLong(check) != id))
				continue;
			results.add(vertex);
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
}