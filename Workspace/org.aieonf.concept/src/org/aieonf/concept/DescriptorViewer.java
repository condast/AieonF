package org.aieonf.concept;

public class DescriptorViewer implements IDescriptorViewer
{
	private IDescriptor descriptor;
	
	public DescriptorViewer( IDescriptor descriptor )
	{
		this.descriptor = descriptor;
	}

	/**
	 * @return the descriptor
	 */
	protected final IDescriptor getDescriptor()
	{
		return descriptor;
	}


	@Override
	public String getDescription()
	{
		if( descriptor.getDescription() == null )
			return descriptor.getName();
		return descriptor.getDescription();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return descriptor.toString();
	}
}
