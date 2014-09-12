package org.aieonf.collections;

import org.aieonf.collections.connector.AbstractCollectionConnector;
import org.aieonf.collections.connector.ConnectionException;
import org.aieonf.collections.locator.FileLocator;
import org.aieonf.collections.persistence.ConceptPersistence;
import org.aieonf.concept.*;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.locator.LocatorAieon;
import org.aieonf.concept.security.IPasswordAieon;
import org.aieonf.concept.security.PasswordAieon;
import org.aieonf.util.logger.Logger;
import org.aieonf.util.persistence.IPersistence;

import java.io.File;
import java.util.Calendar;

/**
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
public class FileCollectionConnector<T extends IDescribable<?>> extends AbstractCollectionConnector<T>
{
	//Error messages
	public static final String S_ERR_OPENING = 
		"The loader is opening the collection, but it does not does not exist: ";
	public static final String S_ERR_CREATING = 
			"The file cannot be created at the provided loacation: ";
	public static final String S_ERR_REGISTERING = 
		"The loader is registering, but the collection exists: ";
	public static final String S_ERR_NOT_A_PASSWORD = 
		"The password aieon does not have the correct attributes: ";
	public static final String S_ERR_NOT_A_LOADER = 
		"The aieon does not have the correct attributes ";
	public static final String S_ERR_NO_SOURCE_FOUND = 
		"The aieon does not have a source ";
	public static final String S_ERR_NO_IDENTIFIER_FOUND = 
		"The aieon does not have an identifier ";
	public static final String S_ERR_NO_URI_FOUND = 
		"The aieon does not have a URI ";
	public static final String S_ERR_NO_SECRETKEY_FOUND = 
		"The aieon does not have a secret key ";
	
  //Manifest aieons are always stored as encrypted concepts
  private IPersistence<IConcept> persist;

  /**
   * Returns true if the manifest is created
  */
  private boolean created;

  private Logger logger;

  /**
   * The loader manager Manages loading of the manifest aieons
   * @param collection AbstractFileCollection
   * @param loader LoaderAieon
   * @throws CollectionException
   */
  public FileCollectionConnector( ILoaderAieon loader, IDescribableCollection<T> collection )
      throws CollectionException
  {
    //Check the loader aieon
    super( loader, true );
  	this.logger = Logger.getLogger( this.getClass() );
    this.persist = new ConceptPersistence();
    this.checkLoader( loader );
    this.created = false;
  }

  /**
   * Get the persistence
   * @return
  */
  public IPersistence<IConcept> getPersistence()
  {
  	return this.persist;
  }
  
	/**
   * Connect the collection
   *
   * @throws ConnectionException
  */
  @Override
  public void connect() throws ConnectionException
  {
  	ILoaderAieon loader=  super.getLoader();
  	File file = LocatorAieon.getFileFromURI( loader.getURI() );
    logger.trace( "Initialising source: " + loader.getURI() + ": " + file.exists());
    if( file.exists() == false ){
    	try{
    		if( this.allowCreate() ){
      		File parent = file.getParentFile();
    			this.created = parent.mkdirs();
    			if( this.created == false )
       			throw new CollectionException( S_ERR_CREATING + file.getAbsolutePath() );    				
    		}
    		else
    			throw new CollectionException( S_ERR_OPENING + loader.getURI() );
    	}
    	catch( Exception ex ){
    		throw new ConnectionException( ex.getMessage(), ex );
    	}		
    }
    super.connect();
  }

  /**
   * If true, a new collection can be created
   * @return
   * @throws ConceptException
   */
  public boolean allowCreate() throws ConceptException
  {
  	ILoaderAieon loader=  super.getLoader();
    logger.trace( "Is password aieon " + PasswordAieon.isPasswordAieon( loader ) );
		if( PasswordAieon.isPasswordAieon( loader ) == false )
			return true;
		
		IPasswordAieon aieon;
		if( loader instanceof PasswordAieon )
			aieon = ( IPasswordAieon )loader;
		else
			aieon = new PasswordAieon();
		return aieon.isRegistering();
  }

  /**
   * If true, a new collection may be opened
   * @return
   * @throws ConceptException
   */
  public boolean allowOpen() throws ConceptException
  {
  	ILoaderAieon loader=  super.getLoader();
		if( PasswordAieon.isPasswordAieon( loader ) == false )
			return true;
    logger.trace( "The loader is a registering: " + 
    		loader.getURI() + ": " + this.allowCreate());		
		return true;
  }

  /**
   * If true, the given manifest implies the loader
   * @param manifest
   * @return
   * @throws ConceptException
   */
  final boolean implies( ManifestAieon manifest )
  	throws ConceptException
  {
		if(( manifest == null ) || ( manifest.getURI() == null ))
			return false;
  	ILoaderAieon loader=  super.getLoader();
   	logger.trace( "Checking source: " + manifest.getURI() + 
				" with " + loader.getURI() );
  	if( manifest.implies( loader ) == false )
    	return false;
    logger.trace( "Checking manifest with password " );
		if( PasswordAieon.isPasswordAieon( manifest ) == false )
			return true;
		
		PasswordAieon aieon;
		if( loader instanceof PasswordAieon )
			aieon = ( PasswordAieon )loader;
		else
			aieon = new PasswordAieon( loader );
		return aieon.implies( manifest );
  }
  
  /**
   * Returns true if the database exists
   *
   * @return boolean
  */
  @Override
  public boolean exists()
  {
  	ILoaderAieon loader=  super.getLoader();
    FileLocator locator = new FileLocator( loader );
    return ( locator.getSourceFile().exists() );
  }

  /**
   * Returns true if the manifest is created
   * @return boolean
   */
  public boolean isCreated()
  {
    return this.created;
  }

  /**
   * Returns true if the loader contains the correct attributes
   * @param loader LoaderAieon
   * @return boolean
   * @throws ConceptException
  */
  protected void checkLoader( ILoaderAieon loader)
  	throws CollectionException
  {
  	if( loader instanceof PasswordAieon ){

  		String key = loader.get( ConceptBase.getAttributeKey( ILoaderAieon.Attributes.LOADER ));
  		logger.trace( "Extended name = null: " + ( key== null ));
  		if( PasswordAieon.isPasswordAieon( loader ) == false ){
  			logger.trace( "Did not pass as a password aieon");
  			throw new CollectionException( S_ERR_NOT_A_PASSWORD + loader.getURI() );
  		}
  	}else{
  		if( LoaderAieon.isLoaderAieon( loader ) == false ){
  			logger.trace( "Did not pass as a loader aieon");
  			throw new CollectionException( S_ERR_NOT_A_LOADER + loader.getURI() );
  		}
  	}
		logger.trace( "Checking source");
    String source = loader.getURI().getPath().trim();
		if(( loader.getURI() == null ) || Descriptor.isNull( source ))
			throw new CollectionException( S_ERR_NO_SOURCE_FOUND + loader.toString() );
 		logger.trace( "Checking identifier");
  	if(( loader.getIdentifier() == null ) || loader.getIdentifier().trim().equals( "" ))
			throw new CollectionException( S_ERR_NO_IDENTIFIER_FOUND + loader.getURI() );
		logger.trace( "Checking uri");
    if(( loader.getURI() == null ) || Descriptor.isNull( loader.getURI().getPath() ))
			throw new CollectionException( S_ERR_NO_URI_FOUND + loader.getURI() );
		logger.trace( "Checking key");
  }

  /**
   * Create a manifest document
   *
   * @return ManifestAieon
   * @throws ConceptException
   * @throws CollectionException
  */
  public ManifestAieon createManifest()
    throws ConceptException, CollectionException
  {
  	ILoaderAieon loader=  super.getLoader();
    ManifestAieon manAieon = ( ManifestAieon )BodyFactory.transform( ManifestAieon.class, loader);
    manAieon.set( IDescriptor.Attributes.NAME.name(), ManifestAieon.MANIFEST );
    manAieon.setVersion( 1 );
    Descriptor.setCreateDate( manAieon,
                                  Calendar.getInstance().getTime() );
    manAieon.set( IDescriptor.Attributes.ID.name(), BodyFactory.IDFactory());
    manAieon.setURI( loader.getURI() );
    logger.trace( "Adding name " + loader.get( IPasswordAieon.Attributes.USER_NAME.name() ));
    return manAieon;
  }
}
