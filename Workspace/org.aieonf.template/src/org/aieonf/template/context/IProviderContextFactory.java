package org.aieonf.template.context;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

public interface IProviderContextFactory<T extends IDescriptor, U extends IDescribable<IDescriptor>> extends IModelContextFactory<T> {

	public void addProvider(IFunctionProvider<T, IModelProvider<T, U>> function);

	public void removeProvider(IFunctionProvider<T, IModelProvider<T, U>> function);

	/**
	 * Returns true if the factory returns the given function
	 * @param function
	 * @return
	 */
	public boolean hasFunction(String function);
	
	/**
	 * Get the desired function for the given data object
	 * @param data
	 * @return
	 */
	public IModelProvider<T, U> getFunction( String function );

}