package org.aieonf.model.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.AbstractFilter.Mode;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public class ModelFilter<T extends IDescriptor, M extends IDescribable> implements
		IModelFilter<T,M> {

	private IFilter<IDescriptor> filter;
	
	private Collection<M> rejected;
	
	public ModelFilter( IFilter<IDescriptor> filter ) {
		super();
		this.filter = filter;
		rejected = new ArrayList<M>();
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
	public boolean accept( M leaf) throws FilterException {
		return filter.accept( leaf.getDescriptor() );
	}

	@Override
	public Collection<M> doFilter(Collection<M> collection) throws FilterException {
		Collection<M> results = new ArrayList<M>();
		rejected.clear();
		for( M model: collection ){
			if(( filter == null ) || filter.accept( model.getDescriptor()))
				results.add(model);
			else
				rejected.add( model);
		}
		return results;
	}

	@Override
	public Collection<M> doFilter(
			Collection<M> collection, int amount)
			throws FilterException {
		Collection<M> results = new ArrayList<M>();
		rejected.clear();
		for( M model: collection ){
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
	public Collection<M> getRejected() {
		return rejected;
	}
	
	@Override
	public boolean acceptChild(M child) {
		return filter.accept( child.getDescriptor() );
	}
}
