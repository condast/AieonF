package org.aieonf.concept;

/**
 * Interface for updatable objects
 * @author Kees
 *
 */
public interface IDescribable<T extends IDescriptor>
{
  /**
   * Get the name of the descriptor
   * @return
   */
	public T getDescriptor();
	
	/**
   * If true, the values have changed
   * @return
  */
  public boolean hasChanged();
}
