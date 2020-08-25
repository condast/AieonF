/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast </p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.collections;

import java.io.*;
import java.net.URI;
import java.util.*;

import org.aieonf.collections.parser.ICollectionParser;
import org.aieonf.commons.encryption.IEncryption;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.security.AieonFEncryption;

/**
 *
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * This method maintains all the general settings of this program
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
abstract class AbstractChangeableCollection<T extends IDescribable> implements IAccessible
{
	//Prevents incorrect entries to be logged more than once
	private List<String> incorrectEntries;

	private ICollectionParser<T> parser;

	/**
	 * Initialise and create this class
	 * 
	 * @param lm ILoaderManager
	 * @param persist IPersistence
	 */
	protected AbstractChangeableCollection( ICollectionParser<T> parser )
	{
		this.parser = parser;
		this.incorrectEntries = new ArrayList<String>();
	}

	/**
	 * Create the database based on the info of the loader. Returns true if the creation was successful
	 *
	 * @param locator LocatorAieon
	 * @throws CollectionException
	 */
	protected abstract boolean create( ILoaderAieon loader ) throws CollectionException;

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	public boolean canParseFromDescriptor( String key ){
		return this.parser.canParseFromDescriptor(key);
	}


	/**
	 * @return the parser
	 */
	protected final ICollectionParser<T> getParser()
	{
		return parser;
	}

	/**
	 * @param parser the parser to set
	 */
	protected final void setParser(ICollectionParser<T> parser)
	{
		this.parser = parser;
	}

	/**
	 * Set the manifest aieon
	 *
	 * @param manager LoaderManager
	 * @throws ConceptDatabaseException
	 */
	@Override
	public void prepare( ILoaderAieon loader ) throws CollectionException
	{
		ManifestAieon manifest = null;
		try{
			//Rules if the collection does not contain an internal manifest file
			if( this.containsManifest( loader ) == false ){        
				if( !loader.getStoreInternal() ){
					loader.set( IDescriptor.Attributes.NAME.toString(), ManifestAieon.MANIFEST );
					manifest = new ManifestAieon( loader );
					manifest.fill(loader);
					IEncryption encryption = new AieonFEncryption( manifest );
					String signature = encryption.encryptData( manifest.getURI().getPath() );
					loader.set( IDescriptor.Attributes.ID.toString(), signature.substring(0, 10 ));
					this.parser.initialise( manifest );       	
					return;
				}else{
					if( loader.isCreatable() )
						this.create(loader);
				}
			}

			//An internal manifest file was found
			this.access( loader );
			manifest = this.searchManifest( loader );
			this.parser.initialise( manifest );
		}
		catch( Exception ex ){
			throw new CollectionException( ex );
		}
		finally{
			URI source = loader.getURI();
			if( manifest == null ){
				if( loader.getStoreInternal() )
					throw new CollectionException( S_ERR_NO_MANIFEST_STORED_INTERNALLY );
				if( !loader.isCreatable() )
					throw new CollectionException( S_ERR_NO_MANIFEST_CREATED );
			}
			if( manifest.test( loader ) == false )
				throw new CollectionException( S_ERR_SECURITY_LOADER_EXCEPTION + source );
			this.leave();
		}
	}

	/**
	 * Prepare to access the collection
	 *
	 * @throws CollectionException
	 */
	@Override
	public boolean access()
	{
		if( !canAccess() )
			return false;
		return this.access( this.parser.getManifest() );
	}

	/**
	 * Access the collection
	 */
	protected abstract boolean access( ILoaderAieon loader );

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IAccessable#canAccess()
	 */
	@Override
	public boolean canAccess()
	{
		return ( this.parser.getManifest() != null );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IAccessable#leave()
	 */
	@Override
	public void leave()
	{
	}

	/**
	 * Search the manifest aieon
	 * @param loader
	 * @return
	 * @throws CollectionException
	 */
	protected abstract ManifestAieon searchManifest( ILoaderAieon loader ) throws CollectionException;

	/**
	 * Returns true if the manifest exists in the collection
	 *
	 * @return boolean
	 * @throws CollectionException
	 */
	protected abstract boolean containsManifest( ILoaderAieon loader ) throws CollectionException;

	/**
	 * Update the manifest aieon.
	 *
	 * @param manifest ManifestAieon
	 * @throws CollectionException
	 */
	@Override
	public void updateManifest( ManifestAieon mani )
			throws CollectionException
			{
		if( this.parser.getManifest() == null )
			throw new SecurityException( S_ERR_UPDATE_MANIFEST_NOT_FOUND + mani.toString() );
		try{
			Descriptor.setUpdateDate( mani,
					Calendar.getInstance().getTime() );
			this.parser.initialise( mani );
		}
		catch( Exception ce ){
			throw new CollectionException( mani.getIdentifier(),
					ce.getMessage(), ce );
		}
			}

	/**
	 * Get the manifest aieon
	 * @return
	 */
	@Override
	public ManifestAieon getLoader()
	{
		return this.parser.getManifest();
	}

	/**
	 * Clears the list of incorrect entries
	 */
	protected void clearIncorrectEntries()
	{
		this.incorrectEntries.clear();
	}

	/**
	 * returns true if the given entry is incorrect
	 * @param entry
	 * @return
	 */
	protected boolean isIncorrectEntry( String entry )
	{
		if( this.incorrectEntries == null )
			return false;
		return this.incorrectEntries.contains( entry );
	}

	/**
	 * Add an incorrect entry. returns true if the entry is added
	 * @param entry
	 * @return
	 */
	public boolean addIncorrectentry( String entry )
	{
		if((( incorrectEntries == null ) ||
				( incorrectEntries.contains( entry ))))
			return false;
		incorrectEntries.add( entry );
		return true;
	}

	/**
	 * Returns true if the descriptor is acceptable for storage
	 * @param descriptor
	 * @return
	 */
	public boolean accept( IDescribable changeable ){
		if( changeable == null )
			return false;
		return Descriptor.isValid( changeable.getDescriptor() );
	}


	/**
	 * Parse a descriptor from an entry name. By convention the name is
	 * <name>:<id>:<version>, where the id itself may contain ':' as well.
	 * The version should be a valid int.
	 * This method should NOT replace descriptor in the database, as the
	 * name in an entry is usually in lowercase.
	 *
	 * @param entryName String
	 * @return IDescriptor
	 * @throws ConceptException
	 */
	public static IDescriptor parseDescriptor( String entryName )
			throws ConceptException
			{
		String[]split = entryName.split("[:]");
		if( split.length < 3 )
			throw new ConceptException( S_ERR_INVALID_ENTRYNAME + entryName);
		try{
			int version = Integer.parseInt( split [ split.length - 1] );
			int endIndex = entryName.length() - split[ 2 ].length() - 1;
			String id = entryName.substring( split[ 0 ].length() + 1, endIndex );
			IDescriptor descriptor = new Descriptor( id, split[0] );
			descriptor.setVersion( version );
			return descriptor;
		}
		catch( NumberFormatException nex ){
			throw new ConceptException( nex.getMessage() + ": " + entryName, nex );
		}
			}

	/**
	 * Remove the file extension
	 * concepts with the given name
	 *
	 * @param position String
	 * @return String
	 */
	protected static String removeExtension( String position )
	{
		File file = new File( position );

		String regexp = new String( "[\\.]");
		String[] split = file.getName().split( regexp );
		int endIndex = file.getName().length() - split[ split.length - 1].length() - 1;
		return file.getName().substring( 0, endIndex );
	}

	/**
	 * Returns true if the given list is empty
	 * @param descriptors
	 * @return
	 */
	public static boolean isEmpty( List<? extends IDescriptor> descriptors )
	{
		return (( descriptors == null) ||  ( descriptors.size() == 0 ));
	}

	/**
	 * Get the first concept in the given enumeration, or null if none were found
	 *
	 * @param concepts Collection
	 * @return IConcept
	 */
	public static IConcept getFirst( Collection<? extends IConcept> concepts )
	{
		Iterator<? extends IConcept> iterator = concepts.iterator();
		if( iterator.hasNext() == false )
			return null;
		return iterator.next();
	}

	/**
	 * Normalise the strings to lowercase and trimmed
	 * @param names List
	 * @return List
	 */
	public static List<String>normalise( List<String>names )
	{
		List<String>normalised = new ArrayList<String>();
		for( String name: names ){
			normalised.add( name.trim().toLowerCase());
		}
		return normalised;
	}

	/**
	 * Removes the version from the entry name. Throws an exception if
	 * the structure is incorrect
	 * @param name String
	 * @return String[]
	 * @throws Exception
	 */
	public static String[] removeVersion( String name )
			throws Exception
			{
		String[] split = name.split( "_" );
		String version = split[ split.length - 1 ];
		if( split.length < 2 )
			throw new Exception( S_ERR_INVALID_ENTRYNAME );
		split = version.split( "[.]" );
		if( split.length < 2 )
			throw new Exception( S_ERR_INVALID_ENTRYNAME );
		int vers = Integer.parseInt( split[ 0 ] );
		String[] result = new String[ 2 ];
		result[ 0 ] = name.substring( 0, name.length() - version.length() );
		result[ 1 ] = String.valueOf( vers );
		return result;
			}
}