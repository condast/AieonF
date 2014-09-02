package org.aieonf.template.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.template.ITemplateLeaf;

public interface IContext<T extends IContextAieon>
{
	/**
	 * Get the domain
	 * @return 
	 */
	public IDomainAieon getDomain();

	/**
	 * Get the template of the context
	 * @return
	 */
	public ITemplateLeaf<T> getTemplate();
	
	/**
	 * Clear the container of the application.
	 * This usually needs to be done when other container
	 * objects are inserted.
	 * @throws Exception
	*/
	public void clear() throws Exception;
}