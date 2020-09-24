package org.aieonf.model.builder;

import org.aieonf.concept.domain.IDomainAieon;

public interface IFunctionProvider<K extends Object, F extends Object> {

	public static final String S_FUNCTION_PROVIDER_ID = "org.aieonf.function.provider";

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
	public boolean canProvide( K key );

	/**
	 * Get the function belonging to the given model leaf
	 * @param data
	 * @return
	 */
	public F getFunction( K key );
}
