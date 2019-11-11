package org.aieonf.orientdb.graph;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientDBModel extends OrientDBModelLeaf implements IModelNode<IDescriptor> {

	private Collection<OrientDBModelLeaf> children;

	public OrientDBModel() {		
	}

	public OrientDBModel( Vertex vertex, IDescriptor descriptor ) {
		super( vertex, descriptor);
	}

	public OrientDBModel( Object id, OrientGraph graph, IDescriptor descriptor ) {
		this( null, id, graph, descriptor);
	}
	
	public OrientDBModel( OrientDBModel parent, Object id, OrientGraph graph, IDescriptor descriptor ) {
		super( parent, id, graph, descriptor );
		children = new ArrayList<>();
		if( parent != null )
			parent.addChild(this);
	}

	@Override
	public boolean addChild(IModelLeaf<? extends IDescriptor> child) {
		OrientDBModelLeaf leaf = (OrientDBModelLeaf) child;
		boolean result = false;
		OrientGraph graph = super.getGraph();
		try {
			graph.addEdge(null, getVertex(), leaf.getVertex(), IModelLeaf.Attributes.CHILD.name());
			graph.commit();
			super.setLeaf(false);
			children.add(leaf);
			result = true;
		}
		catch( Exception ex ) {
			graph.rollback();
			ex.printStackTrace();
		}	
		return result;
	}

	@Override
	public boolean removeChild(IModelLeaf<? extends IDescriptor> child) {
		OrientDBModelLeaf leaf = (OrientDBModelLeaf) child;
		Vertex last = leaf.getVertex();
		OrientGraph graph = super.getGraph();
		graph.removeVertex(last);
		children.remove(child);
		return true;	
	}

	
	@Override
	public Collection<IModelLeaf<? extends IDescriptor>> getChildren() {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<>();
		for( IModelLeaf<? extends IDescriptor> child: this.children)
			results.add(child);
		return results;
	}

	/**
	 * Get the child with the given descriptor
	 * @param descriptor
	 * @return IModelNode<? extends IDescriptor>
	 */
	@Override
	public IModelLeaf<? extends IDescriptor> getChild( IDescriptor descriptor )
	{
		for( IModelLeaf<? extends IDescriptor> model: this.children ){
			if( model.getDescriptor().equals( descriptor ))
				return model;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModelLeaf<? extends IDescriptor>[] getChildren(String name) {
		Collection<IModelLeaf<? extends IDescriptor>> results = new ArrayList<IModelLeaf<? extends IDescriptor>>();
		for( IModelLeaf<? extends IDescriptor> model: this.children ){
			if( model.getDescriptor().getName().equals( name ))
				results.add( model );
		}
		return results.toArray( new IModelLeaf[ results.size() ]);
	}

	/**
	 * Returns true if the model has children
	 * @return
	 */
	@Override
	public boolean hasChildren()
	{
		return ( this.children.isEmpty() == false );
	}

	/**
	 * Get the number of children
	 * @return
	 */
	@Override
	public int nrOfchildren()
	{
		return this.children.size();
	}

}
