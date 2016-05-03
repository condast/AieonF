package org.aieonf.collections.connector;

import org.aieonf.collections.CollectionException;
import org.aieonf.collections.IAccessible;
import org.aieonf.collections.connector.AbstractCollectionConnector;
import org.aieonf.collections.locator.FileLocator;
import org.aieonf.collections.locator.Locator;
import org.aieonf.concept.*;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;
import org.aieonf.concept.locator.LocatorAieon;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public abstract class AbstractFileConnector<T extends IDescribable<?>> extends AbstractCollectionConnector<T>
{
	//Error messages
	public static final String S_ERR_CONNECTING = 
			"The connection cannot be opened because it does not exist and may not be created: ";
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

	private Logger logger = Logger.getLogger( this.getClass().getName());

	/**
	 * The loader manager Manages loading of the manifest aieons
	 * @param collection AbstractFileCollection
	 * @param loader LoaderAieon
	 * @throws CollectionException
	 */
	public AbstractFileConnector( ILoaderAieon loader, boolean create )
	{
		super( loader, create );
	}

	/**
	 * The loader manager Manages loading of the manifest aieons
	 * @param collection AbstractFileCollection
	 * @param loader LoaderAieon
	 * @throws CollectionException
	 */
	public AbstractFileConnector( ILoaderAieon loader, IAccessible collection, boolean create )
	{
		super( loader, collection, create );
	}

	/**
	 * Initialise the collection
	 *
	 * @throws CollectionException
	 */
	@Override
	public void connect() throws ConnectionException
	{
		ILoaderAieon loader = super.getLoader();
		File file = LocatorAieon.getFileFromURI( loader.getURI() );
		logger.log( Level.FINE, "Initialising source: " + loader.getURI() + ": " + file.exists());
		if( file.exists() ){
			super.setConnected( this.create() );
			return;
		}
		if( !super.isCreateable() )
			throw new ConnectionException( S_ERR_CONNECTING  );


		File parent = file.getParentFile();
		parent.mkdirs();

		//Check again
		if( this.exists())
			return;

		super.setConnected( this.create() );
	}

	/**
	 * Create the collection, and return true if it was successful
	 * @return
	 * @throws ConnectionException
	 */
	public abstract boolean create() throws ConnectionException;

	/**
	 * Prepare the given collection
	 * @param collection
	 * @return
	 * @throws ConnectionException
	 */
	protected boolean prepareCollection( IAccessible collection ) throws ConnectionException
	{
		try {
			collection.prepare( super.getLoader() );
			super.setAccessible( collection );
			return true;
		}
		catch (CollectionException e) {
			throw new ConnectionException( e );
		}
	}

	/**
	 * Returns true if the database exists
	 *
	 * @return boolean
	 */
	@Override
	public boolean exists()
	{
		FileLocator locator = new FileLocator( super.getLoader() );
		return ( locator.getSourceFile().exists() );
	}

	/**
	 * Get a default loader for this collection
	 *
	 * @param applicationKey String
	 * @return FireFoxBookmarkCollection
	 * @throws CollectionException
	 * @throws MalformedURLException 
	 */
	public static ILoaderAieon getDefaultLoader( String identifier, String folder, String reference )
			throws CollectionException, MalformedURLException
	{
		File file = new File( getDefaultSource( folder, reference ));
		if( file.exists() == false )
			throw new CollectionException( Locator.S_COULD_NOT_CREATE_DATABASE_ERR );

		LoaderAieon loader = new LoaderAieon( identifier, getDefaultSource( folder, reference ));
		return loader;
	}

	/**
	 * Fill the loader with the given details
	 * @param loader
	 * @param identifier
	 * @param folder
	 * @param reference
	 * @throws CollectionException
	 */
	public static void fillLoader( ILoaderAieon loader, String identifier, String providerName, String folder, String reference )
	{
		loader.setIdentifier( identifier );
		loader.setProviderName(providerName);
		loader.setURI( getDefaultSource( folder, reference ));
	}

	/**
	 * Get the default location for the personal database
	 * @param folder, the folder that serves as the root for the search
	 * @param reference, the actual file that has to be retrieved
	 * @return String
	 */
	public static URI getDefaultSource( String folder, String reference )
	{
		String ffRoot = System.getProperty( "user.home" ) + folder;
		File root = new File( ffRoot );
		root = findFile( root, reference );
		return root.toURI();
	}

	/**
	 * Find the correct bookmarks file
	 * @param file
	 * @return
	 */
	protected static File findFile( File file, String reference ){
		if( file.getName().equals( reference ))
			return file;
		if( file.isDirectory() == false )
			return null;
		File[] children = file.listFiles();
		File result;
		if(( children == null ) || ( children.length == 0))
			return null;
		for( File child: children ){
			result = findFile( child, reference );
			if( result != null ){
				return result;
			}
		}
		return null;
	}

}
