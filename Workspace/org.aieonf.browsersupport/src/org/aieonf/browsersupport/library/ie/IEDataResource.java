package org.aieonf.browsersupport.library.ie;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.datauri.IDataResource;

public class IEDataResource extends Concept implements IDataResource
{
	public IEDataResource(IDescriptor descriptor)
	{
		super(descriptor);
	}

	public static final String S_ICON = "ICON";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1374988386010517700L;

	@Override
	public void fill(String type, String resource)
	{
		try {
			String[] split = resource.split("\n");
			setValue( IDescriptor.Attributes.ID, split[0] );
			setValue( IDescriptor.Attributes.NAME, split[2] );
			String[] keyValue = split[3].split("[=]");
			setSource( keyValue[1]);
			keyValue = split[5].split("[=]");
			
			String url = keyValue[1];
			if( !url.toLowerCase().startsWith("http"))
				url = "file://" + resource;
			set( IDataResource.Attribute.Resource, resource );
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType()
	{
		return S_ICON;
	}

	@Override
	public String getResource()
	{
		return get( IDataResource.Attribute.Resource );
	}

	
}
