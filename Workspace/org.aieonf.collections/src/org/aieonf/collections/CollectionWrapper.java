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
import java.util.*;

import org.aieonf.commons.filter.*;
import org.aieonf.concept.*;
import org.aieonf.concept.library.ManifestAieon;
import org.aieonf.concept.loader.ILoaderAieon;

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
public class CollectionWrapper<T extends IDescribable<?>> implements IDescribableCollection<T>, IAccessible
{
  private IDescribableCollection<T> collection;

  public CollectionWrapper( IDescribableCollection<T> collection )
  	throws CollectionException
  {
    this.collection = collection;
  }

  /**
   * Get the collection that is used
   * @return
   */
  protected IDescribableCollection<T> getCollection(){
  	return this.collection;	
  }
  
  /**
   * Returns true if the given descriptor is acceptable for storage in the collection
   * By default it checks if the descriptor details (name, id, version) are filled in and
   * the version is zero or greater.
   * 
   * This also checks if the relationships are valid, if appropriate
   * @param descriptor
   * @return
  */
  @Override
	public boolean accept( IDescribable<?> descriptor ){
  	return this.collection.accept( descriptor );
 }

	/**
	 * If true, the given key is used by the parser to create a unique location. This is usually based on the
	 * descriptor data {name, id, version}. These can thus be retrieved without opening the content
	 */
	@Override
	public boolean canParseFromDescriptor( String key ){
		return this.collection.canParseFromDescriptor(key);
	}

  /**
   * Add a concept to the collection. Returns false if the concept
   * could not be added, for instance if it already exists
   *
   * @param concept IConcept
   * @return boolean
  */
  @Override
	public boolean add( T concept )
  {
  	this.collection.add( concept );
    return true;
  }

  /**
   * Set the concept in the collection. Can overwrite equivalent concepts
   * already stored in the collection. Returns false if the concept
   * could not be set.
   *
   * @param concept IConcept
   * @return boolean
   * @throws CollectionException
  */
  @Override
	public boolean set( T concept )  throws CollectionException
  {
  	this.collection.set( concept );
    return true;
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
  	return this.collection.setAll(concept, replace);
  }

  /**
   * Add the given collection. The replace options
   * govern this process. Returns all the concepts that were
   * successfully added
   *
   * @param concepts Collection<IConcept>
   * @param replace boolean
   * @return Collection<IDescriptor>
  */
  @Override
  public boolean addAll( Collection<? extends T> concepts )
  {
    return this.collection.addAll( concepts );
  }

  /**
   * Search all the concepts that pass the given filter
   *
   * @param filter Filter
   * @return List<IConcept>
   * @throws CollectionException
  */
  @Override
	public List<T> search( IFilter<T> filter, boolean descriptorOnly ) throws CollectionException
  {
  	return new ArrayList<T>( this.collection.search( filter, descriptorOnly ));
  }
  
	@Override
	public boolean contains( Object descriptor)
	{
		return this.collection.contains( descriptor );
	}

	@Override
	public boolean containsAll(	Collection<?> descriptors)
	{
		return this.collection.containsAll( descriptors );
	}

	@Override
	public Collection<IDescriptor> getDescriptors() throws CollectionException
	{
		return this.collection.getDescriptors();
	}

	@Override
	public int size()
	{
		return this.collection.size();
	}

	@Override
	public void clear()
	{
		this.collection.clear();
	}

	@Override
	public boolean isEmpty()
	{
		return this.collection.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return this.collection.iterator();
	}

	@Override
	public boolean remove(Object o)
	{
		return this.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return this.collection.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.collection.retainAll(c);
	}

	@Override
	public Object[] toArray()
	{
		return this.collection.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.collection.toArray(a);
	}

	@Override
	public void prepare(ILoaderAieon loader) throws CollectionException
	{
		if(!( this.collection instanceof IAccessible ))
			return;
		IAccessible accessible = ( IAccessible )this.collection;
		accessible.prepare(loader);
	}

	@Override
	public boolean access()
	{
		if(!( this.collection instanceof IAccessible ))
			return true;
		IAccessible accessible = ( IAccessible )this.collection;
		return accessible.access();
	}

	@Override
	public boolean canAccess()
	{
		if(!( this.collection instanceof IAccessible ))
			return true;
		IAccessible accessible = ( IAccessible )this.collection;
		return accessible.canAccess();
	}

	@Override
	public ManifestAieon getLoader()
	{
		if(!( this.collection instanceof IAccessible ))
			return null;
		IAccessible accessible = ( IAccessible )this.collection;
		return accessible.getLoader();
	}

	@Override
	public boolean containsID(String ID)
	{
		if(!( this.collection instanceof IAccessible ))
			return false;
		IAccessible accessible = ( IAccessible )this.collection;
		return accessible.containsID(ID);
	}

	@Override
	public void leave()
	{
		if(!( this.collection instanceof IAccessible ))
			return;
		IAccessible accessible = ( IAccessible )this.collection;
		accessible.leave();
	}

	@Override
	public void updateManifest(ManifestAieon manifest) throws CollectionException
	{
		if(!( this.collection instanceof IAccessible ))
			return;
		IAccessible accessible = ( IAccessible )this.collection;
		accessible.updateManifest(manifest);
	}

	@Override
	public Collection<T> remove(IDescriptor descriptor) throws CollectionException
	{
		return this.collection.remove(descriptor);
	}
}
