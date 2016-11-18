package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.filter.IModelFilter;

public interface ISearchProvider<U extends Object> {

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<U> search( IModelFilter<IDescriptor> filter ) throws ParseException;
}
