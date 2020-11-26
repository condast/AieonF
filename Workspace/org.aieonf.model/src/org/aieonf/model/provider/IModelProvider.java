package org.aieonf.model.provider;

import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.filter.IModelFilter;

/**
 * A model provider provides a function to get models.
 * @author Kees
 *
 * @param <M>
 */
public interface IModelProvider<K extends Object, D extends IDescriptor, M extends IModelLeaf<D>> extends IProvider<M>{

	public static final String S_DESCRIPTOR_PROVIDER_ID = "org.aieonf.descriptor.provider";
	public static final String S_CONCEPT_PROVIDER_ID    = "org.aieonf.concept.provider";
	public static final String S_MODEL_PROVIDER_ID      = "org.aieonf.model.provider";
	public static final String S_DB_PROVIDER_ID         = "org.aieonf.database.provider";
	public static final String S_GRAPH_PROVIDER_ID      = "org.aieonf.graph.provider";
	public static final String S_MODEL_TREE_PROVIDER_ID = "org.aieonf.tree.provider";

	/**
	 * A provider can provide various functions:
	 * 1; a provider merely provides models and the means to read them
	 * 2; a database also allows CRUD operations on a model
	 * 3: A graph provides means to represent a models as a graph
	 * 4: a tree provides means to represent a model as a tree
	 * @author Kees
	 *
	 */
	public static enum DefaultModels{
		PROVIDER,
		DATABASE,
		DESCRIPTOR,
		CONCEPT,
		MODEL,
		GRAPH,
		TREE;

		@Override
		public String toString() {
			String function = S_MODEL_PROVIDER_ID;
			switch( this ){
			case DATABASE:
				function = S_DB_PROVIDER_ID;
				break;
			case DESCRIPTOR:
				function = S_DESCRIPTOR_PROVIDER_ID;
				break;
			case CONCEPT:
				function = S_CONCEPT_PROVIDER_ID;
				break;
			case MODEL:
				function = S_MODEL_PROVIDER_ID;
				break;
			case GRAPH:
				function = S_GRAPH_PROVIDER_ID;
				break;
			case TREE:
				function = S_MODEL_TREE_PROVIDER_ID;
				break;
			default:
				break;
			}
			return function;
		}
		
		/**
		 * Returns true if the given string is valid
		 * @param name
		 * @return
		 */
		public static boolean isValid( String name ){
			for( DefaultModels model: values()){
				if( model.toString().equals( name ))
					return true;
			}
			return false;
		}

		/**
		 * Returns true if the given string is valid
		 * @param name
		 * @return
		 */
		public static DefaultModels getModel( String name ){
			for( DefaultModels model: values()){
				if( model.toString().equals( name ))
					return model;
			}
			return null;
		}

	}

	/**
	 * Open the delegate
	 * @param domain
	 */
	public void open( K key );
	
	public boolean isOpen();
		
	/**
	 * clse it
	 * @param domain
	 */
	public void close();
	
	/**
	 * Sync the actual model with the database
	 */
	public void sync();
	
	/**
	 * Returns true if the given leaf is contained in the provider 
	 * @param descriptor
	 * @return
	 */
	public boolean contains( M leaf );

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<M> get( IDescriptor descriptor ) throws ParseException;

	/**
	 * Get the models conforming to the given descriptor. Use the model builder
	 * listener to obtain them
	 * @param descriptor
	 * @throws ParseException
	 */
	public Collection<M> search( IModelFilter<D, M> filter ) throws ParseException;
	
	/**
	 * Deactivate the function
	 */
	@Override
	public void deactivate();
}