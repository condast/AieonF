package org.aieonf.model.builder;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.domain.IDomainAieon;

public interface IFunctionProvider<T extends IDescribable<?>, U extends Object> {

	/**
	 * A function can be restricted to a certain domain. Returns true if the given domain
	 * is provided
	 * @param domain
	 * @return
	 */
	public boolean supportsDomain( IDomainAieon domain );
	
	/**
	 * Returns true if a function can be provided for the given model
	 * @param data
	 * @return
	 */
	public boolean canProvide( T data );

	/**
	 * Get the function belonging to the given model leaf
	 * @param data
	 * @return
	 */
	public U getFunction( T data );
}
