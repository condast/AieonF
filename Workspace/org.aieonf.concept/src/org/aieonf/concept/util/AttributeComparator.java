package org.aieonf.concept.util;

//J2SE imports

import org.aieonf.concept.*;

  /**
   * <p>Title: Conceptual Network Database</p>
   * <p>Description: Stores concepts; XML pages that describe functions</p>
   * <p>Copyright: Copyright (c) 2004</p>
   * <p>Company: Condast</p>
  * @author Kees Pieters
   * @version 1.0
   */
  public class AttributeComparator<T extends IConcept> extends DescriptorComparator<T>
  {

    /**
     * Store the attribute that is u for comparing
     */
    private String attribute;
    /**
     * Compars two concepts for sorting based on the given attribute key name
     */
    public AttributeComparator( String attribute )
    {
      this.attribute = attribute;
    }

    /**
     * Returns >0 if pFirst > pSecond, 0 if they are equal
     * and negative otherwise
     * @param pFirst Object
     * @param pSecond Object
     * @return int
     */
    @Override
		public int compare( T fConcept, T sConcept )
    {

      String name1 = fConcept.get( attribute );
      if( name1 == null )
        return super.compare( fConcept, sConcept );
      name1 = name1.toLowerCase();

      String name2 = sConcept.get( attribute );
      if( name2 == null )
        return super.compare( fConcept, sConcept );
      name2 = name2.toLowerCase();

      if( name1.equals( name2 ) == false )
        return name1.compareTo( name2 );
      return( fConcept.getScope().compareTo( sConcept.getScope() ));
    }
}
