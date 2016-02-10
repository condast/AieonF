package org.aieonf.model.builder;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public interface IModelParser<T extends IDescriptor> {

	/**
	 * Get the model that builds the application
	 * @return
	 */
	public IModelLeaf<T> getModel();
}
