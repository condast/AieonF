/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.collections;

//J2SE imports
import java.io.*;
import java.util.*;

import org.aieonf.collections.locator.Locator;
import org.aieonf.collections.parser.ICollectionParser;
import org.aieonf.collections.persistence.LocationManager;
import org.aieonf.commons.filter.AbstractFilter;
import org.aieonf.commons.filter.IFilter;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.persistence.IPersistence;
import org.aieonf.concept.*;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.ILocationManager;

import java.util.logging.Level;
import java.util.logging.Logger;



//Concept

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
public class BasicFileCollection<T extends IDescribable<?>> extends AbstractChangeableCollection<T> implements IDescribableCollection<T>
{
  //Strings used by the settings
  public static final String MANIFEST_DIR = "META-INF";
  public static final String MANIFEST_FILE = "MANIFEST.MF";
  
  //If true, the collection can be created from scratch
  private boolean canCreate;
  
  //Get the logger
  private Logger logger;

  /**
   * Create a collection that sorted according to the given manifest. The
   * comparator will only work with get / search operations
   *
   * @param loader LoaderAieon
  */
  public BasicFileCollection( ILoaderAieon loader, ICollectionParser<T> parser )
  {
    this( loader, parser, false );
  }

  /**
   * Create a collection that sorted according to the given manifest. The
   * comparator will only work with get / search operations
   *
   * @param loader LoaderAieon
  */
  public BasicFileCollection( ILoaderAieon loader, ICollectionParser<T> parser, boolean canCreate )
  {
    super( parser );
    this.canCreate = canCreate;
    this.logger = Logger.getLogger( this.getClass().getName() );
  	loader.verify();
  }

  
  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.AbstractChangeableCollection#prepare(org.condast.concept.loader.ILoaderAieon)
	 */
	@Override
	public void prepare(ILoaderAieon loader) throws CollectionException
	{
		if( this.canCreate )
			create( loader );
		super.prepare(loader);
	}

	/**
   * Create the database
   *
   * @param loader LoaderAieon
   * @throws CollectionException
  */
  @Override
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
 
	@Override
	protected ManifestAieon searchManifest(ILoaderAieon loader)
			throws CollectionException
	{
		File file = new File( loader.getURI().getPath() );
  	File manifestDir = new File( file, MANIFEST_DIR );
  	if( !manifestDir.exists() )
  		throw new CollectionException( S_ERR_MANIFEST_NOT_FOUND );
  	File manifestFile = new File( manifestDir, MANIFEST_FILE );
    if( !manifestFile.exists() )
      throw new CollectionException( S_ERR_MANIFEST_NOT_FOUND );

    IPersistence<T> persist = super.getParser().getPersistence();
    InputStream in = null;
    ManifestAieon manifest = null;
    try{
      in = new BufferedInputStream( new FileInputStream( manifestFile ));
      persist.open();
    	manifest = new ManifestAieon( persist.read( in ).getDescriptor());
      persist.close();
      return manifest;
    }
    catch( Exception ex ){
    	throw new CollectionException( ex.getMessage(), ex );
    }
    finally{
    	if( in != null ){
    		try{
    			in.close();
    		}
    		catch( IOException ex ){
    			throw new CollectionException( ex.getMessage(), ex );
    		}
    	}
    }
  }

  /**
   * Convenience method to return the source of this collection
   * @return
   */
  protected File getSource(){
    return new File( super.getLoader().getURI().getPath() );    
  }
  
  /**
   * Returns true if the manifest exists in the collection
   *
   * @return boolean
   * @throws ConceptDatabaseException
  */
  @Override
	protected boolean containsManifest( ILoaderAieon loader ) throws CollectionException
  {
  	File file = new File( loader.getURI().getPath() );
  	File manifestDir = new File( file, MANIFEST_DIR );
  	if( !manifestDir.exists() )
  		return false;
  	File manifest = new File( manifestDir, MANIFEST_FILE );
    return manifest.exists();
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
    if(( file == null ) || ( !file.exists()) || ( file.isFile() == false ))
      return false;

    if( file.getName().equals( MANIFEST_FILE  ))
      return false;
    
    try{
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getLoader() );
      IDescribable<?> descriptor = lm.getDescribable( file.getAbsolutePath() );
    	return descriptor != null;
    }
    catch( Exception ex ){
      logger.info( ex.getMessage() );
    	return false;
    }
  }

  /**
   * This convenience method can be implemented when the descriptor can be derived from the file info.
   * This allows a faster search on name, id and version. The search algorithm will first parse the header,
   * and then the body ONLY if this method returns zero or false;
   * @param file
   * @return
   * @throws CollectionException
   */
  protected IDescriptor getDescriptorFromHeader( File file ) throws CollectionException{
  	return null;
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
  public Collection<T> search( IFilter<T> filter, boolean descriptorOnly )
  {
  	this.access();
  	Set<T> results = new TreeSet<T>();

    IPersistence<T> persist = super.getParser().getPersistence();
    persist.open();
    File source = this.getSource();
    if( this.isCorrectEntry( source, false )){
  		this.search( source, results, filter, false );
  		persist.close();
  		this.leave();
  		return results;
    	
    }
    	
    if( !source.isDirectory() ){
      persist.close();
    	this.leave();
      return results;
    }
    
    for( File file: source.listFiles() ){
    	if( this.isCorrectEntry( file, true ) == false ){
    		this.search( file, results, filter, false );
    		continue;
    	}
    	this.search(file, results, filter, false );
    }
    
    persist.close();
    this.leave();
    return results;
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
	protected void search( File file, Collection<T>results, IFilter<T> filter, boolean descriptorOnly )
  {
    InputStream entryStream;
    T obj;
    IPersistence<T> persist = super.getParser().getPersistence();
    IDescribable<?> descriptor = null;
    boolean accept = false;
    if( this.isCorrectEntry( file, true ) == false ){
    	if( file.isDirectory() ){
    		for( File child: file.listFiles() )
        	this.search( child, results, filter, descriptorOnly );
    	}
    	return;
    }
    
    try{
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getLoader() );
      descriptor = lm.getDescribable( file.getAbsolutePath() );
      accept = filter.accept( descriptor );
      if(( descriptorOnly ) && ( !accept ))
     		return;

      entryStream = new FileInputStream( file );
      obj = persist.read( entryStream );
      entryStream.close();
      if(( descriptorOnly == false ) && ( filter.accept( obj ) == false ))
      	return;
      
      results.add( obj );
      int amount = filter.getAmount();
      if(( amount >= 0 ) && ( results.size() >= amount ))
        return;

    }
    catch( Exception ex ){
    	ex.printStackTrace();
     	this.addIncorrectentry( file.getName() );
    }
  }

  /**
   * Return the descriptors of the entries contained in the collection.
   * Throws an exception if the collection closed
   * @return List<String>
   * @throws CollectionException
  */
  @Override
  public Collection<IDescriptor> getDescriptors() throws CollectionException
  {
  	List<IDescriptor> results = new ArrayList<IDescriptor>();
  	this.access();

  	IPersistence<T> persist = super.getParser().getPersistence();
  	persist.open();

  	File source = new File( super.getLoader().getURI().getPath() );
  	if( source.isFile() ){
  		this.getDescriptors( source, results );
  		return results;
  	}
  	if( !source.isDirectory() )
  		return results;

  	for( File entry: source.listFiles() )
  		this.getDescriptors( entry, results );

  	persist.close();
  	this.leave();
  	return results;   
  }

  /**
   * Get the data corresponding to the given entries
   *
   * @param file File
   * @return Collection<T> results
   * @throws CollectionException
  */
  protected void getDescriptors( File file, Collection<IDescriptor> results ) 
  throws CollectionException
  {
    InputStream entryStream;
    T obj;
    IPersistence<T> persist = super.getParser().getPersistence();
    if( this.isCorrectEntry( file, true ) == false ){
    	if( file.isDirectory() ){
    		for( File child: file.listFiles() )
        	this.getDescriptors( child, results );
    	}
    	return;
    }
    
    try{
      entryStream = new FileInputStream( file );
      obj = persist.read( entryStream );
      entryStream.close();
      results.add( obj.getDescriptor() );
    }
    catch( Exception ex ){
    	ex.printStackTrace();
     	this.addIncorrectentry( file.getName() );
    }
  }

  /**
   * Returns true if the model with the given descriptor exists in the database
   *
   * @param descriptor IDescriptor
   * @return boolean
  */
  @Override
  public boolean contains( Object obj )
  {
  	if(!( obj instanceof IDescribable ))
  		return false;
  	
   	this.access();
    boolean found = false;
    File source = new File( super.getLoader().getURI().getPath() );    
    if( this.isCorrectEntry( source, false ))
    	return this.contains( source, obj );
    
    for( File entry: source.listFiles() ){
      if( this.contains( entry, obj ))
         return true;
    }

    this.leave();
    return found;
  }

  /**
   * Returns true if the model with the given descriptor exists in the database
   *
   * @param descriptor IDescriptor
   * @return boolean
   * @throws ConceptDatabaseException
  */
  protected boolean contains( File file, Object descriptor )
  {
    if( file.isDirectory() ){
    	for( File child: file.listFiles() )
    		if( this.contains( child, descriptor ))
    			return true;
    	return false;
    }
    
    if( !this.isCorrectEntry( file, true ))
        return false;;

    try{
      
    	IDescriptor check = this.getDescriptorFromHeader(file);
    	return (( check != null ) && ( check.equals( descriptor )));  	
    }
    catch( Exception ex ){
       this.logger.log( Level.SEVERE, ex.getMessage(), ex );
    }
    return false;
  }

  /**
   * Returns the subset of all the given descriptors that are 
   * contained in this collection
   * @param descriptors Collection<? extends IDescriptor>
   * @return Collection<IDescriptor>
  */
  @Override
  public boolean containsAll( Collection<?> descriptors )
  {
    if( descriptors == null )
    	throw new NullPointerException();

    if( descriptors.isEmpty() )
    	return true;
    
  	this.access();
  	
  	Collection<?> temp = new ArrayList<Object>( descriptors );
    File source = new File( super.getLoader().getURI().getPath() );    
    Object found;
    if(( !source.isDirectory() ) && ( descriptors.size() == 1 )){
    	found = descriptors.iterator().next();
    	if( !this.contains( source, found )){
    		temp.remove( found );
    		if( temp.isEmpty() )
    			return true;	
    	}
    }
    
    for( File entry: source.listFiles() ){
      for( Object descriptor: descriptors ){
      	if( !this.contains( entry, descriptor ))
      		continue;

      	temp.remove( descriptor );
      	if( temp.isEmpty() )
      		return true;	
      }
    }
    this.leave();
    return false;  	
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
	protected void writeManifest( ManifestAieon manifest )
    throws CollectionException
  {
    File source = new File( super.getLoader().getURI().getPath() );    
    File manifestDir = new File( source, MANIFEST_DIR );
    if( !manifestDir.exists())
    	manifestDir.mkdirs();
    File manifestFile = new File( manifestDir, MANIFEST_FILE );
    OutputStream out = null;
    @SuppressWarnings("unchecked")
		IPersistence<IConcept> persist = (IPersistence<IConcept>) super.getParser().getPersistence();
    try{
      out = new BufferedOutputStream( new FileOutputStream( manifestFile ));
      persist.open();
    	persist.write( manifest, out );
      persist.close();
    }
    catch( IOException ex ){
    	throw new CollectionException( ex.getMessage(), ex );
    }
    finally{
    	if( out == null )
    		return;
    	try{
    		out.flush();
    		out.close();
    	}
    	catch( IOException ex ){
    		throw new CollectionException( ex.getMessage(), ex );
    	}
    }
  }

  /**
   * Returns true if the given file can be written
   * @param file
   * @param options
   * @return
   */
  protected boolean checkWrite( File file, ReplaceOptions options ){
  	switch( options){
  		case FALSE:
  			return !file.exists();
  		case IGNORENOTEXIST:
  			return true;
  		default:
  			return file.exists();
  	}
  }
  
  /**
   * Write the model to the database. Depending on the replace options,
   * an existing model may be overwritten, or conversely throw an exception
   *
   * @param model IConcept concept
   * @param replace boolean
   * @throws CollectionException
  */
  protected void write( T obj, ReplaceOptions replace )
    throws CollectionException
  {
    if(( obj == null ) || ( !this.accept( obj )))
    	throw new CollectionException( S_ERR_INVALID_ENTRY + obj.getDescriptor() );

    OutputStream fileStream = null;
    OutputStream outStream = null;
    File file = null;
    try{
      //Send the document to the jar file.
      File source = new File( super.getLoader().getURI().getPath() );    
      logger.fine("Writing descriptor " + obj.getDescriptor().toString() );
      ILocationManager<? extends IDescribable<?>> lm = new LocationManager( super.getLoader() );
      file = new File( source, lm.locate( obj ));
      file.setWritable( true );
      if( !file.exists() )
      	file.getParentFile().mkdirs();
    	if( !this.checkWrite( file, replace ))
    		throw new CollectionException( S_ERR_COULD_NOT_ADD_CONCEPT_EXISTS + obj.getDescriptor().toString() );
      
      fileStream = new FileOutputStream( file );
      outStream = new BufferedOutputStream( fileStream );

      IPersistence<T> persist = super.getParser().getPersistence();
      persist.open();
      persist.write( obj, outStream );
      persist.close();
    }
    catch( Exception e ){
      throw new CollectionException( e );
    }
    finally {
      IOUtils.closeOutputStream( outStream );
      IOUtils.closeOutputStream( fileStream );
      if( file != null )
      	file.setWritable( false );
      this.leave();
    }
  }

  /**
   * Write the model to the database. Depending on the replace options,
   * an existing model may be overwritten, or conversely throw an exception
   *
   * @param model IConcept concept
   * @param replace boolean
   * @return Collection
   * @throws CollectionException
  */
  protected Collection<T> write( Collection<? extends T> list, ReplaceOptions replace )
    throws CollectionException
  {
    if(( list == null ) || ( list.isEmpty() ))
    	return new ArrayList<T>();
  	
    Set<IDescribable<?>> check = new TreeSet<IDescribable<?>>();
    for( T data: list ){
    	if(( data == null ) || ( !this.accept( data )))
    		throw new CollectionException( S_ERR_INVALID_ENTRY + data.getDescriptor() );
    	if( check.contains( data ))
    		throw new CollectionException( S_ERR_DOUBLE_DESCRIPTOR + data.getDescriptor() );
    	check.add( data );
    }

  	Collection<T> results = new ArrayList<T>();
    OutputStream fileStream = null;
    OutputStream outStream = null;

    //Send the document to the jar file.
    File source = new File( super.getLoader().getURI().getPath() );    
    File file = null;
    ILocationManager<? extends IDescribable<?>> lm = null;
    try{
    	lm = new LocationManager( super.getLoader() );
    }
    catch( Exception ex ){
    	throw new CollectionException( ex );
    }
    for( T obj: list ){
    	try{
      	file = new File( source, lm.locate( obj ));
        file.setWritable( true );
        if( !file.exists() )
        	file.getParentFile().mkdirs();
      	if( !this.checkWrite( file, replace ))
      		throw new CollectionException( S_ERR_COULD_NOT_ADD_CONCEPT_EXISTS + obj.getDescriptor().toString() );
        fileStream = new FileOutputStream( file );
      	outStream = new BufferedOutputStream( fileStream );

      	IPersistence<T> persist = super.getParser().getPersistence();
      	persist.open();
      	persist.write( obj, outStream );
      	persist.close();
      	results.add( obj );
      }
      catch( Exception ex ){
      	ex.printStackTrace();
      }
      finally {
        IOUtils.closeOutputStream( outStream );
        IOUtils.closeOutputStream( fileStream );
        if( file != null )
        	file.setWritable( false );
        this.leave();
      }
    }
    return results;
 }

  /**
   * Return the size of the model database
   *
   * @return int
  */
  @Override
	public int size()
  {
    try{
      this.access();
      File source = new File( super.getLoader().getURI().getPath() );    
      if( !source.isDirectory() )
      	return 0;
      int size = source.listFiles().length;
      this.leave();
      return size;
    }
    catch( Exception e ){
      return -1;
    }
  }

	@Override
	public boolean containsID(String ID)
	{
		AbstractFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.ID.name() );
		filter.setAmount( 1 );
		Collection<T> results = this.search(filter, true );
		return !results.isEmpty();
	}

	@Override
	public boolean add(T concept)
	{
		try{
			this.write( concept, ReplaceOptions.FALSE);
			return true;
		}
		catch( CollectionException ex ){
			return false;
		}
	}

	@Override
	public boolean set(T concept) throws CollectionException
	{
		try{
			this.write( concept, ReplaceOptions.TRUE);
			return true;
		}
		catch( CollectionException ex ){
			return false;
		}
	}

  /**
   * Set the concepts in the collection. Depending on the replace options, equivalent concepts
   * already stored in the collection can be overwritten. Returns false if one or more of concept
   * could not be set.
   *
   * @param concept IConcept
   * @return boolean
   * @throws CollectionException
  */
  @Override
	public boolean setAll( Collection<T> concept, ReplaceOptions replace ) throws CollectionException{
  	return false;
  }

	@Override
	public boolean addAll(Collection<? extends T> arg0)
	{
		try{
			this.write( arg0, ReplaceOptions.TRUE);
			return true;
		}
		catch( CollectionException ex ){
			return false;
		}
	}

	@Override
	public boolean remove(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<T> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> remove(IDescriptor descriptor) throws CollectionException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean access(ILoaderAieon loader)
	{
		// TODO Auto-generated method stub
		return false;
	}

 
}