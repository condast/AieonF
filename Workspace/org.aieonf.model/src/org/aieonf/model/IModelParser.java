package org.aieonf.model;

import org.aieonf.concept.IDescriptor;

public interface IModelParser<T extends IDescriptor> {

	/**
	 * Parse the given model	
	 * @param model
	 * @return true if all went well.
	 */
	public boolean parseModel( IModelLeaf<T> model ) throws ModelException;
}
