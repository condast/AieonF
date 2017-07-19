package org.aieonf.model.parser;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.ModelException;

public interface IModelParserEngine<T extends IDescriptor, U extends Object>
{
	/**
	 * The type of parsing that is aplied 
	 * @author keesp
	 *
	*/
	public enum Search{
		BFS,
		DFS
	}
	
	/**
	 * @return the search
	 */
	public abstract Search getSearch();

	/**
	 * Add a listener
	 * @param listener
	 */
	public abstract void addListener( U listener);

	/**
	 * Remove a listener
	 * @param listener
	 */
	public abstract void removeListener( U listener);

	/**
	 * Parse the model according to the search pattern
	 * @throws ModelException
	 */
	public abstract void parse() throws ModelException;

}