package org.aieonf.collections;

import java.util.*;

import org.aieonf.commons.filter.*;
import org.aieonf.concept.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class SynchronizedDescribableCollection<T extends IDescribable<?>>
  implements IDescribableCollection<T>
{
  /**
   * The base collection that provides the data
  */
  private IDescribableCollection<T> collection;

  /**
   * The object that is used to block the functions
  */
  private Object lock;

  /**
   * Create the synchronised collection
   * @param collection IConceptCollection
  */
  public SynchronizedDescribableCollection( IDescribableCollection<T> collection )
  {
    this.collection = collection;
    this.lock = null;
  }

  /**
   * Lock the database
  */
  protected synchronized void lock()
  {
    if( this.lock == null ){
      this.lock = new Object();
      return;
    }
    synchronized( this.lock ){
      try{
        this.lock.wait();
      }
      catch( InterruptedException ex ){ /* ignore */}
    }
  }

  /**
   * unlock the database
  */
  protected void unlock()
  {
    if( this.lock == null )
      return;

    synchronized( this.lock ){
      this.lock.notifyAll();
      this.lock = null;
    }
  }

  /**
   * Returns true if the given descriptor is acceptable for storage in the collection
   * By default it checks if the descriptor details (name, id, version) are filled in and
   * the version is zero or greater. Also checks if the relationships are valid, if appropriate
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
   * @throws CollectionException
  */
  @Override
	public boolean add( T concept )
  {
    return this.collection.add( concept );
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
    return this.collection.set( concept );
  }

  /**
   * Add the collection to the given database. returns the descriptors of all the 
   * concepts that were successfully added
   *
   * @param collection Collection
   * @param replace boolean
   * @return boolean
   * @throws CollectionException
  */
  @Override
	public boolean addAll( Collection<? extends T> concepts )
  {
    return this.collection.addAll( concepts );
  }

  /**
   * If true, the given descriptor is contained in the collection
   *
   * @param descriptor IDescriptor
   * @return boolean
  */
  @Override
	public boolean contains( Object descriptor ) 
  {
    return this.collection.contains( descriptor );
  }

  /**
   * Returns the subset of all the given descriptors that are 
   * contained in this collection
   * @param descriptors
   * @return
   * @throws CollectionException
  */
  @Override
	public boolean containsAll( Collection<?> descriptors )
  {
  	return this.collection.containsAll( descriptors );
  }

  /**
   * Get the descriptors contained in this collection.
   * It is not guaranteed that the case is correct, so additional checks
   * are needed to confirm whether the descriptors are equal
   *
   * @return Collection<IConcept>
   * @throws CollectionException
  */
  @Override
	public Collection<IDescriptor> getDescriptors() throws CollectionException
  {
    return this.collection.getDescriptors();
  }

  /**
   * Remove the given concept form the collection
   *
   * @param descriptor IDescriptor
   * @return List<IConcept>
   * @throws CollectionException
   */
  @Override
  public Collection<T> remove( IDescriptor descriptor ) throws CollectionException
  {
    return this.collection.remove( descriptor );
  }

  /**
   * Remove all the given concepts from the database
   *
   * @param descriptors Collection<IDescriptor>
   * @return Collection<IConcept>
   * @throws CollectionException
  */
  @Override
  public boolean removeAll( Collection<?> descriptors )
  {
  	return this.collection.removeAll( descriptors );
  }
  
  /**
   * Get the concepts that pass the given filter
   *
   * @param filter Filter
   * @return List<IConcept>
   * @throws CollectionException
  */
  @Override
	public Collection<T> search( IFilter<T> filter, boolean descriptorOnly ) throws CollectionException
  {
    return this.collection.search( filter, descriptorOnly );
  }

  /**
   * Get the size of the collection
   *
   * @return int
  */
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
		return this.collection.remove(o);
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
	public boolean setAll(
			Collection<T> concept,
			org.aieonf.collections.IDescribableCollection.ReplaceOptions replace)
			throws CollectionException
	{
		return this.collection.setAll(concept, replace);
	}
}
