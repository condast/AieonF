package org.aieonf.concept.library;

import java.net.URL;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.xml.URICompletion;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.datauri.DataURI;
import org.aieonf.concept.implicit.IImplicitAieon;
import org.aieonf.concept.implicit.ImplicitAieon;

/**
 * Create an url aieon
 * @author Kees
 *
 */
public class URLAieon extends DataURI implements IImplicitAieon<IDescriptor>
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
		SERVER,
		CREATABLE;

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
		super(  );
		set( IImplicit.Attributes.IMPLICIT.name(), IConcept.Attributes.SOURCE.name());
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	 */
	public URLAieon( String uri ) 
	{
		super( IConcept.Attributes.SOURCE.name() );
		set( IImplicit.Attributes.IMPLICIT.name(), IConcept.Attributes.SOURCE.name());
		super.setSource( uri );
		super.setDescription(uri);
		DataURI data = (DataURI) super.getDescriptor();
		data.setDataName(uri);
	}

	/**
	 * Create a domain name with the given name
	 * @param domain
	 */
	public URLAieon( String name, String uri ) 
	{
		super( Attributes.URL.name() );
		set( IImplicit.Attributes.IMPLICIT.name(), IConcept.Attributes.SOURCE.name());
		super.setSource( uri );
		super.setIdentifier(name);
		super.setDescription(name);
		DataURI data = (DataURI) super.getDescriptor();
		data.setDataName(name);
	}

	public URLAieon( URL url ){
		this( url.toExternalForm() );
	}
	
	public URLAieon( IDescriptor descriptor ) {
		super( descriptor );
		set( IImplicit.Attributes.IMPLICIT.name(), IConcept.Attributes.SOURCE.name());
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
		return super.getBoolean( super.get( Attributes.SERVER ));
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
	public void setURI(String uri) {
		super.setSource( uri );
	}

	@Override
	public boolean test(IDescriptor descriptor) {
		IImplicitAieon<IDescriptor> implicit = new ImplicitAieon( super.getBase(), IConcept.Attributes.SOURCE.name() );
		return implicit.test(descriptor);
	}

	@Override
	public boolean accept(IDescriptor descriptor) {
		IImplicitAieon<IDescriptor> implicit = new ImplicitAieon( super.getBase(), IConcept.Attributes.SOURCE.name() );
		return implicit.accept(descriptor);
	}

	@Override
	public boolean isFamily(Object arg0) {
		IImplicitAieon<IDescriptor> implicit = new ImplicitAieon( super.getBase(), IConcept.Attributes.SOURCE.name() );
		return implicit.isFamily(arg0);
	}

	@Override
	public String getImplicit() {
		return get( IImplicit.Attributes.IMPLICIT.name());
	}
	
	/**
	 * This utility replaces some causes of failing markup text exceptions
	 * @param string
	 * @return
	 */
	public static String createMarkupString( String name, String uri ) {
		String result = name.replace("&", "en");
		return " <a href='" + uri + "' target='_blank'>" + result + "</a>"; 
	}
}
