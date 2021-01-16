package org.aieonf.concept.datauri;

import org.aieonf.concept.core.Descriptor;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.commons.xml.URICompletion;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Concept;

public class DataURI extends Concept implements IDataURI
{
	public static final String S_ICON = "icon";
	
	public static final String S_DATA_URI = "Data";
	public static final String S_DATA = "data";
	public static final String S_CHARSET = "charset";
	public static final String S_BASE64 = "base64";
	
	public static final String S_ICON_MIMETYPE = "image/png";
	public static final String S_DEFAULT_MIMETYPE = "text/plain";

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

	public DataURI(){
		super( );
	}

	public DataURI( String name )
	{
		super( S_DATA_URI );
		super.setSource(name);
		super.set(IDataResource.Attribute.IS_DATA_URI.name(), Boolean.TRUE.toString());
		super.set(IDataURI.Attribute.MIME_TYPE.name(), S_DEFAULT_MIMETYPE );
		super.setIdentifier(name);
		super.setDescription(name);
	}

	public DataURI( long id, String name )
	{
		super( id, S_DATA_URI );
		super.set(IDataResource.Attribute.IS_DATA_URI.name(), Boolean.TRUE.toString());
		super.set(IDataURI.Attribute.MIME_TYPE.name(), S_DEFAULT_MIMETYPE );
		super.setSource(name);
		super.setIdentifier(name);
		super.setDescription(name);
	}

	public DataURI( IDescriptor descriptor ) {
		super( descriptor.getBase() );
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
		super.set( IDataResource.Attribute.TYPE.name(), key);
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
		super.set(IDataURI.Attribute.MIME_TYPE.name(), str);
		super.set(IDataResource.Attribute.RESOURCE.name(), split[split.length -1]);
		if( split.length == 3 )
			return;
		for( int i = 2; i < split.length-1 ; i++){
			str = split[i].toLowerCase().trim();
			if( str.startsWith( S_CHARSET)){
				String[] charsetsplit = str.split("[=]");
				if( charsetsplit.length != 2 )
					continue;
				super.set( IDataURI.Attribute.CHARSET.name(), charsetsplit[1]);
			}
			if( str.equals( S_BASE64)){
				super.set( IDataURI.Attribute.ENCODING.name(), str);
			}
		}
		this.datauri = datauri;
	}
	
	@Override
	public String getType(){
		return super.get( IDataResource.Attribute.TYPE.name() );
	}

	@Override
	public String getMimeType(){
		return super.get( IDataURI.Attribute.MIME_TYPE.name() );
	}

	@Override
	public String getMimeTypeExtension(){
		String str = super.get( IDataURI.Attribute.MIME_TYPE );
		String[] split = str.split("[/]");
		return split[1];
	}

	/**
	 * Get the charset (e.g. UTF-8) or null if none is provided
	 * @return
	*/
	@Override
	public String getCharset()	{
		return super.get( IDataURI.Attribute.CHARSET );
	}

	@Override
	public String getEncoding(){
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
		return ( !Descriptor.assertNull(str) && str.equals(S_BASE64 ));
	}

	@Override
	public String getResource()
	{
		return super.get( IDataResource.Attribute.RESOURCE.name() );
	}
	
	@Override
	public String toString(){
		if( this.datauri == null )
			return super.toString();
		return datauri;
	}
	
	public static boolean isDataURI( IDescriptor descriptor ) {
		String result = descriptor.get( IDataResource.Attribute.IS_DATA_URI.name() );
		return StringUtils.isEmpty(result)?false: Boolean.parseBoolean(result);
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
	
	/**
	 * Return the resource as an html string
	 * @param resource
	 * @return
	 */
	public static String toHtml( IDataURI descriptor ) {
		if( !isDataURI( descriptor ))
			return null;
		String webResource = DataURI.getWebResource( descriptor ).split("[?]")[0];
		webResource = URICompletion.complete(webResource );
		return "<a href='' target='_blank'><img src='" + webResource + "' width='16' height='16' /></a>";
	}

	
	/**
	 * Return the resource as an html string
	 * @param resource
	 * @return
	 */
	public static String toHtml( IDataResource resource ) {
		if( Descriptor.assertNull( resource.getResource() ))
			return null;
		
		String webResource = DataURI.getWebResource( resource ).split("[?]")[0];
		webResource = URICompletion.complete(webResource );
		return "<a href='' target='_blank'><img src='" + webResource + "' width='16' height='16' /></a>";
	}

	/**
	 * Return the resource as an html string
	 * @param resource
	 * @return
	 */
	public static String toHtml( IDescriptor descriptor ) {
		IDataURI uri = new DataURI( descriptor );
		return toHtml(uri);
	}
}
