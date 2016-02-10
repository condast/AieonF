package org.aieonf.collections.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.aieonf.collections.persistence.LocationManager;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.ILocationManager;
import org.aieonf.util.parser.ParseException;
import org.aieonf.util.persistence.IPersistence;

public abstract class AbstractJarParser<T extends IDescribable<?>> extends DefaultCollectionParser<T>
{
  public static final String S_ERR_TRANSLATION_FAILURE =
      "The names in the collection could not be translated correctly. The encryption may not be correct";

  private ILoaderAieon loader;
  
  //If the external jar file is not null, the jar collection is open
  private JarFile externalJarFile;

  //Prevents incorrect entries to be logged more than once
  private Collection<String> incorrectEntries;

  private Logger logger;
	
  public AbstractJarParser( IPersistence<T> persistence, ILoaderAieon loader )
	{
		super( persistence );
  	this.loader = loader;
		this.incorrectEntries = new ArrayList<String>();
		logger = Logger.getLogger( this.getClass().getName() );
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.parser.DefaultCollectionParser#open()
	 */
	@Override
	public void open() throws ParseException
	{
    try{
    	this.externalJarFile = new JarFile( new File( loader.getURI() ));
    	super.open();
    }
    catch( IOException ex ){
    	
    }
	}

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.parser.DefaultCollectionParser#close()
	 */
	@Override
	public void close() throws ParseException
	{
		try {
			if( this.externalJarFile == null ){
				super.close();
				return;
			}
			this.externalJarFile.close();
			super.close();
		}
		catch (IOException e) {
			throw new ParseException( e );
		}
		this.externalJarFile = null;
	}

	/**
	 * @return the externalJarFile
	 */
	protected final JarFile getExternalJarFile()
	{
		return externalJarFile;
	}

	public void addIncorrectentry( String entry ){
		this.incorrectEntries.add( entry );
    this.logger.severe( "Incorrect entry: " + entry );
	}
	
	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	@Override
	public boolean canParseFromDescriptor( String key ){
		if( IDescriptor.Attributes.NAME.name().equals(key))
			return true;
		if( IDescriptor.Attributes.ID.name().equals(key))
			return true;
		return IDescriptor.Attributes.VERSION.name().equals(key);
	}
  
  /**
   * Get a correct describable entry, or null if it wasn't found, the entry is a directory, or otherwise
   * If includeManifest is true, the entry the manifest file ais also checked.
   *
   * @param entry JarEntry
   * @param includeManifest boolean
   * @return boolean
   */
  protected IDescribable<?> getEntry( JarEntry entry, boolean includeManifest ) throws ParseException
  {
    if(( entry == null ) || ( entry.getSize() == 0 ))
      return null;
    if( entry.isDirectory() )
    	return null;

    if( !includeManifest && entry.getName().equals(ManifestAieon.LOCATION ))
    	return null;
    this.logger.fine( "Checking jar entry: " + entry.getName() );
    try{
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getManifest() );
      IDescribable<?> obj = lm.getDescribable( entry.getName() );
      if( !obj.getDescriptor().getName().equals( ManifestAieon.MANIFEST  ))
        return obj;
      if( includeManifest )
      	return obj;
      return null;      
    }
    catch( Exception ex ){
      throw new ParseException( ex );
    }
  }
  
  /**
   * Get the list of incorrect entries
   * @return
   */
  public Collection<String> getIncorrectEntries(){
  	return this.incorrectEntries;
  }
}
