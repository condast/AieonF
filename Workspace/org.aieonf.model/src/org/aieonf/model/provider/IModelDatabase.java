package org.aieonf.model.provider;

import java.util.Collection;
import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;

public interface IModelDatabase<K extends Object, D extends IDescriptor, M extends IModelLeaf<D>> extends IModelProvider<K, D, M>{

	public enum Attributes{
		ID,
		USER_ID,
		TOKEN,
		DIRECTION,
		DOMAIN,
		DOMAIN_ONLY,
		MODEL_ID,
		KEY,
		VALUE,
		LABEL,
		CATEGORY,
		WILDCARD;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( super.name());
		}
	}

	public enum Roles{
		ADMIN,
		READ;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public enum Direction{
		IN,
		OUT,
		BOTH;

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
	 * Add a child to the given parent, using the given label
	 * @param descriptor
	 * @return
	 */
	public boolean addNode( long modelId, String label, M child );

	/**
	 * Get the model of all the nodes that are adjacent the given one
	 * @param id
	 * @param direction
	 * @return
	 */
	public Collection<M> adjacent(long id, Direction direction);

	/**
	 * Find the model with the given id
	 * @param id
	 * @return
	 */
	public M find(long id);

	/**
	 * Find the model that belongs to the given descriptor Id, within the 
	 * domain of the database
	 * @param id
	 * @return
	 */
	public M findOnDescriptor(long id);

	/**
	 * Get all the models that are use the descriptor with the given id
	 * @param descriptorId
	 * @return
	 */
	public Collection<M> findModels(long descriptorId);

	/**
	 * search for models that have the given key-value pair
	 * @param key
	 * @param value
	 * @return
	 */
	public Collection<M> searchModels(String key, String value);

	/**
	 * Get all the models, or only within a doamin if this is required
	 * @param domainOnly
	 * @return
	 */
	public Collection<M> getAll(boolean domainOnly);

	/**
	 * Remove a model 
	 * @param descriptor
	 * @return
	 */
	public boolean remove( M leaf );

	/**
	 * Remove the children from the given parent
	 * @param parent
	 * @param children
	 * @return the updated model
	 */
	public Collection<M> remove(long parent, long[] children);

	/**
	 * Remove the children from the given parent, with the given descriptor id
	 * @param parent
	 * @param children
	 * @return the updated model
	 */
	public Collection<M> removeOnDescriptorId(long parent, long descriptor);

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

	/**
	 * Get the options for the given user
	 * @param userId
	 * @return
	 */
	public M getOptions(long userId);
}