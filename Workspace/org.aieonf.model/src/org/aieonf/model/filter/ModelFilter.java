package org.aieonf.model.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.AbstractFilter.Mode;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

public class ModelFilter<T extends IDescriptor> implements
		IModelFilter<T> {

	private IFilter<T> filter;
	
	private Collection<IModelLeaf<T>> rejected;
	
	public ModelFilter( IFilter<T> filter ) {
		super();
		this.filter = filter;
		rejected = new ArrayList<IModelLeaf<T>>();
	}

	@Override
	public String getName() {
		return filter.getName();
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
	public String getRule() {
		return filter.getRule();
	}

	@Override
	public Mode getMode() {
		return filter.getMode();
	}

	@Override
	public int getAmount() {
		return filter.getAmount();
	}

	@Override
	public void setAmount(int amount) {
		filter.setAmount(amount);
	}

	@Override
	public boolean accept( IModelLeaf<T> leaf) throws FilterException {
		return filter.accept( leaf.getDescriptor() );
	}

	@Override
	public Collection<IModelLeaf<T>> doFilter(Collection<IModelLeaf<T>> collection) throws FilterException {
		Collection<IModelLeaf<T>> results = new ArrayList<IModelLeaf<T>>();
		rejected.clear();
		for( IModelLeaf<T> model: collection ){
			if( filter.accept( model.getDescriptor()))
				results.add(model);
			else
				rejected.add( model);
		}
		return results;
	}

	@Override
	public Collection<IModelLeaf<T>> doFilter(
			Collection<IModelLeaf<T>> collection, int amount)
			throws FilterException {
		Collection<IModelLeaf<T>> results = new ArrayList<IModelLeaf<T>>();
		rejected.clear();
		for( IModelLeaf<T> model: collection ){
			if( filter.accept( model.getDescriptor())){
				results.add(model);
				if( results.size() == amount )
					return results;
			}else
				rejected.add( model);
		}
		return results;
	}

	@Override
	public Collection<IModelLeaf<T>> getRejected() {
		return rejected;
	}
	
	@Override
	public boolean acceptChild(IModelLeaf<T> child) {
		return filter.accept( child.getDescriptor() );
	}
}
