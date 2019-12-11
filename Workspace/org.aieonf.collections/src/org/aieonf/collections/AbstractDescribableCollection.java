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
import java.util.*;

import org.aieonf.collections.parser.ICollectionParser;
import org.aieonf.commons.filter.*;
import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.*;
import org.aieonf.concept.core.*;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.DescriptorBatchFilter;
import org.aieonf.concept.filter.DescriptorFilter;
import org.aieonf.concept.loader.ILoaderAieon;

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
public abstract class AbstractDescribableCollection<T extends IDescribable> extends AbstractChangeableCollection<T> implements IDescribableCollection<T>
{
  //The error messages
  public static final String S_ERR_JAR_WRITE_EXCEPTION =
    "Could not write to jar: ";
  public static final String S_ERR_COLLECTION_ACCESS_DENIED_EXCEPTION = "Access is denied"; 
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

  /**
   * Initialise and create this class
   * 
   * @param lm ILoaderManager
   * @param persist IPersistence
   * @throws CollectionException
   */
  public AbstractDescribableCollection( ICollectionParser<T> parser )
  	throws CollectionException
  {
  	super( parser );
  }

	/* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#getDescriptors()
	 */
  @Override
	public Collection<IDescriptor> getDescriptors() throws CollectionException
  {
    IFilter<IDescriptor> filter = new AttributeFilter<IDescriptor>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
  	ICollectionParser<T> parser = super.getParser();
   	parser.setDescriptorOnly( true );
  	try {
			parser.open();
  		parser.parse( filter );
		 	parser.close();
  	}
		catch (ParseException e) {
			e.printStackTrace();
		}
   	
    return getDescriptors( parser.getResults() );
  }

  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#search(org.condast.util.filter.Filter, boolean, int)
	 */
	@Override
	public Collection<T> search( IFilter<T> filter, boolean descriptorOnly) throws CollectionException
	{
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( descriptorOnly );
  	try {
			parser.open();
  		parser.parse( filter );
		 	parser.close();
  	}
		catch (ParseException e) {
			e.printStackTrace();
		}
   	Collection<T> results = parser.getResults();
   	if( results == null )
   		return new ArrayList<T>();
   	return results;
  }
  
  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#contains(org.condast.concept.IDescriptor)
	 */
  @Override
	public boolean contains( Object changeable )
  {
    if( !( changeable instanceof IDescribable ))
    		return false;
    
  	IFilter<IDescribable> filter = new DescriptorFilter<IDescribable>( DescriptorFilter.Rules.Equals, ( IDescribable)changeable );
   	filter.setAmount( 1 );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( true );
  	try {
		parser.open();
  		parser.parse( filter );
		parser.close();
  	}
	catch (ParseException e) {
		e.printStackTrace();
	}
   	return !parser.getResults().isEmpty();
  }

  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#containsAll(java.util.Collection)
	 */
  @Override
	public boolean containsAll( Collection<?> descriptors )
  {
    if( descriptors == null )
    	throw new NullPointerException();

  	IFilter<IDescribable> filter = new DescriptorBatchFilter<IDescribable>( DescriptorFilter.Rules.Equals, descriptors );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( true );
  	try {
			parser.open();
  		parser.parse( filter );
		 	parser.close();
  	}
		catch (ParseException e) {
			e.printStackTrace();
		}
   	return !parser.getResults().isEmpty();
    }

  /* (non-Javadoc)
	 * @see org.condast.concept.database.collections.IBasicCollection#size()
	 */
  @Override
	public int size()
  {
  	IFilter<IDescribable> filter = new AttributeFilter<IDescribable>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( true );
  	try {
  		parser.open();
  		parser.parse( filter );
  		parser.close();
  	}
  	catch (ParseException e) {
  		e.printStackTrace();
  	}
  	return parser.getResults().size();
  }

  @Override
  public boolean containsID(String ID)
  {
  	if(!( Descriptor.isNull( ID )))
  		return false;

  	IFilter<IDescribable> filter = new AttributeFilter<IDescribable>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.ID.name(), ID );
  	filter.setAmount( 1 );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( true );
  	try {
  		parser.open();
  		parser.parse( filter );
  		parser.close();
  	}
  	catch (ParseException e) {
  		e.printStackTrace();
  	}
  	return !parser.getResults().isEmpty();
  }

  /**
   * Write the given concepts to the collection
   * @param concepts
   * @throws CollectionException
   */
  protected abstract void write( Collection<? extends T> concepts ) throws CollectionException;
  
	@Override
	public boolean add(T e)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter, false);
		if( !this.accept( e ))
			throw new CollectionException( S_ERR_INVALID_ENTRY + e.getDescriptor() );
		if( concepts.contains( e ))
			throw new CollectionException( S_ERR_ALREADY_PRESENT + e.toString() );
		boolean result = concepts.add(e);
		this.write( concepts );
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter, false);
		boolean result = true;
    for( T concept: c ){
			if( !this.accept( concept ))
				throw new CollectionException( S_ERR_INVALID_ENTRY + concept );
			if( concepts.contains( concept ))
				throw new CollectionException( S_ERR_ALREADY_PRESENT + concept );
		}
		result = concepts.addAll( c );
		this.write( concepts );
		return result;
	}

	@Override
	public void clear()
	{
		File source = new File( super.getLoader().getURI() );
		source.delete();
		this.create( super.getLoader() );
	}

	@Override
	public boolean isEmpty()
	{
  	IFilter<IDescribable> filter = new AttributeFilter<IDescribable>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
  	filter.setAmount( 1 );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( true );
  	try {
  		parser.open();
  		parser.parse( filter );
  		parser.close();
  	}
  	catch (ParseException e) {
  		e.printStackTrace();
  	}
  	return parser.getResults().isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
  	IFilter<IDescribable> filter = new AttributeFilter<IDescribable>( AttributeFilter.Rules.Equals, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
  	filter.setAmount( 1 );
  	ICollectionParser<T> parser = super.getParser();
  	parser.setDescriptorOnly( false );
  	try {
  		parser.open();
  		parser.parse( filter );
  		parser.close();
  	}
  	catch (ParseException e) {
  		e.printStackTrace();
  	}
  	return parser.getResults().iterator();
	}

	@Override
	public boolean remove(Object o)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter, false );
		boolean result = concepts.remove(o);
		this.write( concepts );
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter, false);
		boolean result = concepts.removeAll(c);
		this.write( concepts );
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter, false);
		boolean result = concepts.retainAll(c);
		this.write( concepts );
		return result;
	}

	@Override
	public Object[] toArray()
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter,false);
		return concepts.toArray();
	}

	@Override
	public <U> U[] toArray(U[] a)
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter,false);
		return concepts.toArray(a);
	}

	@Override
	public boolean set(T concept) throws CollectionException
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter,false);
		if( !this.accept( concept ))
			throw new CollectionException( S_ERR_INVALID_ENTRY + concept.getDescriptor() );
		if( concepts.contains( concept ))
			throw new CollectionException( S_ERR_ALREADY_PRESENT + concept.toString() );
		boolean result = concepts.remove( concept );
		concepts.add( concept );
		this.write( concepts );
		return result;
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
	public boolean setAll( Collection<T> c, ReplaceOptions replace ) throws CollectionException{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, IDescriptor.Attributes.NAME.name(), WildcardFilter.S_ALL );
    Collection<T> describables = this.search(filter,false);
		boolean result = true;
    for( T desc: c ){
    	if( !this.accept( desc ))
				throw new CollectionException( S_ERR_INVALID_ENTRY + desc );
			switch( replace ){
				case TRUE:
					if( !describables.contains( desc ))
						throw new CollectionException( S_ERR_CONCEPT_DOES_NOT_EXIST + desc );
					else
						describables.remove( desc );
					break;
				case FALSE:
					if( describables.contains( desc ))
						throw new CollectionException( S_ERR_ALREADY_PRESENT + desc );
					break;
				default:
					if( describables.contains( desc ))
						describables.remove(desc);
					break;
			}
		}
		result = describables.addAll( c );
		this.write( describables );
		return result;
  }

	@Override
	public Collection<T> remove(IDescriptor descriptor) throws CollectionException
	{
    IFilter<T> filter = new AttributeFilter<T>( AttributeFilter.Rules.Wildcard, WildcardFilter.S_ALL );
    Collection<T> concepts = this.search(filter,false);
    Collection<T> results = new ArrayList<T>();
    for( T concept: concepts ){
    	if( concept.equals( descriptor )){
    		concepts.remove( descriptor );
    	  results.add(concept);
    	}
    }
		return results;
	}
	
	/**
	 * Get a list of descriptors from the given collection
	 * @param collection
	 * @return
	 */
	public static Collection<IDescriptor> getDescriptors( Collection<? extends IDescribable> collection ){
		Collection<IDescriptor> descriptors = new ArrayList<IDescriptor>();
		for( IDescribable descriptor: collection )
			descriptors.add( descriptor.getDescriptor() );
		return descriptors;
			
	}
	
  /**
   * Helper method to prepare a collection
   * @param loader
   * @param collection
   */
	public static void prepare( ILoaderAieon loader, IDescribableCollection<? extends IDescribable> collection ){
  	IAccessible accessible = ( IAccessible )collection;
  	accessible.prepare(loader);
  }
	
}