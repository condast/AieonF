package org.aieonf.concept;

/**
 * The descriptor viewer gives a summary of the most vital data of a descriptor. This is
 * principally context specific
 * @author Kees
 *
 */
public interface IDescriptorViewer
{
	/**
	 * Get the description for this descriptor
	 * @return
	 */
	public String getDescription();
	
	@Override
	public String toString();
}
