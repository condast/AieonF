package org.aieonf.model.filter;

import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.filter.AbstractFilter.Mode;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public class ModelFilterWrapper<T extends IDescriptor> implements
		IModelFilter<T> {

	private static final String S_ERR_MIN_DEPTH_WRONG = "The minimum depth must be equal to, or larger than zero";
	private IFilter<IModelLeaf<T>> filter;
	
	private int minDepth, maxDepth;
	
	public ModelFilterWrapper( IFilter<IModelLeaf<T>> filter ) {
		this( filter, 0, -1 );
	}

	public ModelFilterWrapper( IFilter<IModelLeaf<T>> filter, int minDepth, int maxDepth ) {
		super();
		this.filter = filter;
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public ModelFilterWrapper( IFilter<IModelLeaf<T>> filter, int maxDepth ) {
		this( filter, 0, maxDepth );
	}

	@Override
	public String getName() {
		return filter.getName();
	}
	
	@Override
	public int getMinDepth() {
		return this.minDepth;
	}

	@Override
	public int getMaxDepth() {
		return this.maxDepth;
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
		if( !acceptDepth(minDepth, maxDepth, obj ))
			return false;
		return filter.accept( obj );
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
		if( !acceptDepth(minDepth, maxDepth, child ))
				return false;
		return filter.accept( child );
	}

	/**
	 * Accept the object with a check on minimum and maximum depth
	 * @param filter
	 * @param minDepth
	 * @param maxDepth
	 * @param obj
	 * @return
	 */
	public static boolean acceptDepth( int minDepth, int maxDepth, Object obj ){
		if( obj instanceof IModelLeaf<?>){
			IModelLeaf<?> leaf = (IModelLeaf<?>) obj;
			if( minDepth > leaf.getDepth() )
				return false;
			if(( maxDepth >=0 ) && ( maxDepth < leaf.getDepth() ))
				return false;
		}
		return true;
	}
}