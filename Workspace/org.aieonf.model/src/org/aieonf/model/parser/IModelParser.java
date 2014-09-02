package org.aieonf.model.parser;

import org.aieonf.model.IModelLeaf;
import org.aieonf.model.ModelException;

/**
 * Is called when a model parser parses a node
 * @author keesp
 *
 */
public interface IModelParser
{
	/**
	 * Parse the given node
	 * @param node
	 * @throws ModelException
	 */
	public void parse( IModelLeaf<?> node ) throws ModelException;
}
