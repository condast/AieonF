package org.aieonf.model.filter;

import java.util.Collection;

import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.commons.filter.AbstractFilter.Mode;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.FilterFactory.Filters;
import org.aieonf.model.core.IModelLeaf;

public class ModelFilterWrapper<T extends IDescriptor, M extends IDescribable> implements
		IModelFilter<T,M> {

	private static final String S_ERR_MIN_DEPTH_WRONG = "The minimum depth must be equal to, or larger than zero";
	private IAttributeFilter<M> filter;
	
	private int minDepth, maxDepth;
	
	public ModelFilterWrapper( IAttributeFilter<M> filter ) {
		this( filter, 0, -1 );
	}

	public ModelFilterWrapper( IAttributeFilter<M> filter, int minDepth, int maxDepth ) {
		super();
		this.filter = filter;
		if( minDepth < 0 )
			throw new IllegalArgumentException( S_ERR_MIN_DEPTH_WRONG);
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public ModelFilterWrapper( IAttributeFilter<M> filter, int maxDepth ) {
		this( filter, 0, maxDepth );
	}

	@Override
	public String getName() {
		return filter.getName();
	}
	
	@Override
	public Filters getType() {
		return Filters.ATTRIBUTES;
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
	public boolean accept( M obj) throws FilterException {
		if( !acceptDepth(minDepth, maxDepth, obj ))
			return false;
		return filter.accept( obj );
	}

	@Override
	public Collection<M> doFilter(
			Collection<M> collection) throws FilterException {
		return filter.doFilter(collection);
	}

	@Override
	public Collection<M> doFilter(
			Collection<M> collection, int amount)
			throws FilterException {
		return filter.doFilter(collection, amount);
	}

	@Override
	public Collection<M> getRejected() {
		return filter.getRejected();
	}
	
	@Override
	public boolean acceptChild( M child) {
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

	@Override
	public String getReference() {
		return filter.getReference();
	}

	@Override
	public String getValue() {
		return filter.getValue();
	}
}
