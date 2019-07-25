package org.aieonf.model.parser;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;

public interface IModelParser<T extends IDescriptor> {

	/**
	 * Parse the given model	
	 * @param model
	 * @return true if all went well.
	 */
	public boolean parseModel( IModelLeaf<T> model ) throws ModelException;
}
