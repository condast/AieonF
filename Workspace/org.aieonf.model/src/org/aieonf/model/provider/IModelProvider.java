package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;

public interface IModelProvider<U extends Object> extends ISearchProvider<U>{

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";

	/**
	 * Get a unique identifier for this model provider
	 * @return
	 */
	public String getIdentifier();
	
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
	 * Deactivate the function
	 */
	public void deactivate();

	/**
	 * Print the given database
	 */
	public String printDatabase();
}