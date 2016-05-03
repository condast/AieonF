package org.aieonf.concept.context;

import java.net.URI;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.domain.IDomainAieon;

public interface IContextAieon extends IConcept
{
	//The name of the root concept
	public static final String S_APPLICATION = "Application";
	public static final String S_DATABASE = "Database";
	public static final String S_CONFIG = "config";
	public static final String S_USER_HOME_PROPERTY = "user.home";

	/**
	 * Specifies different types of locations.
	 * - Local: the context aieon is stored locally (e.g. within a plugin)
	 * - Application: the context aieon is stored in the application root
	 * - User: the context aieon is stored in a user directory
	 * - URI: the context is stored as a URI.  
	 * @author keesp
	 *
	 */
	public enum LocationType
	{
		LOCAL,
		APPLICATION,
		USER,
		URI;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * The basic elements of an application aieon
	 */
	public enum Attributes
	{
		APPLICATION_NAME,
		APPLICATION_ID,
		APPLICATION_DOMAIN,
		APPLICATION_VERSION,
		CONTEXT,
		LOCATION_TYPE,
		ORGANISATION,
		WEBSITE,
		LICENSE;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		public static boolean isValid( String str ){
			for( Attributes attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}
	}
	
	/**
	 * A context aieon is always associated with one domain.
	 * @return
	 */
	public IDomainAieon getDomain();
	
	/**
	 * Get the application database
	 * @return
	 */
	public abstract String getApplicationDatabaseSource();

	/**
	 * Get the name of the application
	 *
	 * @return String
	 */
	public abstract String getApplicationName();

	/**
	 * Get the id of the application
	 *
	 * @return String
	 */
	public abstract String getApplicationID();

	/**
	 * Get the version of the application
	 *
	 * @return String
	 */
	public abstract String getApplicationVersion();

	/**
	 * Get the application domain
	 *
	 * @return String
	 */
	public abstract String getApplicationDomain();

	/**
	 * Get the context within the application
	 * @return
	 */
	public String getContext();

	/**
	 * Get the user database source
	 * @return
	 */
	public URI getUserDatabaseSource();

	/**
	 * Get the user database source
	 * @return
	 */
	public URI getNamedUserDatabaseSource( String name );

	/**
	 * Get the user directory
	 *
	 * @return String
	 */
	public URI getUserDirectory();

	public void verify() throws NullPointerException;
}