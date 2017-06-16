package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;

/**
 * A model provider provides a function to get models.
 * @author Kees
 *
 * @param <U>
 */
public interface IModelProvider<T extends IDescriptor, U extends IDescriptor> extends IProvider<IModelLeaf<U>>{

	/**
	 * Open the delegate
	 * @param domain
	 */
	public void open( T domain );
	
	public boolean isOpen( T domain );
		
	/**
	 * clse it
	 * @param domain
	 */
	public void close( T domain );
	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	public boolean contains( U leaf );

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<IModelLeaf<U>> get( IDescriptor descriptor ) throws ParseException;

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<IModelLeaf<U>> search( IModelFilter<IDescriptor> filter ) throws ParseException;
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

	/**
	 * Print the given database
	 */
	public String printDatabase();
}