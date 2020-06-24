package org.aieonf.template.context;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

/**
 * A factory provides models for a given domain D. It needs a:
 * - contextaieon C to initialise
 * - a describable U to retrieve the necessary model
 * @author Kees
 *
 * @param <M>
 * @param <C>
 */
public interface IProviderContextFactory<K extends Object, D extends IDescriptor, M extends IDescribable> extends IModelContextFactory<M> {

	public void addProvider(IFunctionProvider<K, IModelProvider<D, M>> function);

	public void removeProvider(IFunctionProvider<K, IModelProvider<D, M>> function);

	/**
	 * Returns true if the factory returns the given function
	 * @param function
	 * @return
	 */
	public boolean hasFunction(K function);
	
	/**
	 * Get the desired function for the given data object
	 * @param data
	 * @return
	 */
	public IModelProvider<D, M> getFunction( K function );

}