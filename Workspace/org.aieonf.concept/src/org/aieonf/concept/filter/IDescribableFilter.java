package org.aieonf.concept.filter;

import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.filter.FilterFactory.Filters;

public interface IDescribableFilter<M extends IDescribable> extends IAttributeFilter<M> {
	
	public Filters getType();
	
	/**
	 * If true, the given child is accepted
	 * @param child
	 * @return
	 */
	public boolean acceptChild( M child );
}
