package org.aieonf.template.context;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelDelegate;

public interface IProviderContextFactory<T extends IDescriptor> extends IModelContextFactory<IContextAieon> {

	public void addProvider(IFunctionProvider<IDescriptor, IModelDelegate<IContextAieon, T>> function);

	public void removeProvider(IFunctionProvider<IDescriptor, IModelDelegate<IContextAieon, T>> function);

	/**
	 * Returns true if the factory returns the given function
	 * @param function
	 * @return
	 */
	public boolean hasFunction(String function);

}