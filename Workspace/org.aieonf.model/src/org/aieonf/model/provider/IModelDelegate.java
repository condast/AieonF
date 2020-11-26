package org.aieonf.model.provider;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.filter.IModelFilter;

/**
 * A delegate is exactly the same as a provider, but the methods have no return values, to 
 * indicate the values are returned asynchronously, ad will have to be retrieved by adding
 * listeners  
 * @author Kees
 *
 * @param <D>
 */
public interface IModelDelegate<D extends IDescriptor, M extends IModelLeaf<D>>{

	/**
	 * Open the delegate
	 * @param domain
	 */
	public void open(D domain );
	
	public boolean isOpen();
		
	/**
	 * clse it
	 * @param domain
	 */
	public void close();

	/**
	 * Delegates the 'contains' function 
	 * @param descriptor
	 * @return
	 */
	public void contains( M descriptor );

	/**
	 * Delegates the get function
	 * @param descriptor
	 * @throws ParseException
	 */
	public void get( M descriptor ) throws ParseException;

	/**
	 * Delegates the serach function
	 * @param filter
	 * @throws ParseException
	 */
	public void search( IModelFilter<D, M> filter ) throws ParseException;

	/**
	 * Add a change listener
	 * @param listener
	 */
	void addListener(IModelListener<M> listener);

	/**
	 * Remove a listener
	 * @param listener
	 */
	void removeListener(IModelListener<M> listener);
}