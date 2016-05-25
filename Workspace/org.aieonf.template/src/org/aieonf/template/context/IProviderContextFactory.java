package org.aieonf.template.context;

import org.aieonf.commons.transaction.ITransaction;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelDelegate;
import org.aieonf.template.ITemplateLeaf;

public interface IProviderContextFactory<T extends IDescriptor, U extends IDescribable<?>> {

	public ITemplateLeaf<T> createTemplate();
	
	/**
	 * A model provider contains all the aieons needed for a get action 
	 * @return the cdb
	 */
	public abstract IModelDelegate<U> getModelProvider();

	/**
	 * Get a specific model provider
	 * @return the cdb
	 */
	public abstract IModelDelegate<U> getDatabase();

	/**
	 * Search the default model provider according to the given filter
	 * @param factory
	 * @param filter
	 * @return
	 */
	public ITransaction<U, IModelDelegate<U>> search( IModelFilter<IDescriptor> filter);
}