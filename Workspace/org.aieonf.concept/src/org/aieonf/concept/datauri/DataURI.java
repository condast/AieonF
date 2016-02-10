package org.aieonf.concept.datauri;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class DataURI extends ConceptWrapper implements IDataURI
{
	public static final String S_ICON = "icon";
	
	public static final String S_DATA_URI = "Data";
	public static final String S_DATA = "data";
	public static final String S_CHARSET = "charset";
	public static final String S_BASE64 = "base64";
	
	public static final String S_ICON_MIMETYPE = "image/png";

	public static final String S_ERR_ILLEGAL_DATA_URI_1 = 
			"Illegal Data URI; Should have the form <type>=<value>";
	public static final String S_ERR_ILLEGAL_DATA_URI_2 = 
			"Illegal Data URI; Should minimally have the form <type>=data:<mimetype><data>";
	public static final String S_ERR_ILLEGAL_DATA_URI_3 = 
			"Illegal Data URI; Should have the form <type>=data:mimetype/encoding,data>";
	public static final String S_ERR_ILLEGAL_DATA_URI_4 = 
			"Illegal MIMETYPE; Should have the form <type>/<value>";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7841764790094621482L;
	
	private String datauri;

	public DataURI()
	{
		super( new Concept());
	}

	public DataURI( String name )
	{
		super( new Concept( S_DATA_URI ));
		super.setSource(name);
		super.setDescription(name);
	}

	public DataURI( String id, String name )
	{
		super( new Concept( id, S_DATA_URI ));
		super.setSource(name);
		super.setDescription(name);
	}

	public DataURI( IDescriptor descriptor ){
		super( descriptor );
	}
	
	public String getDataName(){
		return super.get( S_DATA );
	}
	
	public void setDataName( String value ){
		super.set( S_DATA, value);
	}
	
	/**
	 * Fill the Data URI with the given datauri string
	 * @param datauri
	 */
	@Override
	public void fill( String datauri ){
		String[] split = datauri.split("[=]");
		if( split.length < 2 )
			throw new IllegalArgumentException( S_ERR_ILLEGAL_DATA_URI_1 );
		this.fill(split[0], split[1]);
	}
	
	/**
	 * Fill the Data URI with the given datauri string
	 * @param datauri
	 */
	@Override
	public void fill( String key, String datauri ){
		super.set( IDataResource.Attribute.Type, key);
		String str = datauri.replace("\"", "").trim();
		String[] split = str.split("[:;,]");
		str = split[0].toLowerCase().trim();
		if(( split.length < 3 ) || ( !str.equals( S_DATA )))
			throw new  IllegalArgumentException( S_ERR_ILLEGAL_DATA_URI_2 );
		if( split.length > 5 )
			throw new  IllegalArgumentException( S_ERR_ILLEGAL_DATA_URI_3 );
		
		str = split[1];
		if( str.split("[/]").length != 2 )
			throw new  IllegalArgumentException( S_ERR_ILLEGAL_DATA_URI_4 );
		super.set(IDataURI.Attribute.MIME_TYPE, str);
		super.set(IDataResource.Attribute.Resource, split[split.length -1]);
		if( split.length == 3 )
			return;
		for( int i = 2; i < split.length-1 ; i++){
			str = split[i].toLowerCase().trim();
			if( str.startsWith( S_CHARSET)){
				String[] charsetsplit = str.split("[=]");
				if( charsetsplit.length != 2 )
					continue;
				super.set( IDataURI.Attribute.CHARSET, charsetsplit[1]);
			}
			if( str.equals( S_BASE64)){
				super.set( IDataURI.Attribute.ENCODING, str);
			}
		}
		this.datauri = datauri;
	}
	
	@Override
	public String getType()
	{
		return super.get( IDataResource.Attribute.Type );
	}

	@Override
	public String getMimeType()
	{
		return super.get( IDataURI.Attribute.MIME_TYPE );
	}

	@Override
	public String getMimeTypeExtension()
	{
		String str = super.get( IDataURI.Attribute.MIME_TYPE );
		String[] split = str.split("[/]");
		return split[1];
	}

	/**
	 * Get the charset (e.g. UTF-8) or null if none is provided
	 * @return
	*/
	@Override
	public String getCharset()	
	{
		return super.get( IDataURI.Attribute.CHARSET );
	}

	@Override
	public String getEncoding()
	{
		return super.get( IDataURI.Attribute.ENCODING );
	}
	
	@Override
	public void setURI( String uri ){
		super.setSource( uri );
	}

	/**
	 * Returns true if the encoding is base64
	 * @return
	 */
	@Override
	public boolean isBase64Encoded(){
		String str = this.getEncoding();
		return ( !Descriptor.isNull(str) && str.equals(S_BASE64 ));
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.Resource );
	}
	
	@Override
	public String toString(){
		if( this.datauri == null )
			return super.toString();
		return datauri;
	}
	
	/**
	 * returns a representation of the resource that can be dispalyed in a web page
	 * @param resource
	 * @return
	 */
	public static String getWebResource( IDataResource resource ){
		if(!( resource instanceof IDataURI ))
			return resource.getResource();
		IDataURI duri = ( IDataURI )resource;
		if( duri.getMimeType().equals( S_ICON_MIMETYPE ))
			return resource.toString();
		return resource.getResource();
	}

}
