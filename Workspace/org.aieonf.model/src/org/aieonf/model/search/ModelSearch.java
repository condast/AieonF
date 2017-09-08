package org.aieonf.model.search;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;

public class ModelSearch<T extends IDescriptor> {

	private IModelLeaf<T> model;

	public ModelSearch( IModelLeaf<T> model ) {
		super();
		this.model = model;
	}
	
	/**
	 * Get the descriptors with the given name
	 * @param name
	 * @return
	 */
	public IDescriptor[] getDescriptors( String name ){
		Collection<IDescriptor> results = new ArrayList<IDescriptor>();
		searchModel( results, name, model );
		return results.toArray( new IDescriptor[ results.size() ]);
	}
	
	@SuppressWarnings("unchecked")
	protected void searchModel( Collection<IDescriptor> results, String name, IModelLeaf<?> current ){
		if( name.equals( current.getDescriptor().getName() ))
			results.add( current.getDescriptor() );
		if(!( current instanceof IModelNode ))
			return;
		IModelNode<T> node = (IModelNode<T>) current;
		for( IModelLeaf<?> child: node.getChildren() )
			searchModel( results, name, child );
	}
}
