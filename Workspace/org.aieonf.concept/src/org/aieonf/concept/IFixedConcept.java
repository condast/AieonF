/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
*/
package org.aieonf.concept;

import java.util.*;

/**
 * Defines a concept, basically a wrapper around the underlying XML definition
*/
public interface IFixedConcept extends IConcept
{
	/**
   * Get a relationship with the given descriptor
   * descriptor. Should return null if it doesn't exist
   *
   * @param descriptor IDescriptor
   * @return List
  */
  public List<IRelationship> getRelationship( IDescriptor descriptor );

  /**
   * Get the relationships of the concept
   *
   * @return List
  */
  public List<IRelationship> getRelationships();

  /**
   * The amount of relationships that this concept contains
   *
   * @return int
  */
  public int relationshipSize();

  /**
   * If true there are no relationships
   *
   * @return boolean
  */
  public boolean hasNoRelationships();
}
