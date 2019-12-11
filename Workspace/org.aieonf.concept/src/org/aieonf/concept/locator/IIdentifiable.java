package org.aieonf.concept.locator;

import org.aieonf.concept.IDescribable;


public interface IIdentifiable<T extends LocatorAieon> extends IDescribable
{
  /**
   * Get the name of the database. This name does not have to be unique.
   * It is mainly used for labels etc. 
   * @return
  */
  public String getName();
  
  /**
   * The identifier needs to be unique  
   * @return
  */
  public String getIdentifier();

  /**
   * Returns true if the given identifier is identified
   * in this database
   * @return
  */
  public boolean identify( String identifier );
}
