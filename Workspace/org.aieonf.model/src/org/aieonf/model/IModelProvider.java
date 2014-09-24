package org.aieonf.model;

import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.util.parser.ParseException;

public interface IModelProvider<T extends IDescriptor, U extends Object> {

	public static final String S_MODEL_PROVIDER_ID = "org.condast.concept.model";

	public void addListener( IModelBuilderListener listener );

	public void removeListener( IModelBuilderListener listener );

	public void open();
	
	public boolean isOpen();
	
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
	 * Deactivate the function
	 */
	public void deactivate();
}