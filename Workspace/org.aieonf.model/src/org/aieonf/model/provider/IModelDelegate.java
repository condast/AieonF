package org.aieonf.model.provider;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;

public interface IModelDelegate<U extends Object> {
	
	public void addListener( IModelBuilderListener<U> listener );

	public void removeListener( IModelBuilderListener<U> listener );

	public void open();
	
	public boolean isOpen();
	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	public void close();

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
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();
}