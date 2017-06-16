package org.aieonf.model.provider;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;

public interface IModelDatabase<U extends IDescriptor> extends IWriteModelProvider<IModelLeaf<U>>{
	
	/**
	 * Add a model
	 * @param descriptor
	 * @return
	 */
	public void add( U leaf );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public void remove( U leaf );


	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public void update( IModelLeaf<? extends IDescriptor> leaf );
}