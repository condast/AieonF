package org.aieonf.util.filter;

import java.util.Collection;

import org.aieonf.util.filter.AbstractFilter.Mode;

public interface IFilter<T>
{
  /**
   * Get the name of the filter
   * @return String
  */
  public String getName();

  /**
   * Get the rule of the filter
   * @return String
  */
  public String getRule();

  /**
   * Get the mode of operation
   * @return
  */
  public Mode getMode();
  
  /**
   * Get the amount of objects that need to be filtered. Returns a negative value
   * if all the objects need to be filtered
   * @return
   */
  public int getAmount();

  /**
   * Set the amount of items that need to be filtered
   * @param amount
   */
  public void setAmount( int amount );
  
  /**
   * Returns true if the given object has passed the filter
   *
   * @param obj Object
   * @return boolean
   * @throws FilterException
  */
  public boolean accept( Object obj ) throws FilterException;
  
  /**
   * Returns a filtered list of objects provided by the input list
   *
   * @param Collection List
   * @return Collection
   * @throws FilterException
  */
  public Collection<T> doFilter( Collection<T> collection ) throws FilterException;  

  /**
   * Returns a filtered list of objects provided by the input list. An optional
   * amount can be included to stop the process. If this value is negative
   * the whole collection will be filtered
   *
   * @param Collection collection
   * @param int amount
   * @return List
   * @throws FilterException
  */
  public Collection<T> doFilter( Collection<T> collection, int amount ) throws FilterException;  

  /**
   * Get the items that were rejected after a doFilter operation. 
   * @return
   */
  public Collection<T> getRejected();
}
