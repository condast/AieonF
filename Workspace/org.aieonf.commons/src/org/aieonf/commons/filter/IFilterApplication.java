package org.aieonf.commons.filter;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public interface IFilterApplication
{

  /**
   * Add a filter to the filtered application
   * @param filter Filter
  */
  public void addFilter( AbstractFilter<?> filter );

  /**
   * Remove a filter from the filtered application
   * @param filter Filter
  */
  public void removeFilter( AbstractFilter<?> filter );
}
