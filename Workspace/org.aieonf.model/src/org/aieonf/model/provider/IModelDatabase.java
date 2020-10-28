package org.aieonf.model.provider;

import java.util.Map;

import org.aieonf.concept.IDescribable;

public interface IModelDatabase<K extends Object, M extends IDescribable> extends IModelProvider<K, M>{
	
	public enum Roles{
		ADMIN,
		READ;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	/**
	 * Create a model and add it to the database 
	 * @param descriptor
	 * @return
	 */
	public boolean create( M leaf );

	/**
	 * Add a model
	 * @param descriptor
	 * @return the id of the newly made model
	 */
	public long add( M leaf );

	/**
	 * Add a child to the given parent
	 * @param descriptor
	 * @return
	 */
	public boolean add( M parent, M child );

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public boolean remove( M leaf );

	/**
	 * Remove the models with the given ids
	 * @param descriptor
	 * @return
	 */
	boolean remove(long[] ids);

	/**
	 * Remove the models with the given ids from the model with the key id 
	 * @param descriptor
	 * @return
	 */
	public boolean remove( Map.Entry<Long, Long[]> ids );

	/**
	 * update a model 
	 * @param descriptor
	 * @return
	 */
	public boolean update( M leaf );
}