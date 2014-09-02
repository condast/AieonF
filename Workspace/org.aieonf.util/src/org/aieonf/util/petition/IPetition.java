package org.aieonf.util.petition;

public interface IPetition<T,U extends Object>
{
	/**
	 * Get the petition, which is a request that can be identified by a provider
	 * @return
	 */
	public T getPetition();
	
	/**
	 * Optional data for further processing
	 * @return
	 */
	public U getData();
}
