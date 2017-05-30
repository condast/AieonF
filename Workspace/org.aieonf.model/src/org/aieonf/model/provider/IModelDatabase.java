package org.aieonf.model.provider;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public interface IModelDatabase<U extends Object> extends IModelProvider<U>{
	
	/**
	 * Add a model
	 * @param descriptor
	 * @return
	 */
	public void add( IModelLeaf<? extends IDescriptor> leaf );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public void remove( IModelLeaf<? extends IDescriptor> leaf );


	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public void update( IModelLeaf<? extends IDescriptor> leaf );
}