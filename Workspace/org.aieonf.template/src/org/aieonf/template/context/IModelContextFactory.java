package org.aieonf.template.context;

import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.ITemplateLeaf;

public interface IModelContextFactory<T extends IContextAieon> {

	public abstract void addListener(IModelBuilderListener listener);

	public abstract void removeListener(IModelBuilderListener listener);

	/**
	 * Get the domain for this context
	 * @return
	 */
	public abstract IDomainAieon getDomain();

	/**
	 * Create the template
	 * @return
	 */
	public abstract ITemplateLeaf<T> createTemplate();

	/**
	 * Get the template
	 * @return
	 */
	public abstract ITemplateLeaf<T> getTemplate();
}