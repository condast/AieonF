package org.aieonf.concept.core;

//J2SE
import java.util.*;

import org.aieonf.concept.*;
import org.aieonf.concept.wrapper.ConceptWrapper;

public class EmbeddedRelationship extends RelationshipInstance
	implements IFixedConcept
{
	/**
	 * Needed for serialization
  */
	private static final long serialVersionUID = -5652436965260284184L;

	//Supported attributes
	public static final String EMBEDDED = "Embedded";
	
	/**
	 * The embedded concept
	 */
	private IDescriptor embedded;
	
	/**
	 * Create a default embedded relationship. By default the
	 * isLeaf property is set to true
	 * @throws ConceptException
	*/
	public EmbeddedRelationship() throws ConceptException
	{
		super();
		this.setLeaf( true );
		this.setEmbedded( true );
	}

  /**
   * Create a embedded relationship for the given concept. By default the
   * isLeaf property is set to true, and the concept properties are copied
   * into those of the relationship. So only attributes of the relationship
   * that do not match with those of the concept are retained.
   * The concept descriptor of the concept is used.
   * NOTE: Relationships of the concept are NOT copied.
   *
   * @param parent IFixedConcept
   * @param concept IConcept
  */
  public EmbeddedRelationship( IFixedConcept parent, IDescriptor descriptor )
  {
    this( parent, descriptor, true );
 }

  /**
   * Create a embedded relationship for the given concept. By default the
   * isLeaf property is set to true, and the concept properties are copied
   * into those of the relationship. So only attributes of the relationship
   * that do not match with those of the concept are retained.
   * The concept descriptor of the concept is used.
   * NOTE: Relationships of the concept are NOT copied.
   *
   * @param parent IFixedConcept
   * @param concept IConcept
   * @param leaf boolean
  */
  public EmbeddedRelationship( IFixedConcept parent, IDescriptor embedded, boolean leaf )
  {
    super( parent );
    //this.fill( descriptor );
    this.embedded = new ConceptWrapper( embedded );
		this.setLeaf( leaf );
		this.setEmbedded( true );
  }

  /* (non-Javadoc)
	 * @see org.condast.concept.core.RelationshipInstance#getConceptDescriptor()
	 */
	@Override
	public IDescriptor getConceptDescriptor() 
	{
		if( !this.isEmbedded() )
			return super.getConceptDescriptor();
		return embedded;
	}

	/**
   * If true, the relationship contains an embedded concept
   *
   * @return boolean
  */
  public final boolean isEmbedded()
  {
    return super.getBoolean( EMBEDDED );
  }

  /**
   * Set whether or not the relationship contains an embedded concept
   *
   * @param choice boolean
  */
  public final void setEmbedded( boolean choice )
  {
    super.set( EMBEDDED, String.valueOf( choice ));
  }

  /**
   * Fill the relationship with the properties of the given concept.
   * Overwrite similar attributes, but the non-matching attributes of
   * the relationship are retained.
   *
   * @param concept IConcept
   * @throws ConceptException
  */
  protected final void fill( IDescriptor descriptor ) throws ConceptException
  {
    Iterator<String> iterator = descriptor.iterator();
    String key;
    while( iterator.hasNext() ){
      key = iterator.next();
      this.set( key, descriptor.get( key ));
    }
    this.set( IRelationship.Attribute.DescriptorName.name(), descriptor.getName() );
  }

  /**
   * Returns true if the given relation is embedded. This means that:
   * 1: the relationship contains an 'embedded' attribute
   * 2: The concept descriptor equals the relationship descriptor
   *
   * @param relation IRelationship
   * @return boolean
   * @throws ConceptException
  */
  public static final boolean isEmbedded( IDescriptor descriptor )
    throws ConceptException
  {
    if( descriptor.getBoolean( EmbeddedRelationship.EMBEDDED ) == false )
      return false;
    if(( descriptor instanceof IRelationship ) == false )
    	return false;
    IRelationship relationship = ( IRelationship )descriptor; 
    return( relationship.getConceptDescriptor().equals( descriptor ));
  }

  /**
   * Get a relationship that is associated with a concept with the given
   * descriptor. Should return null if it doesn't exist
   *
   * @param descriptor IDescriptor
   * @return List<IRelationship>
  */
  @Override
	public final List<IRelationship> getRelationship( IDescriptor descriptor )
  {
    List<IRelationship> relations = new ArrayList<IRelationship>();
    if( descriptor.equals( this ))
    	relations.add( new RelationshipInstance( this, super.getParent() ));
    return relations;
  }

  /**
   * Get the relationships of the concept
   *
   * @return List<IRelationship>
   * @throws ConceptException
  */
	@Override
	public List<IRelationship> getRelationships(){
    List<IRelationship> relations = new ArrayList<IRelationship>();
		relations.add( new RelationshipInstance( this, super.getParent() ));
    return relations;
	}

  /**
   * If true there are no relationships
   *
   * @return boolean
  */
	@Override
	public boolean hasNoRelationships()
	{
		return false;
	}

  /**
   * The amount of relationships that this concept contains
   *
   * @return int
  */
	@Override
	public int relationshipSize() 
	{
		return 1;
	}
	
  /**
   * Make an id, which is consistent for the given name. This ID
   * is mainly used to create a working ID for embedded concepts
   * @param name
   * @return
  */
  public static String makeEmbeddedID( String name )
  {
    byte[] bytes = name.getBytes();
    StringBuffer buffer = new StringBuffer();
    for( byte bt: bytes){
    	buffer.append(bt);
    }
  	return buffer.toString();
  }
	
}
