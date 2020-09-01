/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.context;

import java.io.File;
import java.net.URI;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.Universe;
import org.aieonf.concept.body.IBodyAieon;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Concept;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.xml.StoreConcept;

/**
 * Create a concept, using a properties file
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class ContextAieon extends Concept implements IContextAieon
{
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -3648972713512835462L;


	//Error messages
	public static final String S_ERR_NO_APPLICATION_NAME = 
			"The application has not been named yet. Please set the application name first";
	public static final String S_ERR_NO_CONTEXT = 
			"The context has not been named yet. Please set the context first";
	public static final String S_ERR_NO_MANUFACTURER_NAME = 
			"The name of the manufacturor has not been named yet. Please set the manufacturor first";

	/**
	 * Default constructor
	 * @throws ConceptException
	 */
	public ContextAieon()
	{
		super.setScope( IConcept.Scope.APPLICATION );
		this.setLocationType( LocationType.APPLICATION );
	}

	/**
	 * Create a new application concept and add the serial number
	 * as fixed attribute, so that it can not be modified
	 *
	 * @param identifier String
	 * @param source String
	 * @param applicationName String
	 * @param applicationID String
	 * @param secretKey String
	 */
	protected ContextAieon( long id, String source, String applicationName,
			String applicationID,
			String secretKey )
	{
		super( id, source );
		super.set( ILoaderAieon.Attributes.INTERNAL, Boolean.TRUE.toString());
		super.set( IContextAieon.S_APPLICATION, applicationName );
		super.setScope( IConcept.Scope.APPLICATION );
		this.set( IContextAieon.Attributes.APPLICATION_ID.toString(), applicationID );
		this.set( IContextAieon.Attributes.APPLICATION_DOMAIN.toString(), source );
		this.setLocationType( LocationType.APPLICATION );
		super.set( IDescriptor.Attributes.VERSION, String.valueOf(1 ));
	}

	public ContextAieon(IDescriptor base) {
		super();
	}

	
	/**
	 * Get the user directory
	 *
	 * @return String
	 */
	@Override
	public URI getUserDirectory()
	{
		if( this.getOrganisation() == null )
			throw new NullPointerException( S_ERR_NO_MANUFACTURER_NAME );
		File user = new File( ContextAieon.getDefaultUserDir( this ));
		File file = new File( user, this.getOrganisation() );
		return file.toURI();
	}


	/**
	 * Get the location type for this context aieon
	 * @return
	 */
	public LocationType getLocationType()
	{
		String str = this.get( IContextAieon.Attributes.LOCATION_TYPE ); 
		return LocationType.valueOf( str );
	}

	/**
	 * Set the location type for this context aioen
	 * @return
	 */
	public void setLocationType( LocationType locationType )
	{
		this.set( IContextAieon.Attributes.LOCATION_TYPE, locationType.name() ); 	
	}

	/**
	 * Get the root of the local databases
	 * @return
	 */
	public URI getLocalDatabaseRoot()
	{
		File file = new File( S_DATABASE + File.separator );
		return file.toURI();
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IContextAieon#getApplicationDatabaseSource()
	 */
	@Override
	public String getApplicationDatabaseSource()
	{
		if( this.getApplicationName() == null )
			throw new NullPointerException( S_ERR_NO_APPLICATION_NAME );
		return this.getLocalDatabaseRoot() + this.getApplicationName() + "." + Universe.CDX;
	}

	/**
	 * Get the user database source
	 * @return
	 */
	@Override
	public URI getUserDatabaseSource()
	{
		return this.getNamedUserDatabaseSource( this.getContext());
	}

	/**
	 * Get the user database source
	 * @return
	 */
	@Override
	public URI getNamedUserDatabaseSource( String name )
	{
		if( this.getContext() == null )
			throw new NullPointerException( S_ERR_NO_CONTEXT );
		File user = new File( ContextAieon.getDefaultUserDir( this ) );
		File file = new File( user, name + "." + Universe.CDX );
		return file.toURI();
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IContextAieon#getApplicationName()
	 */
	@Override
	public String getApplicationName()
	{
		return this.get( IContextAieon.Attributes.APPLICATION_NAME );
	}

	/**
	 * Set the id of the instantiation
	 *
	 * @param applicationName String
	 */
	public void setApplicationName( String applicationName )
	{
		this.set( IContextAieon.Attributes.APPLICATION_NAME, applicationName );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IContextAieon#getApplicationID()
	 */
	@Override
	public String getApplicationID()
	{
		return this.get( IContextAieon.Attributes.APPLICATION_ID );
	}

	/**
	 * Set the id of the application
	 *
	 * @param applicationID String
	 */
	public void setApplicationID( String applicationID )
	{
		this.set( IContextAieon.Attributes.APPLICATION_ID, applicationID );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.context.IContextAieon#getApplicationVersion()
	 */
	@Override
	public String getApplicationVersion()
	{
		return this.get( IContextAieon.Attributes.APPLICATION_VERSION );
	}

	/**
	 * Set the version of the application
	 *
	 * @param applicationVersion String
	 */
	public void setApplicationVersion( String applicationVersion )
	{
		this.set( IContextAieon.Attributes.APPLICATION_VERSION, applicationVersion );
	}

	/**
	 * Get the application domain. By default this is the bundle id.
	 *
	 * @return String
	 */
	@Override
	public String getApplicationDomain(){
		return this.get( IContextAieon.Attributes.APPLICATION_DOMAIN );
	}
	
	/* (non-Javadoc)
	 * @see org.condast.concept.context.IContextAieon#getApplicationName()
	 */
	@Override
	public String getContext()
	{
		return this.get( IContextAieon.Attributes.CONTEXT );
	}

	/**
	 * Set the context
	 *
	 * @param context String
	 */
	public void setContext( String context )
	{
		this.set( IContextAieon.Attributes.CONTEXT, context );
	}

	/**
	 * Get the organisation of the application
	 *
	 * @return String
	 */
	public String getOrganisation()
	{
		return this.get( IContextAieon.Attributes.ORGANISATION );
	}

	/**
	 * Set the name of the organisation
	 *
	 * @param organisation String
	 */
	public void setOrganisation( String organisation )
	{
		this.set( IContextAieon.Attributes.ORGANISATION, organisation );
	}

	/**
	 * Get the web site of the organisation
	 *
	 * @return String
	 */
	public String getWebsite()
	{
		return this.get( IContextAieon.Attributes.WEBSITE );
	}

	/**
	 * Set the web site of the organisation
	 *
	 * @param manufacturor String
	 */
	public void setWebsite( String website )
	{
		this.set( IContextAieon.Attributes.WEBSITE, website );
	}

	/**
	 * Every body aieon should test that all the required data
	 * is actually avaialable. Returns true if this is the case
	 * @throws cnceptException if the verification failed
	 */
	@Override
	public void verify() throws NullPointerException
	{
		IContextAieon.Attributes[] attrs = IContextAieon.Attributes.values();
		String error = IBodyAieon.S_ERR_INVALID_LOADER + 
				this.getClass().getName() + ": ";

		for( IContextAieon.Attributes attr: attrs ){
			if( this.get( attr ) == null ){
				System.err.println( "No value found for: " + attr + "\n" + StoreConcept.print(this, true));
				throw new NullPointerException( error + attr );
			}
		}
	}

	@Override
	public String toString()
	{
		return this.getSource() + this.getApplicationName() + ":" + this.getApplicationID() + ":" + this.getApplicationVersion(); 
	}

	/**
	 * Return the default user directory. This is '%system-user%\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getPublicDatabaseDir( IContextAieon aieon )
	{
		File file = new File( S_DATABASE + File.separator + 
				aieon.getApplicationName() + File.separator +
				aieon.getSource() + File.separator );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getDefaultUserDir( IContextAieon aieon )
	{
		if( aieon.getApplicationName() == null )
			throw new NullPointerException( S_ERR_NO_APPLICATION_NAME );
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator +
				"." + aieon.getApplicationName().toLowerCase() + File.separator +
				aieon.getSource() + File.separator );
		return file.toURI();
	}

	/**
	 * Create an ID from the hash code
	 * @param aieon
	 * @return
	 */
	protected static String createID( String name, String version ){
		String str = name + version;
		return String.valueOf( str.hashCode() );
	}
}