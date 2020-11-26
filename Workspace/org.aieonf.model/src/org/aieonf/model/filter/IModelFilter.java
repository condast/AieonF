package org.aieonf.model.filter;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.FilterFactory.Filters;
import org.aieonf.concept.filter.IDescribableFilter;
import org.aieonf.model.core.IModelLeaf;

public interface IModelFilter<D extends IDescriptor, M extends IModelLeaf<D>> extends IDescribableFilter<M> {
	
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
