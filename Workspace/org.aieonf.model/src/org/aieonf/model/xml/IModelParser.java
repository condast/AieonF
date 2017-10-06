package org.aieonf.model.xml;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IModelParser<T extends IDescriptor, M extends IDescribable<T>> {
	/**
	 * Add a model builder listener
	 * @param event
	 */
	public void addModelExtender( IXMLModelInterpreter<T, M> extender );

	/**
	 * Remove a model builder listener
	 * @param event
	 */
	public void removeModelExtender( IXMLModelInterpreter<T, M> extender );
}
