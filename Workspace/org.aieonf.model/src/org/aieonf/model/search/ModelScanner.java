package org.aieonf.model.search;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelNode;
import org.aieonf.model.filter.IModelFilter;

public class ModelScanner<D extends IDescriptor>{

	private IModelLeaf<? extends IDescriptor> model;

	public ModelScanner( IModelLeaf<? extends IDescriptor> model ) {
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
		getDescriptors( results, name, model );
		return results.toArray( new IDescriptor[ results.size() ]);
	}
	
	@SuppressWarnings("unchecked")
	protected void getDescriptors( Collection<IDescriptor> results, String name, IModelLeaf<? extends IDescriptor> current ){
		if( name.equals( current.getData().getName() ))
			results.add( current.getData() );
		if(!( current instanceof IModelNode ))
			return;
		IModelNode<D> node = (IModelNode<D>) current;
		for( IModelLeaf<?> child: node.getChildren().keySet() )
			getDescriptors( results, name, (IModelLeaf<? extends IDescriptor>) child );
	}

	@SuppressWarnings("unchecked")
	public Collection<IModelLeaf<IDescriptor>> get(IDescriptor descriptor) throws ParseException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		getModel( results, descriptor, (IModelLeaf<IDescriptor>) model );
		return results;
	}

	@SuppressWarnings("unchecked")
	protected void getModel( Collection<IModelLeaf<IDescriptor>> results, IDescriptor descriptor, IModelLeaf<IDescriptor> current ){
		if( descriptor.getName().equals( current.getData().getName() )){
			if(( descriptor.getID()<0) || ( descriptor.getID() == current.getData().getID()) ){
				if( descriptor.getVersion() == current.getData().getVersion() )
					results.add( current );
			}
		}
		if(!( current instanceof IModelNode ))
			return;
		IModelNode<D> node = (IModelNode<D>) current;
		for( IModelLeaf<?> child: node.getChildren().keySet() )
			getModel( results, descriptor, (IModelLeaf<IDescriptor>) child );
	}

	@SuppressWarnings("unchecked")
	public Collection<IModelLeaf<IDescriptor>> search(IModelFilter<D,IModelLeaf<D>> filter) throws ParseException {
		Collection<IModelLeaf<IDescriptor>> results = new ArrayList<IModelLeaf<IDescriptor>>();
		searchModel( results, filter, (IModelLeaf<D>) model );
		return results;
	}

	@SuppressWarnings("unchecked")
	protected void searchModel( Collection<IModelLeaf<IDescriptor>> results, IModelFilter<D,IModelLeaf<D>> filter, IModelLeaf<D> current ){
		if( filter.accept( current ))
			results.add( (IModelLeaf<IDescriptor>) current );
		if(!( current instanceof IModelNode ))
			return;
		IModelNode<D> node = (IModelNode<D>) current;
		for( IModelLeaf<?> child: node.getChildren().keySet() )
			searchModel( results, filter, (IModelLeaf<D>) child );
	}

}
