/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

//J2SE
import java.util.*;

import org.aieonf.concept.*;

/**
 * Create a concept, using a properties file
*/
public class RelationshipInstance extends MinimalConcept
  implements IRelationship
{

  /**
   * for serialisation
  */
  private static final long serialVersionUID = -6202601287042704386L;

  //The parent concept of this relationship
  private IFixedConcept parent;

  /**
   * Create a relationship from the given concept. The default
   * cardinality is [0..1]
   *
   * @throws ConceptException
  */
  public RelationshipInstance()
  {
    this.setName( IRelationship.Type.IS_A.name() );
    this.setType( IRelationship.Type.IS_A.name() );
    this.setScope( Scope.PUBLIC );
    this.setExistent( false );
    this.setLeaf( true );
  }

  /**
   * Create a relationship from the given concept
   *
   * @param parent IFixedConcept
   * @throws ConceptException
  */
  protected RelationshipInstance( IFixedConcept parent )
  {
    this();
    this.setType( parent.getName() );
    this.parent = parent;
    this.setExistent( true );
    this.setLeaf( true );
   }

  /**
   * Create a relationship from the given concept
   *
   * @param parent IFixedConcept
   * @param child IDescriptor
   * @throws ConceptException
  */
  public RelationshipInstance( IFixedConcept parent, IDescriptor child )
  {
    this( parent );
    this.setConceptDescriptor( child );
    this.setName( child.getName() );
  }

  public RelationshipInstance( IFixedConcept parent, IDescriptor child, String name )
  {
    this( parent, child );
    this.setName( name );
  }

  public RelationshipInstance( IFixedConcept parent, IDescriptor child, String name, String type )
  {
    this( parent, child, name );
    this.setType( type );
  }

  /**
   * Get the type of the relationship
   *
   * @return String
  */
  @Override
	public final String getType()
  {
    return this.get( IRelationship.Attribute.Type.name() );
  }

  /**
   * Set the type of the relationship
   *
   * @param String
  */
  @Override
	public final void setType( String type )
  {
    this.set( IRelationship.Attribute.Type.name(), type );
  }

  /**
   * Get the parent of the relationship
   *
   * @return IFixedConcept
  */
  @Override
	public final IFixedConcept getParent()
  {
    return this.parent;
  }

  /**
   * Gets the concept descriptor that the relationship refers to
   *
   * @return IDescriptorn
  */
  @Override
	public IDescriptor getConceptDescriptor()
  {
  	if( super.getBoolean( EmbeddedRelationship.EMBEDDED ))
  		return this;

  	Descriptor descriptor = new Descriptor( this );
    descriptor.setVersion( this.getVersion() );
    return descriptor;
  }

  /**
   * Set the concept that the relationship refers to
   *
   * @param descriptor IDescriptor
  */
  public void setConceptDescriptor( IDescriptor descriptor )
  {
    String[] split = descriptor.getName().split( Descriptor.NAME_REGEXP );
    if( split.length > 1 )
      throw new IllegalArgumentException( Descriptor.S_ERR_INVALID_NAME + ": " +
                                  descriptor.getName() );
    this.set( IRelationship.Attribute.DescriptorName.name(), descriptor.getName() );
    this.setID( descriptor.getID() );
    this.setVersion( descriptor.getVersion() );
    this.setName( descriptor.getName() );
    this.setExistent( true );
    this.setLeaf( false );
  }

  /**
   * Update the concept descriptor. Is usually not needed, but as concepts usually
   * get their id's only after storage, it may be necessary in some situations
   * 
   * @param descriptor
   * @throws ConceptException
  */
  @Override
	public void updateConceptDescriptor( IDescriptor descriptor )
  {
  	this.setConceptDescriptor( descriptor );
  }
  
  /**
   * If true, the concept is existent in the local database
   *
   * @return boolean
  */
  @Override
	public final boolean isExistent()
  {
    return Boolean.getBoolean( this.get( IRelationship.Attribute.Existent.name()));
  }

  /**
   * Set whather the concept exists in the local database.
   *
   * @param exists boolean
  */
  @Override
	public final void setExistent( boolean exists )
  {
    this.set( IRelationship.Attribute.Existent.name(), String.valueOf( exists ));
  }

  /**
   * Returns true if the concept is blocked
   *
   * @return boolean
  */
  @Override
	public final boolean isBlocked()
  {
    return Boolean.getBoolean( this.get( IRelationship.Attribute.Block.name() ));
  }

  /**
   * Get or set the blocked option
   *
   * @param blocked boolean
  */
  @Override
	public final void setBlocked( boolean blocked )
  {
    this.set( IRelationship.Attribute.Block.name(), String.valueOf( blocked ));
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
    return Boolean.getBoolean( this.get( IRelationship.Attribute.Leaf.name() ));
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
    this.set( IRelationship.Attribute.Leaf.name(), String.valueOf( isLeaf ));
  }

  /**
   * If true, the concept descriptor of the given relationship matches this one
   *
   * @param relation IRelationship
   * @return boolean
   * @throws ConceptException
   */
  public final boolean equalConcepts( IRelationship relation ) throws ConceptException
  {
    IDescriptor descriptor = relation.getConceptDescriptor();
    return ( descriptor.equals( this.getConceptDescriptor() ));
  }

  
  /**
   * Returns true if the relationship is valid
   * @param relationship
   * @return
  */
  public static final boolean isValid( IRelationship relationship )
  {
  	if( Descriptor.isValid( relationship ) == false )
  		return false;
  	return Descriptor.isValid( relationship.getConceptDescriptor() );
  }
  
  /**
   * Removes all the relationships with the given descriptor of the given concept.
   * Returns a list of relationships of the removed concept
   *
   * @param concept IFullConcept
   * @param descriptor IDescriptor
   * @return List
   * @throws ConceptException
   */
  public final static List<IRelationship> removeRelationships( IFullConcept concept, IDescriptor descriptor )
    throws ConceptException
  {
    List<IRelationship> relations = concept.getRelationships();
    for( IRelationship relation: relations ){
      if( relation.getConceptDescriptor().equals( descriptor ))
    	concept.removeRelationship( relation );
    }
    return relations;
  }

  /**
   * Return the concept descriptors of the relations of the given concept
   *
   * @param concept IFixedConcept
   * @return List
   * @throws ConceptException
  */
  public final static List<IDescriptor> getRelationDescriptors( IFixedConcept concept )
    throws ConceptException
  {
    List<IRelationship> relations = concept.getRelationships();
    List<IDescriptor> descriptors = new ArrayList<IDescriptor>();
    for( IRelationship relation: relations ){
      descriptors.add( relation.getConceptDescriptor() );
    }
    return descriptors;
  }

  /**
   * Returns true if the given concept contains a relationship with the given descriptor
   *
   * @param concept IFixedConcept
   * @return List
   * @throws ConceptException
  */
  public final static boolean hasRelation( IFixedConcept concept, IDescriptor descriptor )
    throws ConceptException
  {
    List<IRelationship> relations = concept.getRelationships();
    for( IRelationship relation: relations ){
      if( relation.getConceptDescriptor().equals( descriptor ))
      	return true;
    }
    return false;
  }

  /**
   * Return the concept descriptors of the relations of the given concept
   *
   * @param concept IFixedConcept
   * @return List
   * @throws ConceptException
  */
  public final static List<IRelationship> getRelationships( IFixedConcept concept, IDescriptor descriptor )
    throws ConceptException
  {
    List<IRelationship> relations = concept.getRelationships();
    List<IRelationship> descriptors = new ArrayList<IRelationship>();
    for( IRelationship relation: relations ){
      if( relation.getConceptDescriptor().equals( descriptor ))
      	descriptors.add( relation );
    }
    return descriptors;
  }

  /**
   * Return the concept descriptors of the relations of the given concept,
   * but omit double entries
   *
   * @param concept IFixedConcept
   * @return List
   * @throws ConceptException
  */
  public final static List<IDescriptor> getCondensedRelationDescriptors( IFixedConcept concept )
    throws ConceptException
  {
    List<IRelationship> relations = concept.getRelationships();
    List<IDescriptor> descriptors = new ArrayList<IDescriptor>();
    for( IRelationship relation: relations ){
      if( descriptors.contains( relation.getConceptDescriptor() ) == false )
        descriptors.add( relation.getConceptDescriptor() );
    }
    return descriptors;
  }
}
