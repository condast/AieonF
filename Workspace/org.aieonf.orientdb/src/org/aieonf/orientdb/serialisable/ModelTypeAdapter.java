package org.aieonf.orientdb.serialisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.aieonf.commons.Utils;
import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.IConceptBase;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;
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
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

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
		graph.addEdge(domain.getDomain(), model, child, label);
		return true;
	}

	@Override
	protected Vertex onSetDescriptor(Vertex node) {
		IDomainAieon domain = this.provider.getActiveDomain();
		Iterator<Edge> edges = node.getEdges(Direction.IN, IDescriptor.DESCRIPTOR).iterator();
		Vertex descriptor = null;
		while(( descriptor == null ) &&  edges.hasNext()) {
			Edge edge = edges.next();
			descriptor = edge.getVertex(Direction.IN);
			IConceptBase base = new VertexConceptBase( descriptor );
			//Collection<Vertex> implicit = 
		}
		if( descriptor == null )
			descriptor = graph.addVertex( ModelFactory.S_CLASS + IDescriptor.DESCRIPTORS);
		descriptor.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(graph.countVertices()));
		node.addEdge(IDescriptor.DESCRIPTOR, descriptor);
		return descriptor;
	}
	
	@Override
	protected boolean onFillDescriptor(Vertex descriptor, String key, String value) {
		if( StringUtils.isEmpty(value))
			return false;
		String name = key.replace(".", "_");
		descriptor.setProperty(name, value);
		return true;
	}

	@Override
	protected boolean onAddProperty(Vertex node, String key, String value) {
		node.setProperty(key, value);
		return true;
	}

	/**
	 * Find the vertieces that are implicit to the given descriptor
	 * @param graph
	 * @param domain
	 * @param desrciptor
	 * @return
	 */
	protected static Collection<Vertex> findImplicit( OrientGraph graph, String domain, IActiveDomainProvider provider, IDescriptor reference ){
		Collection<Vertex> results = new ArrayList<>();
		IDescribablePredicate<IDescriptor> predicate = provider.getPredicates();
		if( !predicate.test(reference))
			return results;
		Iterator<Vertex> iterator = graph.getVerticesOfClass(domain).iterator();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();
			IDescriptor descriptor = new Descriptor(new VertexConceptBase( vertex ));
			int compare = predicate.compare(reference, descriptor);
			if( compare == 0)
				results.add(vertex);
		}
		return results;
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