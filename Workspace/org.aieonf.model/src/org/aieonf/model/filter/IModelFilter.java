package org.aieonf.model.filter;

import org.aieonf.commons.filter.IAttributeFilter;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.filter.FilterFactory.Filters;

public interface IModelFilter<M extends IDescribable> extends IAttributeFilter<M> {
	
	public Filters getType();
	
	/**
	 * If true, the given child is accepted
	 * @param child
	 * @return
	 */
	public boolean acceptChild( M child );

	/**
	 * Get the minimum depth of the search. Is zero by default.
	 * @return
	 */
	public int getMinDepth();

	/**
	 * Get the maximum depth of the search. If negative, the search covers the tree
	 * @return
	 */
	public int getMaxDepth();
}
