/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

import org.aieonf.concept.core.ConceptException;

/**
 * Defines a relationship, a specialised concept that connects base concepts
*/
public interface IRelationship extends IConcept
{
  //This interface defines the relationships with other concepts
  public static final String RELATIONSHIPS = "Relationships";

  //The attributes that are supported
  public static enum Attribute{
    Type,
    DescriptorName,
    Parent,
    Existent,
    Block,
    Leaf,
    Embedded
  }

  /**
   * The type of relationships that are identified. Each type is in itself a
   * concept name
  */
  public static enum Type{
    IS_A,
    HAS_A,
    PARENT,
    CHILD
  }

  /**
   * Get the parent of the relationship
   *
   * @return IFixedConcept
  */
  public IFixedConcept getParent();

  /**
   * Get the type of the relationship
   *
   * @return String
  */
  public String getType();

  /**
   * Set the type of the the relationship
   *
   * @param type String
  */
  public void setType( String type );
  
  /**
   * Gets the concept that the relationship refers to
   *
   * @return IDescriptor
  */
  public IDescriptor getConceptDescriptor();

  /**
   * Update the concept descriptor. Is usually not needed, but as concepts usually
   * get their id's only after storage, it may be necessary in some situations
   * 
   * @param descriptor
   * @throws ConceptException
  */
  public void updateConceptDescriptor( IDescriptor descriptor );

  /**
   * If true, the concept is existent in the local database
   *
   * @return boolean
  */
  public boolean isExistent();

  /**
   * Set whather the concept exists in the local database.
   *
   * @param exists boolean
  */
  public void setExistent( boolean exists );

  /**
   * Returns true if the concept is blocked
   *
   * @return boolean
  */
  public boolean isBlocked();

  /**
   * Get or set the blocked option
   *
   * @param blocked boolean
  */
  public void setBlocked( boolean blocked );

  /**
   * Returns true if the concept is a leaf. This means that there is no
   * concept coupled to the relationship, but the relationship itself is
   * the concept. This does not necessarily mean that there is no concept
   * descriptor associated with the relationship, but it is up to the
   * application to determine this association. As a default a relationship is
   * a leaf if the associated concept descriptor is null, and a non-null
   * concept descriptor sets the isLeaf to false.
   *
   * @return boolean
  */
  public boolean isLeaf();

  /**
   * Get or set the isLeaf property.This means that there is no
   * concept coupled to the relationship, but the relationship itself is
   * the concept. This does not necessarily mean that there is no concept
   * descriptor associated with the relationship, but it is up to the
   * application to determine this association. As a default a relationship is
   * a leaf if the associated concept descriptor is null, and a non-null
   * concept descriptor sets the isLeaf to false.
   *
   * @param isLeaf boolean
  */
  public void setLeaf( boolean isLeaf );
}
