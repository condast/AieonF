package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelNode extends ModelLeaf implements IModelNode<IDescriptor> {

	public static final String S_ERR_NO_DESCRIPTOR_FOUND = "The descriptor was not found for this model";
		
	private transient OrientGraph graph;
	
	private transient IDomainAieon domain; 

	public ModelNode( OrientGraph graph, IDomainAieon domain, Vertex vertex ) {
		this( graph, domain, null, vertex );
	}
	
	public ModelNode( OrientGraph graph, IDomainAieon domain, IModelNode<?> parent, Vertex vertex ) {
		super( graph, domain, parent, vertex );
		this.graph = graph;
		this.domain = domain;
		Map<IModelLeaf<? extends IDescriptor>, String> children = null;	
		boolean reverse = isReverse(); 
		if( reverse ) {
			Iterator<Edge> iterator = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT).iterator();
			while( iterator.hasNext() ) {
				Edge edge = iterator.next();
				addChild( new ModelNode( graph, domain, edge.getVertex(com.tinkerpop.blueprints.Direction.IN)));							
			}
		}else	
			getChildren();
		super.setLeaf(Utils.assertNull(children));
	}
	
	@Override
	public IModelNode.Direction getDirection() {
		String str = super.get(IModelNode.Attributes.DIRECTION.name());
		return StringUtils.isEmpty(str)? Direction.UNI_DIRECTIONAL: Direction.valueOf(str);
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child) {
		return addChild(child, IS_CHILD);
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child, String type) {
		Vertex vertex = getVertex();
		ModelLeaf leaf = (ModelLeaf) child;
		addChild(domain, graph, vertex, leaf.getVertex(), type);
		super.setLeaf(false);
		return true;
	}

	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child) {
		Vertex vertex = getVertex();
		ModelLeaf leaf = (ModelLeaf) child;
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN).iterator();
		if(!edges.hasNext())
			return false;
		
		while( edges.hasNext()) {
			Edge edge = edges.next();
			if( IDescriptor.DESCRIPTOR.equals( edge.getLabel()))
				continue;
			if( !leaf.getVertex().equals( edge.getVertex(com.tinkerpop.blueprints.Direction.IN)))
				continue;
			Vertex vchild = edge.getVertex(com.tinkerpop.blueprints.Direction.OUT);
			Iterator<Edge> vedges = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN, IModelNode.IS_PARENT).iterator();
			while( vedges.hasNext())
			graph.removeEdge(edge);
			return true;
		}
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		super.setLeaf(Utils.assertNull(children));
		return true;
	}

	@Override
	public Map<IModelLeaf<? extends IDescriptor>, String> getChildren() {
		Map<IModelLeaf<? extends IDescriptor>, String> children = new HashMap<>();
		Vertex vertex = getVertex();
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT).iterator();
		if(!edges.hasNext())
			return children;
		
		while( edges.hasNext()) {
			Edge edge = edges.next();
			if( IDescriptor.DESCRIPTOR.equals( edge.getLabel()))
				continue;
			Vertex child = edge.getVertex(com.tinkerpop.blueprints.Direction.OUT);
			children.put(new ModelNode( graph, domain, this, child), edge.getLabel());
		}
		return children;
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getChild(IDescriptor descriptor) {
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		for( IModelLeaf<? extends IDescriptor> child: children.keySet() ) {
			if( child.getData().equals(descriptor))
				return child;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<>();
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		for( IModelLeaf<? extends IDescriptor> child: children.keySet() ) {
			if( child.getData().getName().equals(name))
				results.add(child);
		}
		return results.toArray( new IModelLeaf[ results.size()]);
	}

	@Override
	public String getChildIdentifier(IModelLeaf<? extends IDescriptor> child) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		return Utils.assertNull(children);
	}

	@Override
	public int nrOfchildren() {
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		return children.size();
	}
	
	public static void addChild( IDomainAieon domain, OrientGraph graph, Vertex parent, Vertex child, String label ) {
		String rev = parent.getProperty(IModelLeaf.Attributes.REVERSE.name());
		boolean reverse = StringUtils.isEmpty(rev)?false: Boolean.parseBoolean(rev);
		if( reverse ) {
			graph.addEdge(domain.getDomain(), child, parent, label);
			graph.addEdge(domain.getDomain(), parent, child, IModelNode.IS_PARENT);
		}else {
			graph.addEdge(domain.getDomain(), parent, child, label);
			graph.addEdge(domain.getDomain(), child, parent, IModelNode.IS_PARENT);
		}
	}
}