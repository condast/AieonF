/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept.util;

import org.aieonf.concept.loader.ILoaderAieon;

/**
 * Defines the base interface to read concepts from the collection.
 * The collection is identified by a location (URI)
*/
public interface IDescribableSet<T extends ILoaderAieon>
{

  /**
   * Prepare to access the collection
   *
  */
  public void prepare( ILoaderAieon loader );

  /**
   * Get the loader
   * @return
   */
  public T getLoader();
  
  /**
   * If the given ID is contained in this accessible collection, then return true;
   * @param ID
   * @return
   */
  public boolean containsID( String ID );
}