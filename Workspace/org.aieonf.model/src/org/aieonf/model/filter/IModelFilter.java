package org.aieonf.model.filter;

import org.aieonf.commons.filter.IFilter;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IModelFilter<T extends IDescriptor, M extends IDescribable<T>> extends IFilter<M> {

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
