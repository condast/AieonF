package org.aieonf.model.filter;

import org.aieonf.commons.filter.IFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

public interface IModelFilter<T extends IDescriptor> extends IFilter<IModelLeaf<T>> {

	/**
	 * If true, the given child is accepted
	 * @param child
	 * @return
	 */
	public boolean acceptChild( IModelLeaf<T> child );

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
