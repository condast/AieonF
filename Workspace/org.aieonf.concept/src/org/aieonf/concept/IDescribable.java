package org.aieonf.concept;

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
}
