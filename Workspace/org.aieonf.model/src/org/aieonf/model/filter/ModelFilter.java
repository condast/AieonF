package org.aieonf.model.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.DescribableFilter;
import org.aieonf.model.core.IModelLeaf;

public class ModelFilter<D extends IDescriptor, M extends IModelLeaf<D>> extends DescribableFilter<M> implements
		IModelFilter<D,M> {

	
	public ModelFilter( IAttributeFilter<IDescriptor> filter ) {
		super( filter );
	}

	@Override
	public int getMinDepth() {
		return 0;
	}

	@Override
	public int getMaxDepth() {
		return 0;
	}

	@Override
	public boolean accept( M leaf ) throws FilterException {
		return super.getFilter().accept( leaf.getData() );
	}

	@Override
	public Collection<M> doFilter(Collection<M> collection) throws FilterException {
		Collection<M> results = new ArrayList<M>();
		IAttributeFilter<IDescriptor> filter = super.getFilter();
		getRejected().clear();
		for( M model: collection ){
			if(( filter == null ) || filter.accept( model.getDescriptor()))
				results.add(model);
			else
				getRejected().add( model);
		}
		return results;
	}

	@Override
	public Collection<M> doFilter(
			Collection<M> collection, int amount)
			throws FilterException {
		Collection<M> results = new ArrayList<M>();
		getRejected().clear();
		IAttributeFilter<IDescriptor> filter = super.getFilter();
		for( M model: collection ){
			if( filter.accept( model.getData())){
				results.add(model);
				if( results.size() == amount )
					return results;
			}else
				getRejected().add( model);
		}
		return results;
	}

	
	@Override
	public boolean acceptChild(M child) {
		IAttributeFilter<IDescriptor> filter = super.getFilter();
		return filter.accept( child.getData() );
	}
}
