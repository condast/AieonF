package org.aieonf.model.provider;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;

/**
 * A delegate is exactly the same as a provider, but the methods have no return values, to 
 * indicate the values are returned asynchronously, ad will have to be retrieved by adding
 * listeners  
 * @author Kees
 *
 * @param <U>
 */
public interface IModelDelegate<U extends Object> extends IProvider<U>{
	
	/**
	 * Get the model provider
	 * @param leaf
	 * @return
	 */
	public IModelProvider<U> getFunction( String function );
	
	/**
	 * Delegates the 'contains' function 
	 * @param descriptor
	 * @return
	 */
	public void contains( IModelLeaf<? extends IDescriptor> leaf );

	/**
	 * Delegates the get function
	 * @param descriptor
	 * @throws ParseException
	 */
	public void get( IDescriptor descriptor ) throws ParseException;

	/**
	 * Delegates the serach function
	 * @param filter
	 * @throws ParseException
	 */
	public void search( IModelFilter<IDescriptor> filter ) throws ParseException;
}