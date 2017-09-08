package org.aieonf.template.parser;

import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.ModelException;

/**
 * Is called when a model parser parses a node
 * @author keesp
 *
 */
public interface ITemplateModelParser
{
	/**
	 * Parse the given node with the corresponding template node
	 * @param node
	 * @throws ModelException
	 */
	public void check( IModelLeaf<?> templateNode, IModelLeaf<?> node ) throws ModelException;
}
