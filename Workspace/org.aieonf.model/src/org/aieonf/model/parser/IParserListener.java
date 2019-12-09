package org.aieonf.model.parser;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IParserListener<D extends IDescriptor, M extends IDescribable<D>> {
	
	/**
	 * Notify relevant events during the build process 
	 * @param event
	 */
	public void notifyChange( ParseEvent<D,M> event );
}
