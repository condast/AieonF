package org.aieonf.concept.util;

import org.aieonf.commons.comparator.IHierarchicalComparator;
//J2SE imports
import org.aieonf.concept.*;
import org.aieonf.concept.filter.VersionFilter;

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
public class DescriptorComparator<T extends IDescriptor> implements IHierarchicalComparator<T>
{
  //Compares the concepts according to the versioning strategy
  VersionFilter.Compare vc;

  /**
   * Compars two concepts for sorting
  */
  public DescriptorComparator()
  {
    this.vc = VersionFilter.Compare.ALL;
  }

  /**
   * Compares two concepts for sorting
   *
   * @param VersionFilter.Compare vc
  */
  public DescriptorComparator( VersionFilter.Compare vc )
  {
    this.vc = vc;
  }

  /**
   * Returns >0 if pFirts > pSecond, 0 if they are equal
   * and negative otherwise
   * @param pFirst Object
   * @param pSecond Object
   * @return int
   */
  @Override
	public int compare ( T pFirst, T pSecond )
  {
    int result = this.compareSubrange( 0, pFirst, pSecond );
    if( result != 0 )
      return result;

    result = this.compareSubrange( 1, pFirst, pSecond );
    if( result != 0 )
      return result;

    return this.compareSubrange( 2, pFirst, pSecond );
  }

  /**
   * Compare the two string arguments
   * @param first
   * @param second
   * @return
   */
  protected int compareString( String first, String second ){
    if(( first == null ) && ( second != null ))
      return -1;
    if(( first != null ) && ( second == null ))
      return 1;

    return first.toLowerCase().compareTo( second.toLowerCase() );	
  }
  
	@Override
	public int compareRange(Comparable<?> reference, Comparable<?> target)
	{
    if(( reference == null ) && ( target != null ))
      return -1;
    if(( reference != null ) && ( target == null ))
      return 1;

    return  compareString( reference.toString(), target.toString() );
	}

	@Override
	public int compareSubrange(int level, Comparable<?> reference, Comparable<?> target )
	{
		switch( level ){
			case 0:
				return this.compareRange( reference, target );
			//case 1:
		  //  return compareString( reference.getID(), target.getID() );
			//case 2:
		  //  int result = ( reference.getVersion() - target.getVersion() );
		  //  if(( this.vc.equals( VersionFilter.Compare.EQUAL_GREATER )) ||
		  //  	( this.vc.equals( VersionFilter.Compare.GREATER )))
		  //  return result;
		}
		return -1;
	}

	@Override
	public int size()
	{
		return 3;
	}
}
