package org.aieonf.util.filter;

import org.aieonf.util.StringStyler;

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public abstract class AbstractComparableAttributeFilter<T,U extends Object> extends AbstractFilter<T>
{
  //The supported rule set
  public enum Rules{
  	SMALLER,
  	EQUALS_SMALLER,
   	EQUALS,
   	EQUALS_GREATER,
  	GREATER, 
  	IN_RANGE,
  	OUT_OF_RANGE,
  	IN_RANGE_EXCLUDE_MIN,
  	IN_RANGE_EXCLUDE_MAX,
  	IN_RANGE_EXCLUDE_MIN_MAX;

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return StringStyler.prettyString( super.toString() );
		}
  }

  /**
   * The attribute key and value that is used as reference
  */
  public String refKey;
  public U refVal1;
  public U refVal2;

  /**
   * Create the filter
   *
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter() throws FilterException
  {
    super.setName( AbstractComparableAttributeFilter.class.getName() );
    
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule ) throws FilterException
  {
    super( AbstractComparableAttributeFilter.class.getName(), rule.name() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule, String refKey ) throws FilterException
  {
    super( AbstractComparableAttributeFilter.class.getName(), rule.name() );
    this.refKey = refKey;
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey Attribute
   * @param refVal String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule, String refKey, U refVal )
    throws FilterException
  {
    this( rule, refKey );
    this.refVal1 = refVal;
    this.refVal2 = null;
  }

  /**
   * Create the filter
   *
   * @param name String
   * @param rule String
   * @param refKey String
   * @param refVal String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule, String name, String refKey, U refVal )
    throws FilterException
  {
    super( name, rule.toString() );
    this.refKey = refKey;
    this.refVal1 = refVal;
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey Attribute
   * @param refVal String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule, String refKey, U refVal1, U refVal2 )
    throws FilterException
  {
    this( rule, refKey, refVal1 );
    this.refVal2 = refVal2;
  }

  /**
   * Create the filter
   *
   * @param name String
   * @param rule String
   * @param refKey String
   * @param refVal String
   * @throws FilterException
  */
  public AbstractComparableAttributeFilter( Rules rule, String name, String refKey, U refVal1, U refVal2 )
    throws FilterException
  {
    this( rule, name, refKey, refVal1 );
     this.refVal2 = refVal2;
  }

  /**
   * Set a new reference key
   * @param refKey String
  */
  public void setRefKey( String refKey )
  {
    this.refKey = refKey;
  }

  /**
   * Set a new reference value
   *
   * @param refVal String
  */
  public void setMinRefVal( U refVal )
  {
    this.refVal1 = refVal;
  }

  /**
   * Set a new reference value
   *
   * @param refVal String
  */
  public void setMaxRefVal( U refVal )
  {
    this.refVal2 = refVal;
  }

  /**
   * Get a list of accepted rules
   *
   * return rule String[]
   * @return boolean
  */
  @Override
	public String[] getRules()
  {
  	Rules[] rules = Rules.values();
  	String[] names = new String[ rules.length ];
  	for( int i =0; i < rules.length; i++ )
  		names[ i ] = rules[i].name();
  	return names;
  }

  /**
   * If true, the given rule is accepted by this filter
   *
   * @param rule String
   * @return boolean
  */
  @Override
	protected boolean acceptRule( String rule )
  {
    return AbstractComparableAttributeFilter.checkRule( rule );
  }

  /**
   * Compares the given object with the given reference.
   * @param reference
   * @param obj
   * @return -1: obj < reference, 0: obj equals reference, 1 obj > reference
   */
  protected abstract int compareTo( U reference, Object obj );

  /**
   * If true, the given object is accepted when the 
   * filter is enabled. The filter ALWAYS returns false on a null object.
   * 
   * @param obj
   * @return
   * @throws FilterException
  */
  @Override
	protected boolean acceptEnabled( Object  obj ) throws FilterException
  {
    if(( obj == null ) || (!( obj instanceof Comparable )))
      return false;
    
    U minVal, maxVal;
    Rules rule = Rules.valueOf(super.getRule());
    switch( rule ){
    	case SMALLER:
    		return compareTo(this.refVal1, obj ) < 0;
    	
    	case EQUALS_SMALLER:
    		return compareTo(this.refVal1, obj ) <= 0;
    
    	case EQUALS:
    		return compareTo(this.refVal1, obj ) == 0;

    	case GREATER:
    		return compareTo(this.refVal1, obj) > 0;

    	case EQUALS_GREATER:
    		return compareTo(this.refVal1, obj ) >= 0;

    	case IN_RANGE:
    		if(( this.refVal1 == null ) || ( this.refVal2 == null ))
    			return false;
    		minVal = ( compareTo( this.refVal1, this.refVal2 ) < 0 )? this.refVal1: this.refVal2;
    		maxVal = ( compareTo( this.refVal1, this.refVal2 ) <= 0 )? this.refVal2: this.refVal1;
    		return ( compareTo( minVal, obj) >= 0) && ( compareTo( maxVal, obj ) <= 0 );

      case OUT_OF_RANGE:
      	if(( this.refVal1 == null ) || ( this.refVal2 == null ))
      		return true;
    		minVal = ( compareTo( this.refVal1, this.refVal2 ) < 0 )? this.refVal1: this.refVal2;
    		maxVal = ( compareTo( this.refVal1, this.refVal2 ) <= 0 )? this.refVal2: this.refVal1;
    		return ( compareTo( minVal, obj) < 0) || ( compareTo( maxVal, obj ) > 0 );

    	case IN_RANGE_EXCLUDE_MIN:
    		if(( this.refVal1 == null ) || ( this.refVal2 == null ))
    			return false;
    		minVal = ( compareTo( this.refVal1, this.refVal2 ) < 0 )? this.refVal1: this.refVal2;
    		maxVal = ( compareTo( this.refVal1, this.refVal2 ) <= 0 )? this.refVal2: this.refVal1;
    		return ( compareTo( minVal, obj) > 0) && ( compareTo( maxVal, obj ) <= 0 );

    	case IN_RANGE_EXCLUDE_MAX:
    		if(( this.refVal1 == null ) || ( this.refVal2 == null ))
    			return false;
    		minVal = ( compareTo( this.refVal1, this.refVal2 ) < 0 )? this.refVal1: this.refVal2;
    		maxVal = ( compareTo( this.refVal1, this.refVal2 ) <= 0 )? this.refVal2: this.refVal1;
    		return ( compareTo( minVal, obj) >= 0) && ( compareTo( maxVal, obj ) < 0 );

    	case IN_RANGE_EXCLUDE_MIN_MAX:
    		if(( this.refVal1 == null ) || ( this.refVal2 == null ))
    			return false;
    		minVal = ( compareTo( this.refVal1, this.refVal2 ) < 0 )? this.refVal1: this.refVal2;
    		maxVal = ( compareTo( this.refVal1, this.refVal2 ) <= 0 )? this.refVal2: this.refVal1;
    		return ( compareTo( minVal, obj) > 0) && ( compareTo( maxVal, obj ) < 0 );
      }
    return false;
  }

  /**
   * If true, the given rule is accepted by this filter
   *
   * @param rule String
   * @return boolean
  */
  public static boolean checkRule( String ruleName )
  {
  	Rules[] rules = Rules.values();
  	for( Rules rule: rules )
  		if( ruleName.equals( rule.name() ))
  			return true;
  	return false;
  }
}
