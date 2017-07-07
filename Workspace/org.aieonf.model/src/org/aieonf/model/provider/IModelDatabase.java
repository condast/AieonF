package org.aieonf.model.provider;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;

public interface IModelDatabase<K extends Object, V extends IDescribable<? extends IDescriptor>> extends IModelProvider<V>{
	
	/**
	 * Add a model
	 * @param descriptor
	 * @return
	 */
	public void add( V leaf );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public void remove( V leaf );


	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public void update( V leaf );
}