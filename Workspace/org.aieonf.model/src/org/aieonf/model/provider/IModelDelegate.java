package org.aieonf.model.provider;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;

/**
 * A delegate is exactly the same as a provider, but the methods have no return values, to 
 * indicate the values are returned asynchronously, ad will have to be retrieved by adding
 * listeners  
 * @author Kees
 *
 * @param <U>
 */
public interface IModelDelegate<D extends IDescribable<? extends IDescriptor>, U extends IDescriptor>{

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
	 * Delegates the 'contains' function 
	 * @param descriptor
	 * @return
	 */
	public void contains( U descriptor );

	/**
	 * Delegates the get function
	 * @param descriptor
	 * @throws ParseException
	 */
	public void get( U descriptor ) throws ParseException;

	/**
	 * Delegates the serach function
	 * @param filter
	 * @throws ParseException
	 */
	public void search( IModelFilter<IDescriptor> filter ) throws ParseException;

	/**
	 * Add a change listener
	 * @param listener
	 */
	void addListener(IModelBuilderListener<U> listener);

	/**
	 * Remove a listener
	 * @param listener
	 */
	void removeListener(IModelBuilderListener<U> listener);
}