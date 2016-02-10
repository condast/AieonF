package org.aieonf.util.xml;

public class URICompletion
{
	private String uri;
	
	public URICompletion( String uri )
	{
		this.uri = uri;
	}

	
	/**
	 * @return the uri
	 */
	protected final String getUri()
	{
		return uri;
	}

	/**
	 * Prevent common SAX parser errors from happening
	 * @param uri
	 * @return
	 */
	public static String complete( String uri ){
		return uri.replaceAll("&", "&amp;" );
	}
}
