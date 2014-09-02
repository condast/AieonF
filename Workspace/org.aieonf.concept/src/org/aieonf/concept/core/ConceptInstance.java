/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

//J2SE imports
import java.util.*;

import org.aieonf.concept.*;

/**
 * Create a concept, using a properties file
*/
public class ConceptInstance extends MinimalConcept
  implements IFullConcept
{
	/**
	 * For serialization purposes
	*/
	private static final long serialVersionUID = 7682777946699977691L;

	//Error messages
	public static final String S_ERR_INVALID_RELATIONSHIP_DESCRIPTOR = 
		"The relation descriptor is invalid: ";
	//Supported error messages
	public static final String S_ERR_INVALID_RELATIONSHIP = 
		"The concept contains an invalid relationship: ";
	
	//Every concept has a list of relationships
  private List<IRelationship> relationships;

  /**
   * Create a default concept instance
  */
  public ConceptInstance()
  {
    super();
    relationships = new ArrayList<IRelationship>();
  }

  /**
   * Create a concept with the given name
   * @param name String
  */
  public ConceptInstance( String name ) 
  {
    this();
    this.setName( name );
  }

  /**
   * Create a concept with the given name
   *
   * @param id String
   * @param name String
  */
  public ConceptInstance( String id, String name )
  {
    this( name );
    this.setID( id );
  }

  /**
   * Create a concept from a descriptor
   * @param descriptor IDescriptor
  */
  public ConceptInstance( IDescriptor descriptor )
  {
    this( descriptor.getName() );
    this.setID( descriptor.getID() );
    this.setVersion( descriptor.getVersion() );
  }

  /**
   * If true, the values have changed
   * @return
  */
  @Override
  public boolean hasChanged()
  {
  	if( super.hasChanged() )
  		return super.hasChanged();
  	for( IRelationship relationship: this.relationships )
  		if( relationship.hasChanged() )
  			return true;
  	return false;
  }
  
  /**
   * Set the update flag.
   * @param choice
  */
  @Override
  public void setChanged( boolean choice )
  {
  	if( choice == true ){
  		super.setChanged( choice );
  		return;
  	}
  	super.setChanged( false );
  }
  
  /**
   * Clears the relationships of this concept
  */
  protected void clearRelationships()
  {
    relationships.clear();
  }

  /**
   * Add a relationship to this concept
   *
   * @param relationship IRelationship
   * @return boolean
   * @todo activate exception
  */
  @Override
	public boolean addRelationship( IRelationship relationship )
  {
    if( relationship == null )
      return false;

    //Check if relationship instances are already contained.
    if( relationships.contains( relationship ) == false ){
      boolean retVal = relationships.add( relationship );
      super.setChanged( retVal );
      return retVal;
    }
    return false;
  }

  /**
   * Add a List of relationships to this concept
   *
   * @param relationships Collection
   * @return boolean
  */
  @Override
	public final boolean addRelationship( Collection<IRelationship> relationships )
  {
    Iterator<IRelationship> iterator = relationships.iterator();
    Object relation;
    boolean retVal = true;
    while( iterator.hasNext() == true ){
      relation = iterator.next();
      if( relation instanceof IRelationship )
        retVal &= this.addRelationship(( IRelationship )relation );
      else
        retVal = false;
    }
    super.setChanged( retVal );
    return retVal;
  }

  /**
   * Remove a relationship from this concept
   *
   * @param relationship IRelationship
   * @return boolean
  */
  @Override
	public final boolean removeRelationship( IRelationship relationship )
  {
    super.setChanged( true );
    return relationships.remove( relationship );
  }

  /**
   * Get a relationship with the given descriptor
   * Should return null if it doesn't exist
   *
   * @param descriptor IDescriptor
   * @return List<IRelationship>
  */
  @Override
	public List<IRelationship> getRelationship( IDescriptor descriptor )
  {
    List<IRelationship> relations = new ArrayList<IRelationship>();
    for( IRelationship relationship: relationships ){
    	if( relationship.equals( descriptor ))
        relations.add( relationship );
    }
    return relations;
  }

  /**
   * Remove all the relationships of this concept
   *
  */
  @Override
	public void removeRelationships()
  {
    super.setChanged( true );
  	this.relationships.clear();
  }

  /**
   * Get the relationships of the concept
   *
   * @return List<IRelationship>
  */
  @Override
	public List<IRelationship> getRelationships()
  {
    return relationships;
  }

  /**
   * The amount of relationships that this concept contains
   *
   * @return int
  */
  @Override
	public int relationshipSize()
  {
    return relationships.size();
  }

  /**
   * If true there are no relationships
   *
   * @return boolean
  */
  @Override
	public boolean hasNoRelationships()
  {
    return (relationships.isEmpty() );
  }

  /**
   * Get a relationships of the given concept that
   * contain the given concept descriptor
   *
   * @param descriptor IDescriptor
   * @return List<IRelationship>
   * @throws ConceptException
  */
  public static final List<IRelationship> getRelationship( IFixedConcept concept, IDescriptor descriptor )
    throws ConceptException
  {
    List<IRelationship> relations = new ArrayList<IRelationship>();
    List<IRelationship> relationships = concept.getRelationships();
    for( IRelationship relationship: relationships ){
    	if( relationship.getConceptDescriptor().equals( descriptor ))
        relations.add( relationship );
    }
    return relations;
  }

  /**
   * Returns true if the given concept descriptor is a relationship of the
   * given concept.
   *
   * @param descriptor IDescriptor
   * @return List<IRelationship>
   * @throws ConceptException
  */
  public static final boolean containsRelation( IFixedConcept concept, IDescriptor descriptor )
    throws ConceptException
  {
    List<IRelationship> relationships = concept.getRelationships();
    for( IRelationship relationship: relationships ){
    	if( relationship.getConceptDescriptor().equals( descriptor ))
        return true;
    }
    return false;
  }

  /**
   * Get the difference between the given concept and a minimal concept
   *
   * @param check ConceptInstance
   * @return Properties
   * @throws ConceptException
  */
  public static Properties getDifference( ConceptInstance check ) throws ConceptException
  {
    ConceptInstance concept = new ConceptInstance();
    return Descriptor.getDifference( concept, check );
  }
}
