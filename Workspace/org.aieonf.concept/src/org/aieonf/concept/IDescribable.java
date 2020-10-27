package org.aieonf.concept;

import java.util.Map;
import java.util.Set;

/**
 * Interface for updatable objects
 * @author Kees
 *
 */
public interface IDescribable
{
	/**
	 * Get the name of the descriptor
	 * @return
	 */
	public IDescriptor getDescriptor();

	/**
	 * If true, the values have changed
	 * @return
	 */
	public boolean hasChanged();
	
	/**
	 * Get the entry set
	 * @return
	 */
	public Set<Map.Entry<String, String>> entrySet();
}
