package org.aieonf.concept.library;

import java.net.URL;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.datauri.DataURI;
import org.aieonf.concept.datauri.IDataURI;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.aieonf.util.StringStyler;
import org.aieonf.util.xml.URICompletion;

/**
 * Create an url aieon
 * @author Kees
 *
 */
public class URLAieon extends ImplicitAieon implements IDataURI
{
	/**
	 * For serialisation
	 */
	private static final long serialVersionUID = 8350616501493379468L;

	public static final String S_FILE = "file";
	public static final String S_HTTP = "http";
	public static final String S_PLACE = "place";

	/**
	 * The supported additional attributes
	 * @author Kees
	 *
	 */
	public enum Attributes
	{
		URL,
		TYPE,
		SERVER;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * The supported types
	 * @author Kees
	 *
	 */
	public enum ResourceTypes{
		Html,
		Rss,
		Music,
		Video,
		Document
	}
	/**
	 * Create a default domain aieon
	 */
	public URLAieon()
	{
		super( new DataURI( IConcept.Attributes.SOURCE.toString() ), IConcept.Attributes.SOURCE.toString() );
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	 */
	public URLAieon( String uri ) 
	{
		super( new DataURI( uri ), IConcept.Attributes.SOURCE.toString() );
		super.setSource( uri );
	}

	public URLAieon( URL url ){
		this( url.toExternalForm() );
	}
	
	/**
	 * Create a domain aieon with the given id and name
	 * @param id
	 * @param domain
	 */
	public URLAieon( String id, String uri )
	{
		super( new DataURI( id, IConcept.Attributes.SOURCE.toString() ), IConcept.Attributes.SOURCE.toString() );
		this.setSource( uri );		
	}

	/**
	 * Create a domain aieon with the given id and name
	 * @param id
	 * @param domain
	 */
	public URLAieon( IDescriptor descriptor )
	{
		super( new DataURI( descriptor ), IConcept.Attributes.SOURCE.toString() );
	}

	/**
	 * Get the type of the url
	 * @return
	 */
	public ResourceTypes getResouceType()
	{
		String value = this.get( Attributes.TYPE );
		if( value == null )
			return ResourceTypes.Html;
		return ResourceTypes.valueOf( value );
	}

	/**
	 * Set the type for this uri
	 * @param type String
	 */
	public void setType( String type )
	{
		super.set( Attributes.TYPE, type );
	}

	/**
	 * If true, the url points to a location on the server
	 * @return
	 */
	public Boolean isOnServer()
	{
		return super.getBoolean( super.getKeyName( Attributes.SERVER ));
	}

	/**
	 * Set whether the url is on the server the type for this uri
	 * @param type URLAieon.Types
	 */
	public void setOnServer( boolean choice )
	{
		super.set( Attributes.SERVER, String.valueOf( choice ));
	}

	/**
	 * Complete the uri, based on the info that is available
	 * @return
	 */
	public String completeURI(){
		String uri = super.getSource();
		if(( uri == null ) || ( uri.toLowerCase().startsWith( S_PLACE + ":")))
			return "null";
		if(( uri.toLowerCase().startsWith( S_FILE ) || ( uri.toLowerCase().startsWith( S_HTTP )))){
			return URICompletion.complete(uri);
		}
		if( this.isOnServer() )
			return URICompletion.complete(uri);

		uri = S_HTTP + "://" + uri;
		uri = URICompletion.complete(uri);	
		return uri;
	}

	@Override
	public void fill(String type, String resource)
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		data.fill(type, resource);
	}

	@Override
	public String getResource()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getResource();
	}

	@Override
	public void fill(String datauri)
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		data.fill( datauri);
	}

	@Override
	public String getMimeType()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getMimeType();
	}

	@Override
	public String getMimeTypeExtension()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getMimeTypeExtension();
	}

	@Override
	public String getCharset()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getCharset();
	}

	@Override
	public String getEncoding()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getEncoding();
	}

	@Override
	public boolean isBase64Encoded()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.isBase64Encoded();
	}

	@Override
	public String getType()
	{
		IDataURI data = ( IDataURI )super.getStoredDescriptor(); 
		return data.getType();
	}

	@Override
	public void setURI(String uri) {
		super.setSource( uri );
	}
}
