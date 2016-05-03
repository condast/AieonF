package org.aieonf.model;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;

public interface IModelProvider<T extends IDescriptor, U extends Object> {

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

	/**
	 * Get a unique identifier for this model provider
	 * @return
	 */
	public String getIdentifier();
	
	public void addListener( IModelBuilderListener listener );

	public void removeListener( IModelBuilderListener listener );

	public void open();
	
	public boolean isOpen();
	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	public void close();

	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	public boolean contains( IModelLeaf<? extends IDescriptor> leaf );

	public Collection<U> get( IDescriptor descriptor ) throws ParseException;

	public Collection<U> search( IModelFilter<IDescriptor> filter ) throws ParseException;
	
	/**
	 * Create a transaction for the given object in this provider
	 * @param data
	 * @return
	 */
	public ITransaction<U, IModelProvider<T, U>> createTransaction();
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

	/**
	 * Print the given database
	 */
	public String printDatabase();
}