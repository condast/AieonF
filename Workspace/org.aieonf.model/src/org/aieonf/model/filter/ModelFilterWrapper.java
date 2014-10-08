package org.aieonf.model.filter;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.util.filter.AbstractFilter.Mode;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public class ModelFilterWrapper<T extends IDescriptor> implements
		IModelFilter<T> {

	private IFilter<IModelLeaf<T>> filter;
	
	public ModelFilterWrapper( IFilter<IModelLeaf<T>> filter ) {
		super();
		this.filter = filter;
	}

	@Override
	public String getName() {
		return filter.getName();
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
	public boolean accept(Object obj) throws FilterException {
		return filter.accept(obj);
	}

	@Override
	public Collection<IModelLeaf<T>> doFilter(
			Collection<IModelLeaf<T>> collection) throws FilterException {
		return filter.doFilter(collection);
	}

	@Override
	public Collection<IModelLeaf<T>> doFilter(
			Collection<IModelLeaf<T>> collection, int amount)
			throws FilterException {
		return filter.doFilter(collection, amount);
	}

	@Override
	public Collection<IModelLeaf<T>> getRejected() {
		return filter.getRejected();
	}

	@Override
	public boolean acceptChild(IModelLeaf<T> child) {
		return filter.accept(child );
	}

}
