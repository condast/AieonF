package org.aieonf.concept.util;

//J2SE imports
import java.util.Comparator;

import org.aieonf.concept.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class ConceptWeightComparator<T extends IDescriptor> implements Comparator<T>
{
  /**
   * Compars two concepts for sorting
  */
  public ConceptWeightComparator(){}

  /**
   * Returns >0 if pFirts > pSecond, 0 if they are equal
   * and negative otherwise
   * @param pFirst Object
   * @param pSecond Object
   * @return int
   */
  @Override
	public int compare ( T fConcept, T sConcept )
  {
    if( fConcept.equals( sConcept ) == false )
      return 0;

    String weight1 = fConcept.get( IExtendedConcept.Attribute.Weight.name() );
    if( weight1 == null )
      weight1 = "0";

    String weight2 = sConcept.get( IExtendedConcept.Attribute.Weight.name() );
    if( weight2 == null )
      weight2 = "0";

    int fWeight = Integer.parseInt( weight1 );
    int sWeight = Integer.parseInt( weight2 );
    return ( fWeight - sWeight );
  }
}
