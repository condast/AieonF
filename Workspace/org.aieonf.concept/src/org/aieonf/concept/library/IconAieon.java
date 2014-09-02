package org.aieonf.concept.library;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class IconAieon extends ConceptWrapper implements IIconAieon
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4116866697044006461L;

	public IconAieon(IDescriptor descriptor)
	{
		super(descriptor);
	}

	public IconAieon(String id, IDescriptor descriptor) throws ConceptException
	{
		super(id, descriptor);
	}

	@Override
	public String getIconFile()
	{
		return super.get( IIconAieon.Attributes.IconFile );
	}

	
}
