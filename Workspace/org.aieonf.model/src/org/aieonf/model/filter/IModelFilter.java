package org.aieonf.model.filter;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.util.filter.IFilter;

public interface IModelFilter<T extends IDescriptor> extends IFilter<IModelLeaf<T>> {

	/**
	 * If true, the given child is accepted
	 * @param child
	 * @return
	 */
	public boolean acceptChild( IModelLeaf<T> child );
}
