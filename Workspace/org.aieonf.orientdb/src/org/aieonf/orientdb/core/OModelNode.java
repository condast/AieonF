package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OModelNode<D extends IDescriptor> extends OModelLeaf<D> implements IModelNode<D> {

	private Map<IModelLeaf<?>, Edge> edges;

	public OModelNode( OrientGraph graph, Vertex vertex, D descriptor) {
		this( null, graph, vertex, descriptor);
	}
	
	public OModelNode(IModelNode<D> parent, OrientGraph graph, Vertex vertex, D descriptor) {
		super(parent, graph, vertex, descriptor);
		edges = new HashMap<>();
	}

	public OModelNode(OrientGraph graph, D descriptor) {
		super(graph, descriptor);
		edges = new HashMap<>();
	}

	public OModelNode(org.xml.sax.Attributes attributes) {
		this(null, null);
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child) {
		OModelLeaf<?> leaf = (OModelLeaf<?>) child;
		leaf.setParent(this);
		edges.put(child, leaf.getParentEdge());
		super.setLeaf(false);
		return true;
	}

	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child) {
		Edge edge = edges.remove(child);
		super.getGraph().removeEdge(edge);
		super.setLeaf( edges.isEmpty());
		return false;
	}

	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren() {
		return edges.keySet();
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getChild(IDescriptor descriptor) {
		for( IModelLeaf<?> leaf: edges.keySet()) {
			if( leaf.getDescriptor().equals(descriptor))
				return leaf;
		}
		return null;
	}

	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<?>> results = new ArrayList<>();
		for( IModelLeaf<?> leaf: edges.keySet()) {
			if( leaf.getDescriptor().getName().equals(name))
				results.add(leaf);
		}
		return null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean hasChildren() {
		return !this.edges.isEmpty();
	}

	@Override
	public int nrOfchildren() {
		return this.edges.size();
	}
}
