package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IModelDatabase<D extends IDescriptor, T extends IDescribable<? extends IDescriptor>> extends IModelProvider<D, T>{
	
	/**
	 * Add a model
	 * @param descriptor
	 * @return
	 */
	public void add( T leaf );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public void remove( T leaf );


	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public void update( T leaf );
}