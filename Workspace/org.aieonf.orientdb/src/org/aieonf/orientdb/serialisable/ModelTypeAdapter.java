package org.aieonf.orientdb.serialisable;

import java.util.logging.Logger;

import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.serialise.AbstractModelTypeAdapter;
import org.aieonf.orientdb.core.ModelNode;
import org.aieonf.orientdb.graph.ModelFactory;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelTypeAdapter extends AbstractModelTypeAdapter<Vertex, Vertex> {

	private OrientGraph graph;
	
	private IDomainAieon domain;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public ModelTypeAdapter( IDomainAieon domain, OrientGraph graph) {
		super();
		this.graph = graph;
		this.domain = domain;
	}

	@Override
	protected IModelLeaf<IDescriptor> onTransform(Vertex node) {
		return new ModelNode( this.graph, this.domain, node );
	}

	@Override
	protected Vertex onCreateNode(long id, boolean leaf) {
		Vertex vertex = graph.addVertex( domain.getDomain());
		vertex.setProperty(IDescriptor.Attributes.ID.name(), String.valueOf(graph.countVertices()));
		return vertex;
	}

	@Override
	protected boolean onAddChild(Vertex model, Vertex child, String label) {
		graph.addEdge(domain.getDomain(), model, child, label);
		return true;
	}

	@Override
	protected Vertex onSetDescriptor(Vertex node) {
		Vertex descriptor = graph.addVertex( ModelFactory.S_CLASS + IDescriptor.DESCRIPTORS);
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
}