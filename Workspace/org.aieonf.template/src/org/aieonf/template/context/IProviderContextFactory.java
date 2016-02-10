package org.aieonf.template.context;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.template.ITemplateLeaf;
import org.aieonf.util.transaction.ITransaction;

public interface IProviderContextFactory<T extends IDescriptor, U extends IDescribable<?>> {

	public ITemplateLeaf<T> createTemplate();
	
	/**
	 * A model provider contains all the aieons needed for a get action 
	 * @return the cdb
	 */
	public abstract IModelProvider<T, U> getModelProvider();

	/**
	 * Get a specific model provider
	 * @return the cdb
	 */
	public abstract IModelProvider<T, U> getDatabase();

	/**
	 * Search the default model provider according to the given filter
	 * @param factory
	 * @param filter
	 * @return
	 */
	public ITransaction<U, IModelProvider<IContextAieon, U>> search( IModelFilter<IDescriptor> filter);
}