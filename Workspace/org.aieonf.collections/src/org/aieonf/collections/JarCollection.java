/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast </p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.collections;

//J2SE imports
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.jar.*;

import org.aieonf.collections.parser.ICollectionParser;
import org.aieonf.collections.persistence.ConceptPersistence;
import org.aieonf.collections.persistence.EncryptedStream;
import org.aieonf.collections.persistence.LocationManager;
import org.aieonf.concept.*;
import org.aieonf.concept.body.*;
import org.aieonf.concept.core.*;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.persist.ILocatedPersistence;
import org.aieonf.util.filter.*;
import org.aieonf.util.io.IOUtils;
import org.aieonf.util.logger.*;
import org.aieonf.util.persistence.IPersistence;
//various third party

//condast imports

//concept imports

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
public class JarCollection<T extends IDescribable<?>> extends AbstractDescribableCollection<T>
{
	//The error messages
	public static final String S_ERR_JAR_WRITE_EXCEPTION =
			"Could not write to jar: ";
	public static final String S_ERR_COLLECTION_ACCESS_DENIED_EXCEPTION = "Access is debnied"; 
	public static final String S_ERR_INCORRECT_RESULTSET =
			"Invalid result set for the given request";
	public static final String S_ERR_ADD_DOCUMENT_BUFFER = "Could not add the concept to the document buffer";
	public static final String S_ERR_COULD_NOT_UPDATE_CONCEPT_NOT_EXISTS = 
			"Could not update the concept, because it does not excist: ";
	public static final String S_ERR_SECURITY_FILE_OPEN_EXCEPTION =
			"Failed to open collection as the file has not been registered yet: ";
	public static final String S_ERR_SECURITY_FILE_REGISTER_EXCEPTION =
			"Failed to register the collection as it already exists: ";
	public static final String S_ERR_SECURITY_FILE_OPEN_MANIFEST_EXCEPTION =
			"Failed to open as the manifest could not be opened: ";
	public static final String S_ERR_SECURITY_LOADER_EXCEPTION =
			"Failed to open the collection as the loader does not match the manifest: ";
	public static final String S_ERR_TRANSLATION_FAILURE =
			"The names in the collection could not be translated correctly. The encryption may not be correct";

	//If the external jar file is not null, the jar collection is open
	private JarFile externalJarFile;

	private Logger logger;

	/**
	 * Initialise and create this class
	 * 
	 * @param lm ILoaderManager
	 * @param persist IPersistence
	 * @throws CollectionException
	 */
	public JarCollection( ICollectionParser<T> parser )
			throws CollectionException
			{
		super( parser );
		this.logger = Logger.getLogger( this.getClass() );
			}

	/**
	 * Create the database based on the info of the loader
	 *
	 * @param locator LocatorAieon
	 * @throws CollectionException
	 */
	@Override
	protected boolean create( ILoaderAieon loader ) throws CollectionException
	{
		//fill the file with a dummy jar entry
		File file = new File( loader.getURI() );
		if( file.exists() )
			return false;

		file.getParentFile().mkdirs();
		ManifestAieon manifest = new ManifestAieon( loader ); 
		try {
			manifest.fill( loader );
			manifest.set( IDescriptor.Attributes.ID.name(), BodyFactory.IDFactory());
			manifest.sign();
		}
		catch (Exception e) {
			throw new CollectionException( e );
		}
		this.writeManifest( manifest, file );
		return true;
	}

	/**
	 * Write the model to the database. Depending on the replace options,
	 * an existing model may be overwritten, or conversely throw an exception
	 *
	 * @param manifest ManifestAieon
	 * @param pst IPersistence<IConcept>
	 * @param replace boolean
	 * @throws CollectionException
	 */
	protected void writeManifest( ManifestAieon manifest, File file )
			throws CollectionException
			{
		String location = ManifestAieon.LOCATION;
		JarOutputStream jarStream = null;
		OutputStream eout = null;
		try{
			jarStream = new JarOutputStream( new FileOutputStream( file ));
			eout = EncryptedStream.getEncryptedOutputStream(manifest, jarStream);

			JarEntry entry = new JarEntry( location );
			jarStream.putNextEntry( entry );
			ConceptPersistence persist = new ConceptPersistence();
			jarStream.write( persist.toBytes(manifest, manifest ));
			jarStream.closeEntry();
		}
		catch( Exception e ){
			e.printStackTrace();
			throw new CollectionException( e );
		}
		finally {
			IOUtils.closeOutputStream( eout );
			IOUtils.closeOutputStream( jarStream );
		}
			}

	/**
	 * Load the manifest aieon that is stored in the collection. 
	 * Throws an exception if it does not exist.
	 *
	 * @return ManifestAieon
	 * @throws CollectionException
	 * @throws URISyntaxException 
	 */
	@Override
	protected ManifestAieon searchManifest( ILoaderAieon loader ) throws CollectionException
	{
		JarEntry entry = null;
		JarFile jar = null;
		File file = new File( loader.getURI() );
		if( !file.exists() )
			return null;

		try{
			jar = new JarFile( new File( loader.getURI() ));
			this.externalJarFile = jar;

			InputStream entryStream = null;
			InputStream ein = null;
			IConcept concept;

			IDescribable<?> descriptor = null;
			for( Enumeration<JarEntry> list = jar.entries(); list.hasMoreElements(); ){
				entry = list.nextElement();
				if( entry.getName().equals( ManifestAieon.LOCATION ) == false )
					continue;

				try{
					descriptor = new Descriptor( ManifestAieon.MANIFEST );
					String name = descriptor.getDescriptor().getName();
					this.logger.trace( "found entry: " + entry.getName() + " = name " + name );
					if( name == null )
						throw new NullPointerException( S_ERR_TRANSLATION_FAILURE );
					this.logger.trace( "found name: " + name );

					entryStream = jar.getInputStream( entry );
					ein = EncryptedStream.getEncryptedInputStream(loader, entryStream );
					IPersistence<IConcept> persist = new ConceptPersistence();
					persist.open();
					concept = persist.read( ein );
					persist.close();

					this.logger.info( "concept found: " + concept.toString() );
					ConceptBody<IConcept> body = new ConceptBody<IConcept>( concept );
					IConcept transform = body.transform();
					ManifestAieon manifest = new ManifestAieon( transform );
					manifest.fill(transform);
					return manifest;
				}
				catch( Exception ex ){
					ex.printStackTrace();
					this.addIncorrectentry( entry.getName() );
					this.logger.error( "Incorrect entry " + descriptor.toString() +
							":" + ex.getMessage() );
				}
				finally{
					IOUtils.closeInputStream(ein);
				}
			}
			throw new CollectionException( S_ERR_MANIFEST_NOT_FOUND );
		}
		catch( IOException ex ){
			throw new CollectionException( ex.getMessage(), ex );
		}
		finally{
			IOUtils.closeJarFile( jar );
			this.externalJarFile = null;
		}
	}


	/**
	 * Returns true if the mainfies exists in the collection
	 *
	 * @return boolean
	 * @throws CollectionException
	 */
	@Override
	protected boolean containsManifest( ILoaderAieon loader ) throws CollectionException
	{
		JarEntry entry = null;

		JarFile jar = null;
		File file = new File(loader.getURI() );
		if( !file.exists() )
			return false;

		try{
			jar = new JarFile( file );
			this.externalJarFile = jar;
			this.logger.trace( "Getting manifest: " );
			for( Enumeration<JarEntry> list = jar.entries(); list.hasMoreElements(); ){
				try{
					entry = list.nextElement();
					if(( entry == null ) || ( entry.getSize() == 0 ))
						continue;
					if( entry.getName().equals( ManifestAieon.LOCATION ))
						return true;
				}
				catch( Exception ex ){
					throw new CollectionException( ex.getMessage(), ex );
				}
			}
		}
		catch( Exception ex ){
			throw new CollectionException( ex );
		}
		finally{
			this.externalJarFile = null;
			IOUtils.closeJarFile(jar);
		}
		return false;
	}

	/**
	 * Prepare to access the collection
	 *
	 * @throws CollectionException
	 */
	@Override
	public boolean access()
	{
		JarFile jar = null;
		if( super.getLoader() != null )
			if( this.externalJarFile != null ){
				this.leave();
				return false;
			}	
		File source = new File( super.getLoader().getURI());
		try{
			jar = new JarFile( source );
			this.externalJarFile = jar;
			return super.access();
		}
		catch( Exception ex ){
			this.logger.error( ex.getMessage() + ": " + source, ex );
			return false;
		}
	}

	@Override
	protected boolean access(ILoaderAieon loader)
	{
		if( super.getLoader() == null )
			return false;
		File source = new File(  loader.getURI());
		if( !source.exists() )
			return false;
		try {
			JarFile jar = new JarFile( source );
			this.externalJarFile = jar;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Close access to the collection
	 */
	@Override
	public void leave()
	{
		try{
			JarFile jar = this.externalJarFile;
			if( jar == null ){
				return;
			}

			this.externalJarFile = null;
			jar.close();
		}
		catch( IOException ioex ){
			this.logger.error( ioex.getMessage(), ioex );
		}
		finally{
			super.leave();
		}
	}

	/**
	 * Remove the given file and return the enumeration of jar entries
	 *
	 * @param entryNames Collection<String>
	 * @throws CollectionException
	 */
	protected void remove( Collection<String> entryNames ) throws CollectionException
	{
		if( entryNames == null )
			throw new NullPointerException();

		if( entryNames.isEmpty())
			return;

		//byte[] bytes = this.toBytes( entryNames, ReplaceOptions.IGNORENOTEXIST );
		//ByteArrayInputStream inStream = new ByteArrayInputStream( bytes );
		//this.fromBuffer( inStream );
		/*
    try{
      inStream.close();
    }
    catch( IOException ex ){
      throw new DatabaseException( ex );
    }
		 */
	}

	/**
	 * Write the list to the database. Depending on the replace options,
	 * an existing model may be overwritten, or conversely throw an exception
	 *
	 * @param list Collection<T>
	 * @param replace ReplaceOptions
	 * @return Collection<IDescriptor>
	 * @throws CollectionException
	 */
	@Override
	protected void write( Collection<? extends T> list )
			throws CollectionException
			{
		if(( list == null ) || ( list.isEmpty() ))
			return;

		FileOutputStream fileStream = null;
		JarOutputStream jarStream = null;

		try{
			File file = new File( super.getLoader().getURI() );
			fileStream = new FileOutputStream( file );
			jarStream = new JarOutputStream( fileStream );

			JarEntry entry;

			LocationManager lm = new LocationManager( super.getLoader() );
			entry = new JarEntry( ManifestAieon.LOCATION );
			jarStream.putNextEntry( entry );
			ConceptPersistence cp = new ConceptPersistence();
			jarStream.write( cp.toBytes(super.getLoader(), super.getLoader() ));
			jarStream.closeEntry();

			ILocatedPersistence<T> persist = ( ILocatedPersistence<T> )super.getParser().getPersistence();
			persist.open();
			for( T concept: list ){
				entry = new JarEntry( lm.locate( concept ));
				jarStream.putNextEntry( entry );
				jarStream.write( persist.toBytes(super.getLoader(), concept ));
				jarStream.closeEntry();
			}
			persist.close();   
		}
		catch( Exception e ){
			throw new CollectionException( e );
		}
		finally {
			IOUtils.closeOutputStream( jarStream );
		}
			}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#size()
	 */
	@Override
	public int size()
	{
		try{
			JarFile jar = this.externalJarFile;
			int size = jar.size();
			return size;
		}
		catch( Exception e ){
			return -1;
		}
	}

	@Override
	public Collection<T> remove(IDescriptor descriptor) throws CollectionException
	{
		IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
		Collection<T> concepts = this.search(filter, false);
		Collection<T> results = new ArrayList<T>();
		for( T concept: concepts ){
			if( concept.equals( descriptor )){
				concepts.remove( descriptor );
				results.add(concept);
			}
		}
		return results;
	}
}