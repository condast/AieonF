package org.aieonf.model.provider;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public interface IModelDatabase<D extends IDescriptor, T extends IDescriptor> extends IModelProvider<D, T>{
	
	/**
	 * Add a model
	 * @param descriptor
	 * @return
	 */
	public void add( D leaf );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public void remove( D leaf );


	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public void update( IModelLeaf<? extends IDescriptor> leaf );
}