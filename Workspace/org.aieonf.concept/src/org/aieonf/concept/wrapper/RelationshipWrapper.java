package org.aieonf.concept.wrapper;

import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IFixedConcept;
import org.aieonf.concept.IRelationship;

/**
 * Wheras concepts can be made specific by transforming
 * them to specific instances, A rleationship is often best
 * served by creating wrappers around an IRelationship
 * instance, thereby allowing manipulation in the instance
 * itself instead of creating a clone. 
 * This class provides the default functionality for such
 * wrappers 
 * @author Kees Pieters
 *
 */
public class RelationshipWrapper extends ConceptWrapper implements IRelationship {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5846800978555223764L;

	public RelationshipWrapper( IRelationship relationship )
	{
		super( relationship );
	}
	
	/**
	 * Get the relationship
	 * @return
	 */
	protected IRelationship getRelationship()
	{
		return ( IRelationship )super.getDescriptor();
	}
	
  /**
   * Get the type of the relationship
   *
   * @return String
  */
  @Override
	public final String getType()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
  	return relationship.getType();
  }

  /**
   * Set the type of the relationship
   *
   * @param String
  */
  @Override
	public final void setType( String type )
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    relationship.setType( type );
  }

  /**
   * Get the parent of the relationship
   *
   * @return IFixedConcept
  */
  @Override
	public final IFixedConcept getParent()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    return relationship.getParent();
  }

  /**
   * Gets the concept descriptor that the relationship refers to
   *
   * @return IDescriptor
  */
  @Override
	public final IDescriptor getConceptDescriptor()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
  	return relationship.getConceptDescriptor();
  }

  /**
   * Update the concept descriptor. Is usually not needed, but as concepts usually
   * get their id's only after storage, it may be necessary in some situations
   * 
   * @param descriptor
  */
  @Override
	public void updateConceptDescriptor( IDescriptor descriptor )
 {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
  	relationship.updateConceptDescriptor( descriptor );  	
 }

  /**
   * If true, the concept is existent in the local database
   *
   * @return boolean
  */
  @Override
	public final boolean isExistent()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    return relationship.isExistent();
  }

  /**
   * Set whather the concept exists in the local database.
   *
   * @param exists boolean
  */
  @Override
	public final void setExistent( boolean exists )
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    relationship.setExistent( exists );
  }

  /**
   * Returns true if the concept is blocked
   *
   * @return boolean
  */
  @Override
	public final boolean isBlocked()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    return relationship.isBlocked();
  }

  /**
   * Get or set the blocked option
   *
   * @param blocked boolean
  */
  @Override
	public final void setBlocked( boolean blocked )
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    relationship.setBlocked( blocked );
  }

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
  @Override
	public final boolean isLeaf()
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    return relationship.isLeaf();
  }

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
  @Override
	public final void setLeaf( boolean isLeaf )
  {
    IRelationship relationship = ( IRelationship)super.getDescriptor();
    relationship.setLeaf( isLeaf );
  }
}
