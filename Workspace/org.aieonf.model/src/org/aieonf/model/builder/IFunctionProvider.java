package org.aieonf.model.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public interface IFunctionProvider<T extends IDescriptor, U extends Object> {

	/**
	 * Returns true if a function can be provided for the given model
	 * @param leaf
	 * @return
	 */
	public boolean canProvide( IModelLeaf<T> leaf );

	/**
	 * Get the function belonging to the given model leaf
	 * @param leaf
	 * @return
	 */
	public U getFunction( IModelLeaf<T> leaf );
}
