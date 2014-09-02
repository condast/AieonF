package org.aieonf.concept.implicit;

import org.aieonf.concept.DescriptorViewer;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;

public class ImplicitDescriptorViewer<T extends IDescriptor> extends DescriptorViewer
{
	public ImplicitDescriptorViewer( IImplicitAieon<T> aieon )
	{
		super( aieon );
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDescription()
	{
		IImplicitAieon<T> aieon = ( IImplicitAieon<T> )super.getDescriptor();
		String description = aieon.getAttributeValue();
		if( Descriptor.isNull( description ))
			return super.getDescription();
		return description;
	}
}
