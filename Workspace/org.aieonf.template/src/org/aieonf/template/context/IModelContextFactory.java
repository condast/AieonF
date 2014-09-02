package org.aieonf.template.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.template.ITemplateLeaf;

public interface IModelContextFactory<T extends IContextAieon, U extends IContext<T>> {

	public abstract void addListener(IModelBuilderListener listener);

	public abstract void removeListener(IModelBuilderListener listener);

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

	/**
	 * Create a model the model
	 * @return
	 */
	public abstract IModelLeaf<IDescriptor> createModel();


	/**
	 * Get the domain for this context
	 * @return
	 */
	public abstract IDomainAieon getDomain();

	/**
	 * Create the model
	 * @return
	 */
	public abstract U createContext();

}