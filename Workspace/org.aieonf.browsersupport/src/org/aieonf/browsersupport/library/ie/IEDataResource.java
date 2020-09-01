package org.aieonf.browsersupport.library.ie;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.datauri.IDataResource;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class IEDataResource extends ConceptWrapper implements IDataResource
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
			set( IDescriptor.Attributes.ID.toString(), split[0] );
			set( IDescriptor.Attributes.NAME.toString(), split[2] );
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
