package org.aieonf.template.context;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.concept.function.IDescribablePredicate;
import org.aieonf.template.def.ITemplateLeaf;

public interface ITemplateContextFactory<M extends IDescribable> {

	/**
	 * Get the domain for this context
	 * @return
	 */
	public abstract IDomainAieon getDomain();

	/**
	 * Create the template
	 * @return
	 */
	public abstract ITemplateLeaf<IContextAieon> createTemplate();

	/**
	 * Get the template
	 * @return
	 */
	public abstract ITemplateLeaf<IContextAieon> getTemplate();

	/**
	 * Create the template
	 * @return
	 */
	public abstract M createModel();

	/**
	 * Create the predicates for this model
	 * @return
	 */
	public IDescribablePredicate<IDescriptor> createPredicates();

}