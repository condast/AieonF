package org.aieonf.model.provider;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;

/**
 * A provider can return an object U, if the correct model is provided. This
 * allows for a transformation of a model to a concrete class
 * @author Kees
 *
 * @param <U>
 */
public interface IProvider<U extends Object> {

	public static final String S_MODEL_PROVIDER_ID = "org.aieonf.model.provider";
	public static final String S_DB_PROVIDER_ID =    "org.aieonf.database.provider";
	public static final String S_GRAPH_PROVIDER_ID = "org.aieonf.graph.provider";
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
		GRAPH,
		TREE;

		@Override
		public String toString() {
			String function = S_MODEL_PROVIDER_ID;
			switch( this ){
			case DATABASE:
				function = S_DB_PROVIDER_ID;
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
	 * Get a unique identifier for this model provider
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Returns true if the function with the given name is provided. 
	 * @param function
	 * @return
	 */
	public boolean hasFunction( String function ); 
	
	public void addListener( IModelBuilderListener<U> listener );

	public void removeListener( IModelBuilderListener<U> listener );

	public void open( IDomainAieon domain );
	
	public boolean isOpen( IDomainAieon domain );
		
	public void close( IDomainAieon domain );
	
	
	/**
	 * Deactivate the function
	 */
	public void deactivate();

}
