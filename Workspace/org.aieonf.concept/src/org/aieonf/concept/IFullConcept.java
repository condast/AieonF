/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

/**
 * Defines a full concept. This is a utility definition that combines
*  the three elements of a concept
*/
public interface IFullConcept extends IFixedConcept, IConceptRelationship
{
  /**
   * Remove all the relationships of this concept
   *
  */
  public void removeRelationships();
}
