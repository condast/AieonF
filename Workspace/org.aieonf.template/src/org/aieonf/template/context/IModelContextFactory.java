package org.aieonf.template.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.ITemplateLeaf;

public interface IModelContextFactory<T extends IDescriptor> {

	public abstract void addListener(IModelBuilderListener<IModelLeaf<?>> listener);

	public abstract void removeListener(IModelBuilderListener<IModelLeaf<?>>  listener);

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