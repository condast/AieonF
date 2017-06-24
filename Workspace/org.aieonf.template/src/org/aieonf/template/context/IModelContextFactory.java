package org.aieonf.template.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.ITemplateLeaf;

public interface IModelContextFactory<C extends IContextAieon, D extends IDomainAieon> {

	public abstract void addListener(IModelBuilderListener<C> listener);

	public abstract void removeListener(IModelBuilderListener<C>  listener);

	/**
	 * Get the domain for this context
	 * @return
	 */
	public abstract D getDomain();

	/**
	 * Create the template
	 * @return
	 */
	public abstract ITemplateLeaf<C> createTemplate();

	/**
	 * Get the template
	 * @return
	 */
	public abstract ITemplateLeaf<C> getTemplate();
}