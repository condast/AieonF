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
public interface IRelationship extends IDescriptor
{
  //The concept name
  public static final String RELATIONSHIP = "Relationship";

  /**
   * The basic elements of a concept
  */
  public static enum Attributes
  {
    CHILD_OF,
    PARENT_OF,
    HAS_A,
    IS_A;

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
}
