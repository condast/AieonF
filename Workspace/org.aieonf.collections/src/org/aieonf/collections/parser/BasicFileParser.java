/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.collections.parser;

//J2SE imports
import java.io.*;
import java.util.*;

import org.aieonf.collections.CollectionException;
import org.aieonf.collections.locator.Locator;
import org.aieonf.collections.persistence.IFilePersistence;
import org.aieonf.collections.persistence.LocationManager;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.concept.*;
import org.aieonf.concept.body.BodyFactory;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.ILocationManager;

import java.util.logging.Logger;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * This method maintains all the file related issues of a concept collection,
 * such as determining file name of a concept and so on. It does not include
 * loading and saving issues of the created files
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public abstract class BasicFileParser<T extends IDescribable<?>> extends DefaultCollectionParser<T>
{
  //Strings used by the settings
   public static final String MANIFEST_FILE = "MANIFEST.MF";
  
  //If true, the collection can be created from scratch
  private boolean canCreate;
  
  //Prevents incorrect entries to be logged more than once
  private List<String> incorrectEntries;

  //Get the logger
  private Logger logger;

  /**
   * Create a collection that sorted according to the given manifest. The
   * comparator will only work with get / search operations
   *
   * @param loader LoaderAieon
  */
  public BasicFileParser( boolean canCreate )
  {
    this( null, canCreate );
  }

  /**
   * Create a collection that sorted according to the given manifest. The
   * comparator will only work with get / search operations
   *
   * @param loader LoaderAieon
  */
  public BasicFileParser( IFilePersistence<T> persistence, boolean canCreate )
  {
    super( persistence );
    this.canCreate = canCreate;
		this.incorrectEntries = new ArrayList<String>();
    this.logger = Logger.getLogger( this.getClass().getName() );
  }

	/**
	 * Initialise the parser
	 * @param loader
	 * @throws ConceptException
	 */
	@Override
	public void initialise( ManifestAieon manifest ) throws ParseException
	{
		if( this.canCreate )
			create( manifest );
		super.initialise(manifest);
	}
	
	/**
	 * @return the canCreate
	 */
	public final boolean canCreate()
	{
		return canCreate;
	}

	/**
   * Create the database
   *
   * @param loader LoaderAieon
   * @throws CollectionException
  */
  protected boolean create( ILoaderAieon loader ) throws CollectionException
  {
    File root = new File( loader.getURI().getPath() );
    if( root.exists() == true )
    	return false;
    
    if( root.isDirectory() )
      throw new CollectionException( Locator.S_COULD_NOT_CREATE_DATABASE_ERR +
                                   loader.getName() );
    if( !root.mkdirs())
      throw new CollectionException( Locator.S_COULD_NOT_CREATE_DATABASE_ERR +
          loader.getName() );
    return true;
  }
 
  /**
   * If true, the given entry is correct. Also returns false if the
   * entry is a directory. If includeManifest is true, the entry is also
   * checked for being the manifest file and returns false if it is.
   *
   * @param entry JarEntry
   * @param includeManifest boolean
   * @return boolean
  */
  protected boolean isCorrectEntry( File file, boolean includeManifest )
  {
    if(( file == null ) || ( !file.exists()) )
      return false;

    if( file.getName().equals( MANIFEST_FILE  ))
      return false;
    
    try{
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getManifest() );
      IDescribable<?> descriptor = lm.getDescribable( file.getAbsolutePath() );
    	return descriptor != null;
    }
    catch( Exception ex ){
      logger.info( ex.getMessage() );
    	return false;
    }
  }

  /**
   * Convenience method to return the source of this collection
   * @return
   */
  protected File getSource(){
    return new File( super.getManifest().getURI().getPath() );    
  }

  /**
   * Get all the concepts that pass the filter. Stop after the given amount of
   * results have been received. If negative, ALL the concepts are retrieved
   * that comply with the query. If descriptorOnly is true, then the contents
   * of the descriptor will not be parsed
   * Has been added for improved performance
   *
   * @param filter Filter<IDescriptor>
   * @param descriptorOnly boolean
   * @param amount int
   * @return Collection<IConcept>
   * @throws CollectionException
  */
  @Override
  public void parse( IFilter<? extends IDescribable<?>> filter )
  {
  	Collection<T> results = super.getResults();
  	IPersistence<T> persistence = super.getPersistence();
    persistence.open();
    File source = this.getSource();
    if( this.isCorrectEntry( source, false )){
  		this.parse( source, results, filter );
  		persistence.close();
  		return;  	
    }
    	
    if( !source.isDirectory() ){
      persistence.close();
      return;
    }
    
    for( File file: source.listFiles() ){
    	this.parse( file, results, filter );
    }
    
    persistence.close();
    super.setCollection(results);
  }

  /**
   * Get all the concepts that pass the filter. Stop after the given amount of
   * results have been received. If negative, ALL the concepts are retrieved
   * that comply with the query. If descriptorOnly is true, then the contents
   * of the descriptor will not be parsed
   * Has been added for improved performance
   *
   * @param filter Filter<IDescriptor>
   * @param descriptorOnly boolean
   * @param amount int
   * @return Collection<IConcept>
   * @throws CollectionException
   */
  //@SuppressWarnings("unchecked")
	protected void parse( File file, Collection<T>results, IFilter<? extends IDescribable<?>> filter )
  {
    InputStream entryStream;
    T obj;
    IDescribable<?> descriptor = null;
    boolean accept = false;
    if( this.isCorrectEntry( file, true ) == false )
    	return;
    
  	if( file.isDirectory() ){
  		results.add( this.getDirectoryAieon( file ));
  		for( File child: file.listFiles() )
      	this.parse( child, results, filter );
  		return;
  	}

    try{
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getManifest() );
      descriptor = lm.getDescribable( file.getAbsolutePath() );
      accept = filter.accept( descriptor );
      if( !super.isDescriptorOnly() && ( !accept ))
     		return;

      entryStream = new FileInputStream( file );
    	IFilePersistence<T> persistence = (IFilePersistence<T>) super.getPersistence();
      persistence.setFile(file);
      obj = persistence.read( entryStream );
      entryStream.close();
      super.notifyParsed( obj );
      if( !super.isDescriptorOnly() && ( filter.accept( obj ) == false ))
      	return;
      
      results.add( obj );
      int amount = filter.getAmount();
      if(( amount >= 0 ) && ( results.size() >= amount ))
        return;
      if( file.isFile() )
      	return;
      
      for( File child: file.listFiles() )
      	this.parse(child, results, filter);

    }
    catch( Exception ex ){
    	ex.printStackTrace();
     	this.incorrectEntries.add( file.getName() );
    }
  }

	/**
	 * Get a representation for the directory
	 * @param directory
	 * @return
	 */
	protected abstract T getDirectoryAieon( File directory );
	
  /**
   * Get the list of incorrect entries
   * @return
   */
  public Collection<String> getIncorrectEntries(){
  	return this.incorrectEntries;
  }

  /**
   * Set the basics for a concept for this file database
   *
   * @param concept IConcept
   * @throws CollectionException
  */
  protected void setConcept( IConcept concept ) throws CollectionException
  {
    try{
    	concept.set( IConcept.Attributes.SOURCE.name(), this.getManifest().getID() );
    	concept.setVersion( 1 );
      BodyFactory.sign( this.getManifest(), concept );
    }
    catch( Exception ex ){
      throw new CollectionException( ex );
    }
  }
}