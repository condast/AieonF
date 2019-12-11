package org.aieonf.model.parser;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.ModelException;

public interface IModelParser<D extends IDescriptor, M extends IDescribable> {

	public enum Status{
		PREPARE,
		BUSY,
		COMPLETED;
	}

	public void addListener( IParserListener<D,M> listener );

	public void removeListener( IParserListener<D,M> listener );

	/**
	 * Parse the given model	
	 * @param model
	 * @return true if all went well.
	 */
	public boolean parseModel( M model ) throws ModelException;
}
