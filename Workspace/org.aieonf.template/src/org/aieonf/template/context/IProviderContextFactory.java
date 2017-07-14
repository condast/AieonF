package org.aieonf.template.context;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.context.IContextAieon;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IFunctionProvider;
import org.aieonf.model.provider.IModelProvider;

/**
 * A factory provides models for a given domain D. It needs a:
 * - contextaieon C to initialise
 * - a describable U to retrieve the necessary model
 * @author Kees
 *
 * @param <D>
 * @param <C>
 */
public interface IProviderContextFactory<C extends IContextAieon, D extends IDomainAieon, T extends Object, U extends IDescribable<? extends IDescriptor>> extends IModelContextFactory<C,D> {

	public void addProvider(IFunctionProvider<T, IModelProvider<D, U>> function);

	public void removeProvider(IFunctionProvider<T, IModelProvider<D, U>> function);

	/**
	 * Returns true if the factory returns the given function
	 * @param function
	 * @return
	 */
	public boolean hasFunction(T function);
	
	/**
	 * Get the desired function for the given data object
	 * @param data
	 * @return
	 */
	public IModelProvider<D, U> getFunction( T function );

}