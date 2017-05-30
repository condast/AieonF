package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;

public interface IModelProvider<U extends Object> extends IProvider<U>{

	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	public boolean contains( IModelLeaf<? extends IDescriptor> leaf );

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<U> get( IDescriptor descriptor ) throws ParseException;

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<U> search( IModelFilter<IDescriptor> filter ) throws ParseException;
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

	/**
	 * Print the given database
	 */
	public String printDatabase();
}