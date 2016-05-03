/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

import org.aieonf.commons.strings.StringStyler;

/**
 * Defines a concept, basically a wrapper around the underlying XML definition
*/
public interface IConcept extends IDescriptor
{
  //The concept name
  public static final String CONCEPT = "Concept";

  //This interface defines the relationships with other concepts
  public final String ATTRIBUTES = "Attributes";

  /**
   * The basic elements of a concept
  */
  public static enum Attributes
  {
    AUTHOR,
    COPYRIGHT,
    HIDDEN,
    READ_ONLY,
    SCOPE, //public, private, member
    SOURCE;

	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString() );
	}

	public static boolean isValid( String str ){
		for( Attributes attr: values() ){
			if( attr.name().equals( str ))
				return true;
		}
		return false;
	}
  }

  /**
   * The additional scope values that are supported (besides public)
  */
  public static enum Scope
  {
    UNKNOWN,
    MESSAGE,
    APPLICATION,
    PRIVATE,
    DOMAIN,
    MEMBERSHIP,
    CONSTRAINED,
    PUBLIC,
    SHARED;

	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString());
	}
  }

  /**
   * The amount of concepts allowed within the scope
  */
  public static enum Amount
  {
    None,
    One,
    More
  }

  /**
   * The types of fixing
  */
  public static enum Fixed
  {
    False,
    True,
    Database;
  }

  /**
   * Get the scope of the concept (default public)
   *
   * @return Scope
  */
  public Scope getScope();
  
  /**
   * Get the source for this concept
   * @return
   */
  public String getSource();

  /**
   * Set the scope of the concept (default public)
   *
   * @param scope Attribute
  */
  public void setScope( Scope scope );

  /**
   * Returns true if the concept is read-only
   *
   * @return boolean
  */
  public boolean isReadOnly();

  /**
   * Get or set the read-only option
   *
   * @param readOnly boolean
  */
  public void setReadOnly( boolean readOnly );

  /**
   * Returns true if the concept is hidden
   *
   * @return boolean
  */
  public boolean isHidden();

  /**
   * Get or set the hidden option
   *
   * @param hidden boolean
  */
  public void setHidden( boolean hidden );

  /**
   * Returns true if the given descriptor is of the same type as this one.
   *
   * @param descriptor IDescriptor
   * @return boolean
  */
  public boolean isA( IDescriptor descriptor );

  /**
   * Returns true if the given concept contains the given key
   *
   * @param key String
   * @return boolean
  */
  public boolean hasA( String key );

  /**
   * Get the boolean value of a concept. Throws an exception if the
   * attribute is not a boolean
   *
   * @param attribute String
   * @return boolean
  */
  @Override
	public boolean getBoolean( String attribute );

  /**
   * Returns true if the given concept is equal to this one
   *
   * @param concept Object
   * @return boolean
  */
  @Override
	public boolean equals( Object concept );

  /**
   * Returns a string representation of the concept (XML)
   *
   * @return String
  */
  @Override
	public String toString();
}
