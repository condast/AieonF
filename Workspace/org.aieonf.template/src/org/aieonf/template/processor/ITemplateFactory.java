package org.aieonf.template.processor;

import org.aieonf.model.ModelException;
import org.aieonf.template.ITemplate;

public interface ITemplateFactory<T extends ITemplate>
{
	/**
	 * Handle a model node
	 * @param node
	 * @throws ModelException
	 */
	public void processModelNode( T node ) throws ModelException ;
}
