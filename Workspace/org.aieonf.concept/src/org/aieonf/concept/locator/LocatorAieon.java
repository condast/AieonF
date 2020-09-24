package org.aieonf.concept.locator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.core.EmbeddedAieon;
import org.aieonf.concept.implicit.ImplicitAieon;

/**
 * <p>Title: Conceptual Network Database</p>
 * <p> A locator descriptor describes a location of a located collection</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class LocatorAieon extends ImplicitAieon 
implements ILocatorAieon
{
	/**
	 * Needed for serialisation
	 */
	private static final long serialVersionUID = 7821428917476347114L;

	/**
	 * Create a default locator descriptor
	 * @throws ConceptException
	 */
	public LocatorAieon() 
	{
		this( ILocatorAieon.Attributes.URI.name() );
		EmbeddedAieon.setEmbedded( this );
	}

	/**
	 * Create a default locator descriptor

	 */
	protected LocatorAieon( String attribute ) 
	{
		super( ILocatorAieon.Attributes.LOCATOR.toString(), attribute );
		super.setScope( Scope.APPLICATION );
		this.setName( ILocatorAieon.Attributes.LOCATOR.toString() );
		this.set( ILocatorAieon.Attributes.LOCATOR, Boolean.TRUE.toString() );
		EmbeddedAieon.setEmbedded( this );
	}

	/**
	 * Create a default locator descriptor
	 *
	 * @param name Name
	 * @param identifier String
	 * @param source String
	 * @throws ConceptException
	 * @throws MalformedURLException 
	 */
	public LocatorAieon( String name, String identifier, URI source ) throws MalformedURLException
	{
		super( ILocatorAieon.Attributes.URI.name(),source.getPath() );
		this.setName( name );
		this.setURI( source );
		this.setIdentifier( identifier );
		EmbeddedAieon.setEmbedded( this );
	}

	public LocatorAieon( IDescriptor descriptor ) {
		super( descriptor.getBase());
		super.setScope( Scope.APPLICATION );
		this.setName( ILocatorAieon.Attributes.LOCATOR.toString() );
		this.set( ILocatorAieon.Attributes.LOCATOR, Boolean.TRUE.toString() );
		EmbeddedAieon.setEmbedded( this );		
	}

	/**
	 * Get the identifier
	 * @return String
	 */
	@Override
	public String getIdentifier()
	{
		return this.get( ILocatorAieon.Attributes.IDENTIFIER );
	}

	/**
	 * Set the identifier
	 *
	 * @param identifier String
	 */
	@Override
	public void setIdentifier( String identifier )
	{
		super.set( ILocatorAieon.Attributes.IDENTIFIER, identifier );
	}

	/**
	 * Get the source
	 * @return String
	 */
	@Override
	public URI getURI()
	{
		String str = super.get( ILocatorAieon.Attributes.URI.name() );
		if( Utils.assertNull(str))
			return null;
		return URI.create( str );
	}

	/**
	 * Set the source
	 *
	 * @param source String
	 */
	@Override
	public void setURI( URI source )
	{
		super.set( ILocatorAieon.Attributes.URI.name(), getStringFromURI( source ));
	}

	@Override
	public String getFromExtendedKey(String key)
	{
		String extended = super.get( IDescriptor.Attributes.EXTENDED_KEY );
		extended = extended.replaceAll("[$]", ".");
		if( Descriptor.isNull( extended ))
			return super.get(key);
		return super.get( extended + "." + key);
	}

	/**
	 * The extended key is used to group attributes together. It is set once for a concept, usually
	 * based on the enum type
	 * 
	 * @param extended
	 */
	@Override
	protected void setExtendedKey( String extended ){
		super.set(IDescriptor.Attributes.EXTENDED_KEY, extended );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.wrapper.ConceptWrapper#hashCode()
	 */
	@Override
	public int hashCode()
	{
		String hash = super.toString() + ":" + this.getURI();
		return hash.hashCode();
	}

	/**
	 * Get a valid URI from the given  URI, by making it fully URI compatible.
	 * 
	 * @param value
	 * @return String
	 */
	public static String getStringFromURI( URI value )
	{
		String replace = value.toString();
		replace = replace.replace(" ", "%20" );
		return replace;
	}

	/**
	 * Get a valid URI from the given  URI, by making it fully URI compatible.
	 * 
	 * @param value
	 * @return String
	 */
	public static File getFileFromURI( URI value )
	{
		String replace = value.getPath();
		replace = replace.replace("%20", " " );
		//replace = replace.replace("Program Files", "Program_Files" );
		return new File( replace );
	}
}