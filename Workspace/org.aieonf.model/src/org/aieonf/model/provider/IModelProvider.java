package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.filter.IModelFilter;

/**
 * A model provider provides a function to get models.
 * @author Kees
 *
 * @param <T>
 */
public interface IModelProvider<D extends Object, T extends IDescribable<? extends IDescriptor>> extends IProvider<T>{

	/**
	 * Open the delegate
	 * @param domain
	 */
	public void open( D domain );
	
	public boolean isOpen( D domain );
		
	/**
	 * clse it
	 * @param domain
	 */
	public void close( D domain );
	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	public boolean contains( T leaf );

	/**
	 * Get the models conforming to the given descriptor. Tse the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<T> get( IDescriptor descriptor ) throws ParseException;

	/**
	 * Get the models conforming to the given descriptor. Tse the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<T> search( IModelFilter<IDescriptor> filter ) throws ParseException;
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

	/**
	 * Print the given database
	 */
	public String printDatabase();
}