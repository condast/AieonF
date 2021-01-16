package org.aieonf.orientdb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.aieonf.commons.Utils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.orientdb.serialisable.OrientModelTypeAdapter;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ModelNode extends ModelLeaf implements IModelNode<IDescriptor> {

	public static final String S_ERR_NO_DESCRIPTOR_FOUND = "The descriptor was not found for this model";
		
	//Needed to remove children
	private transient OrientGraph graph;
	
	private Map<IModelLeaf<? extends IDescriptor>, String> children;
	
	/**
	 * It is possible to reverse the parent-child relationship, 
	 * which allows the database to create a more convenient layout.
	 * As orient db merely swaps the edge, the boolean is discarded
	 */
	private transient boolean reverse;
	
	public ModelNode( OrientGraph graph, Vertex vertex ) {
		this( graph, null, vertex );
	}
	
	public ModelNode( OrientGraph graph, IModelNode<?> parent, Vertex vertex ) {
		super( parent, vertex );
		vertex.setProperty(IDescriptor.Attributes.CLASS.name(), IModelNode.class.getCanonicalName());
		this.graph = graph;
		this.reverse = false;
		children = new HashMap<>();
		super.setLeaf( !hasChildren());
		if( !isLeaf())
			parseChildren();
	}

	protected void parseChildren() {
		Vertex vertex = getVertex();
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT).iterator();
		if(!edges.hasNext())
			return;
		
		while( edges.hasNext()) {
			Edge edge = edges.next();
			if( IDescriptor.DESCRIPTOR.equals( edge.getLabel()))
				continue;
			Vertex vchild = edge.getVertex(com.tinkerpop.blueprints.Direction.IN);
			if( vchild == null )
				continue;
			if( vertex.getId().equals(vchild.getId()))
				graph.removeEdge(edge);
			IModelLeaf<IDescriptor> child = new ModelNode( graph, this, vchild); 
			//logger.fine( child.getData().toString());	
			children.put(child, edge.getLabel());
		}
	}

	@Override
	public boolean isReverse() {
		return this.reverse;
	}

	@Override
	public void setReverse( boolean choice ) {
		this.reverse = choice;
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child) {
		return addChild(child, IS_CHILD);
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child, String type) {
		Vertex vertex = getVertex();
		ModelLeaf leaf = (ModelLeaf) child;
		OrientModelTypeAdapter.addChild(vertex, leaf.getVertex(), this.reverse, type);
		children.put(leaf, type);
		super.setLeaf(false);
		return true;
	}
	
	@Override
	public Map<IModelLeaf<? extends IDescriptor>, String> getChildren() {
		return children;
	}

	@Override
	public IModelLeaf<? extends IDescriptor> getChild(IDescriptor descriptor) {
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
		Iterator<Map.Entry<IModelLeaf<? extends IDescriptor>, String>> iterator = getChildren().entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<IModelLeaf<? extends IDescriptor>, String> entry = iterator.next();
			if( entry.getValue().equals(name))
				results.add(entry.getKey());
		}
		return results.toArray( new IModelLeaf[ results.size()]);
	}

	@Override
	public String getChildIdentifier(IModelLeaf<? extends IDescriptor> child) {
		return child.get(IConcept.Attributes.IDENTIFIER.name());
	}

	@Override
	public boolean hasChildren() {
		Vertex vertex = getVertex();
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.OUT).iterator();
		if(!edges.hasNext())
			return false;
		
		while( edges.hasNext()) {
			Edge edge = edges.next();
			if( !IDescriptor.DESCRIPTOR.equals( edge.getLabel()))
				return true;
		}
		return false;
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
			return true;
		}
		Map<IModelLeaf<? extends IDescriptor>, String> children = getChildren();
		children.remove(child);
		super.setLeaf(Utils.assertNull(children));
		return true;
	}

	@Override
	public void removeAllChildren() {
		Vertex vertex = getVertex();
		Iterator<Edge> edges = vertex.getEdges(com.tinkerpop.blueprints.Direction.IN).iterator();
		if(!edges.hasNext())
			return;
		
		while( edges.hasNext()) {
			Edge edge = edges.next();
			if( IDescriptor.DESCRIPTOR.equals( edge.getLabel()))
				continue;
		}
		children.clear();
		super.setLeaf(true);
	}

	@Override
	public int nrOfchildren() {
		return children.size();
	}
}