package org.aieonf.concept.loader;

//J2SE
import java.net.*;
import java.util.logging.Logger;

import org.aieonf.commons.encryption.IEncryption;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.body.IBodyAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.locator.LocatorAieon;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class LoaderAieon extends LocatorAieon implements ILoaderAieon
{
	/**
	 *
	 */
	private static final long serialVersionUID = 518014831275550194L;

	//The schema for a file uri
	public static final String S_FILE = "file:";
	public static final String S_DEFAULT_URI = "home:";
	public static final String S_DEBUG_MODE = "Debug";

	//Error messages
	public static final String S_ERR_NO_BODY_CLASS =
			"No body class was provided for this loader";

	/**
	 * Create a loader aieon for the given URI
	 *
	 * @throws ConceptException
	 */
	public LoaderAieon()
	{
		super( ILoaderAieon.Attributes.LOADER.name() );
		super.setName( ILoaderAieon.Attributes.LOADER.name() );
		super.setScope( IConcept.Scope.APPLICATION );
		this.set( ILoaderAieon.Attributes.INTERNAL.name(), Boolean.TRUE.toString() );
		this.set( ILoaderAieon.Attributes.CREATABLE.name(), Boolean.FALSE.toString() );
		this.setReadOnly( Boolean.TRUE );
		this.setAieonCreatorClass( ILoaderAieon.class );
	}

	/**
	 * Create a loader aieon for the given URI
	 *
	 * @param identifier String
	 * @param uri URI
	 * @throws ConceptException
	 * @throws MalformedURLException 
	 */
	public LoaderAieon( String identifier, URI uri ) throws MalformedURLException
	{
		super( ILoaderAieon.Attributes.LOADER.name(), identifier, uri );
		super.setScope( IConcept.Scope.APPLICATION );
		this.set( ILoaderAieon.Attributes.INTERNAL.name(), Boolean.TRUE.toString() );
		this.set( ILoaderAieon.Attributes.CREATABLE.name(), Boolean.FALSE.toString() );
		this.setReadOnly( Boolean.TRUE );
		this.setAieonCreatorClass( ILoaderAieon.class );
	}

	public LoaderAieon( IDescriptor descriptor ) {
		super( descriptor);		
		super.setName( ILoaderAieon.Attributes.LOADER.name() );
		super.setScope( IConcept.Scope.APPLICATION );
		this.set( ILoaderAieon.Attributes.INTERNAL.name(), Boolean.TRUE.toString() );
		this.set( ILoaderAieon.Attributes.CREATABLE.name(), Boolean.FALSE.toString() );
		this.setReadOnly( Boolean.TRUE );
		this.setAieonCreatorClass( ILoaderAieon.class );
	}

	/**
	 * Returns true if loader is stored internally in the object that is loaded
	 * @return
	 */
	@Override
	public boolean getStoreInternal(){
		String value = this.get( ILoaderAieon.Attributes.INTERNAL.name() );
		if( Descriptor.assertNull( value ))
			return false;
		return ConceptBase.parseBoolean( value );
	}

	/**
	 * Set the internal flag. If true, the loader is stored internally, for instance
	 * as a manifest aieon;
	 * @param choice
	 */
	@Override
	public void setStoreInternal( boolean choice ){
		this.set( ILoaderAieon.Attributes.INTERNAL.name(), String.valueOf( choice ));
	}

	/**
	 * Get the secret key
	 *
	 * @return String
	 * @throws ConceptException
	 */
	@Override
	public String getEncryptionKey()
	{
		return super.get( IEncryption.Attributes.ENCRYPTION_KEY.name() );
	}

	/**
	 * Set the secret key
	 *
	 * @param key String
	 */
	public void setEncryptionKey( String key )
	{
		super.set( IEncryption.Attributes.ENCRYPTION_KEY.name(), key );
	}

	/**
	 * Get the encryption algorithm
	 *
	 * @return IEncryption.Algorithm
	 * @throws ConceptException
	 */
	@Override
	public IEncryption.Algorithms getEncryptionAlgorithm()
	{
		String algorithm = this.get( IEncryption.Attributes.ALGORITHM.name() );
		if( algorithm == null )
			return IEncryption.Algorithms.NONE;
		return IEncryption.Algorithms.valueOf( StringStyler.styleToEnum( algorithm ));
	}

	/**
	 * Set the encryption algorithm
	 *
	 * @param algorithm IEncryption.Algorithm
	 */
	public void setEncryptionAlgorithm( IEncryption.Algorithms algorithm )
	{
		this.set( IEncryption.Attributes.ALGORITHM.name(), algorithm.toString() );
	}

	@Override
	public boolean isCreatable()
	{
		String str = this.get( ILoaderAieon.Attributes.CREATABLE.name() );
		return Boolean.parseBoolean( str );
	}

	@Override
	public void setCreatable(boolean choice)
	{
		this.set( ILoaderAieon.Attributes.CREATABLE.name(), String.valueOf( choice ));
	}


	/**
	 * Set the aieon class that is needed to create the body
	 *
	 * @param clss Class
	 * @throws ConceptException
	 */
	@Override
	public void setAieonCreatorClass( Class<? extends IDescriptor> clss )
	{
		this.set( ILoaderAieon.Attributes.AIEON_CREATOR_CLASS.name(), clss.getName() );
	}

	/**
	 * Returns true if the given descriptor implies this one
	 * @TODO Check or changes
	 */
	@Override
	public boolean test(IDescriptor descriptor) {
		if( super.test( descriptor ))
			return true;
		if( this.isDebugmode() || isDebugMode( descriptor ))
			return true;

		String source = descriptor.get( IConcept.Attributes.URI.name() );
		if( Descriptor.assertNull( source ))
			return false;
		if( this.getURI() == null )
			return false;

		URI uri, thisURI;
		try {
			uri = new URI( source );
			thisURI = this.getURI();
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
		return ( uri.equals( thisURI ));
	}

	/**
	 * Every body aieon should test that all the required data
	 * is actually available. Returns true if this is the case
	 * @throws cnceptException if the verification failed
	 */
	@Override
	public void verify() throws NullPointerException
	{
		String error = IBodyAieon.S_ERR_INVALID_LOADER +  this.getClass().getName() + ": ";
		String source = IConcept.Attributes.SOURCE.name();
		if( super.get( source ) == null )
			throw new NullPointerException( error + source );

		ILoaderAieon.Attributes[] attrs = ILoaderAieon.Attributes.values();
		for( ILoaderAieon.Attributes attr: attrs ){
			if( this.get( attr.name() ) == null )
				throw new NullPointerException( error + attr );
		}
	}

	/**
	 * Returns true if the loader has activated debug mode. This will
	 * override the implies check, so that a test plugin can access the
	 * data of other plugins
	 * @return
	 */
	protected boolean isDebugmode(){
		boolean result = this.getBoolean(S_DEBUG_MODE);
		if( !result )
			return false;
		Logger logger = Logger.getLogger(LoaderAieon.class.getName() );
		logger.info("DEBUG MODE ACTIVATED");
		return result;
	}

	/**
	 * If true, the given concept qualifies as a loader aion.
	 * @param concept IConcept
	 * @return boolean
	 */
	public static boolean isLoaderAieon( IDescriptor descriptor )
	{
		return( descriptor.get( ILoaderAieon.Attributes.LOADER.name()) != null );
	}

	/**
	 * If true, the given descriptor signifies debug mode.
	 * @param concept IConcept
	 * @return boolean
	 */
	public static boolean isDebugMode( IDescriptor descriptor )
	{
		String str = descriptor.get( S_DEBUG_MODE );
		if( Descriptor.assertNull(str))
			return false;
		Boolean result = str.equals(Boolean.TRUE.toString().toLowerCase() );
		if( !result )
			return false;
		Logger logger = Logger.getLogger(LoaderAieon.class.getName() );
		logger.info("DEBUG MODE ACTIVATED");
		return result;
	}

}
