/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.security;

//Concept imports
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.Universe;
import org.aieonf.concept.body.IBodyAieon;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.loader.LoaderAieon;

/**
 *
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions
 * Create a descriptor, using a properties file</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class PasswordAieon extends LoaderAieon implements IPasswordAieon
{
	public static final String S_DEFAULT_IDENTIFIER = 
			"org.condast.private";

	/**
	 * For serialisation purposes
	 */
	private static final long serialVersionUID = -3835167323842806277L;

	/**
	 * Create a default password aieon
	 * @throws ConceptException
	 */
	public PasswordAieon()
	{
		super();
		this.setAieonCreatorClass( this.getClass() );
		super.setSource( S_DEFAULT_URI );
		this.setRegister( false );
		super.setName( IPasswordAieon.Attributes.PASSWORD.name() );
		this.setScope( Scope.APPLICATION );
		super.setClassName( this.getClass().getName() );
		super.setExtendedKey( IPasswordAieon.Attributes.class.getName() );
	}

	/**
	 * Create the password descriptor
	 *
	 * @param identifier String
	 * @param source String
	 * @param userName String
	 * @param password String
	 * @throws ConceptException
	 */
	public PasswordAieon( String userName, String password )
	{
		super();
		this.setUserName( userName );
		this.setPassword( password );
		this.setRegister( false );
		this.setAieonCreatorClass( this.getClass() );
		this.setScope( Scope.APPLICATION );
		super.setName( IPasswordAieon.Attributes.PASSWORD.name() );
		super.setClassName( this.getClass().getName() );
		super.setSource( S_DEFAULT_URI );
		super.setExtendedKey( IPasswordAieon.Attributes.class.getName() );
			}

	/**
	 * Create the password descriptor
	 *
	 * @param identifier String
	 * @param source URI
	 * @param userName String
	 * @param password String
	 * @throws ConceptException
	 * @throws MalformedURLException 
	 */
	public PasswordAieon( String identifier, URI source, 
			String userName, String password )
					throws MalformedURLException
					{
		super( identifier, source );
		this.setUserName( userName );
		this.setPassword( password );
		this.setRegister( false );
		this.setScope( Scope.APPLICATION );
		this.setAieonCreatorClass( this.getClass() );
		super.setName( IPasswordAieon.Attributes.PASSWORD.name() );
		super.setClassName( this.getClass().getName() );
		super.setSource( S_DEFAULT_URI );
		super.setExtendedKey( IPasswordAieon.Attributes.class.getName() );
					}

	/**
	 * Get the password descriptor
	 * 
	 * @param concept
	 */
	public PasswordAieon( IDescriptor descriptor )
	{
		super( descriptor );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#getUserName()
	 */
	@Override
	public String getUserName()
	{
		String userName = this.get(IPasswordAieon.Attributes.USER_NAME.toString());
		return StringUtils.isEmpty(userName)? this.get( IPasswordAieon.Attributes.USER_NAME ): userName;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName( String userName )
	{
		this.set( IPasswordAieon.Attributes.USER_NAME, userName );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#getPassword()
	 */
	@Override
	public String getPassword()
	{
		String password = this.get(IPasswordAieon.Attributes.PASSWORD.toString());
		return StringUtils.isEmpty(password)? this.get( IPasswordAieon.Attributes.PASSWORD ): password;
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword( String password )
	{
		this.set( IPasswordAieon.Attributes.PASSWORD, password );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#getPassword()
	 */
	@Override
	public String getConfirmation()
	{
		return this.get( IPasswordAieon.Attributes.CONFIRMATION );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#setPassword(java.lang.String)
	 */
	@Override
	public void setConfirmation( String password )
	{
		this.set( IPasswordAieon.Attributes.CONFIRMATION, password );
	}

	/**
	 * Get the alternative identifier for the aieon.
	 * This is based on the user name
	 * @return
	 * @throws IOException
	 */
	public String getAlternativeIdentifier() throws IOException
	{
		String[] split = super.getIdentifier().split( "[\\.]" );
		int last = super.getIdentifier().length() - split[ split.length - 1 ].length() - 1;
		return super.getIdentifier().substring( 0, last) + "." + this.getUserName();
	}

	/**
	 * Get the alternative source for the aieon.
	 * This source is based on the user name
	 * @return
	 * @throws IOException
	 */
	public URI getAlternativeSource() throws IOException
	{
		File parent = new File( super.getURI() ).getParentFile();
		File file = new File( parent.getCanonicalPath() + "\\" + this.getUserName() + 
				"." + Universe.CDX );
		return file.toURI();
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#isRegistering()
	 */
	@Override
	public boolean isRegistering()
	{
		String attr = ConceptBase.getAttributeName( IPasswordAieon.Attributes.class, IPasswordAieon.Attributes.REGISTER.name() );
		return super.getBoolean( attr );	
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#setRegister(boolean)
	 */
	@Override
	public void setRegister( boolean register )
	{
		this.set( IPasswordAieon.Attributes.REGISTER, String.valueOf( register ));
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.security.IPasswordAieon#isValidUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isValidUser( String userName, String password )
	{
		String user = this.getUserName();
		String pw = this.getPassword();
		return ( user.equals( userName ) && pw.equals( password ));
	}

	/**
	 * Returns true if the given concept is equal to this one
	 *
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean implies( IDescriptor descriptor )
	{
		if( super.implies( descriptor ) == false )
			return false;

		String attr = ConceptBase.getAttributeKey( IPasswordAieon.Attributes.USER_NAME );
		String value = descriptor.get( attr );
		if(( value == null ) || ( value.equals( this.getUserName() ) == false ))
			return false;
		attr = ConceptBase.getAttributeKey( IPasswordAieon.Attributes.PASSWORD );
		value = descriptor.get( attr );
		return (( value != null ) && ( value.equals( this.getPassword() )));
	}

	/**
	 * Returns true if this concept accepts the given one as equal
	 *
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean accept( IDescriptor descriptor ) {
		if( super.accept( descriptor ) == false )
			return false; 
		if( this.implies( descriptor ) == false )
			return false;
		String attr = super.getEncryptionKey();
		return ( descriptor.get( attr ) != null );
	}

	/**
	 * Returns true if the given concept is of the same type as this one.
	 * The default behaviour returns true;
	 *
	 * @param concept IConcept
	 * @return boolean
	 */
	public boolean isClass(IDescriptor descriptor) {
		String attr = ConceptBase.getAttributeKey( IPasswordAieon.Attributes.USER_NAME );
		if( descriptor.get( attr ) == null )
			return false;
		attr = ConceptBase.getAttributeKey( IPasswordAieon.Attributes.PASSWORD );
		return ( descriptor.get( attr ) != null );
	}

	/**
	 * Returns a string representation of the concept (XML)
	 *
	 * @return String
	 */
	@Override
	public String toString()
	{
		return this.getID() + ":" + this.getUserName();
	}

	/**
	 * If true, the given concept is a password descriptor
	 *
	 * @param concept IConcept
	 * @return boolean
	 */
	public static boolean isPasswordAieon( IDescriptor descriptor )
	{
		if( LoaderAieon.isLoaderAieon(descriptor) == false )
			return false;
		PasswordAieon aieon = new PasswordAieon();
		return aieon.isClass( descriptor );
	}


	/* (non-Javadoc)
	 * @see org.condast.concept.loader.LoaderAieon#verify()
	 */
	@Override
	public void verify() throws NullPointerException
	{
		String error = IBodyAieon.S_ERR_INVALID_LOADER +  this.getClass().getName() + ": ";
		IPasswordAieon.Attributes[] attrs = IPasswordAieon.Attributes.values();
		for( IPasswordAieon.Attributes attr: attrs ){
			if( this.get( attr ) == null )
				throw new NullPointerException( error + attr );
		}		
	}

	
	@Override
	public boolean implies(Object obj) {
		if( !( obj instanceof IDescriptor ))
				return false;
		IDescriptor descriptor = (IDescriptor) obj;
		String userName = PasswordAieon.getUserName( descriptor);
		if( Utils.assertNull( userName ) || ( !userName.equals( this.getUserName() )))
			return false;
		String password = PasswordAieon.getPassword( descriptor );
		return (!Utils.assertNull( password ) && ( password.equals( this.getPassword() )));
	}

	/**
	 * Get the user name of the given concept, or null if none was found
	 *
	 * @param concept IConcept
	 * @return String
	 */
	public static String getUserName( IDescriptor concept )
	{
		String key = ConceptBase.getAttributeKey( IPasswordAieon.Attributes.USER_NAME );
		return( concept.get( key ));
	}

	/**
	 * Get the password of the given concept, or null if none was found
	 *
	 * @param concept IConcept
	 * @return String
	 */
	static String getPassword( IDescriptor concept )
	{
		return( concept.get( ConceptBase.getAttributeKey( IPasswordAieon.Attributes.PASSWORD )));
	}  

	/**
	 * Get the password of the given concept, or null if none was found
	 *
	 * @param concept IConcept
	 * @return String
	 */
	public static boolean isCorrectPassword( IConcept concept, String password )
	{
		if( PasswordAieon.isPasswordAieon( concept ) == false )
			return false;
		String pwd = concept.get( ConceptBase.getAttributeKey( IPasswordAieon.Attributes.PASSWORD ));
		return password.trim().toLowerCase().equals( pwd );
	}  

	/**
	 * returns true if the password is equal to the confirmation
	 * @param concept
	 * @return
	 */
	public static boolean isPasswordConfirmed( IConcept concept ){
		if( PasswordAieon.isPasswordAieon( concept ) == false )
			return false;
		String pwd = concept.get( ConceptBase.getAttributeKey( IPasswordAieon.Attributes.PASSWORD ));
		if( Descriptor.isNull( pwd ))
			return false;
		pwd = pwd.trim();
		String confirm = concept.get( ConceptBase.getAttributeKey( IPasswordAieon.Attributes.CONFIRMATION ));
		if( Descriptor.isNull( confirm ))
			return false;
		confirm = confirm.trim().toLowerCase();
		return pwd.equals(confirm );

	}
}