package org.aieonf.model.builder;

public interface IFunctionProvider<D extends Object, U extends Object> {

	/**
	 * A function can be restricted to a certain domain. Returns true if the given domain
	 * is provided
	 * @param domain
	 * @return
	 */
	public boolean supportsDomain( D domain );
	
	/**
	 * Returns true if a function can be provided for the given model
	 * @param data
	 * @return
	 */
	public boolean canProvide( D key );

	/**
	 * Get the function belonging to the given model leaf
	 * @param data
	 * @return
	 */
	public U getFunction( D key );
}
