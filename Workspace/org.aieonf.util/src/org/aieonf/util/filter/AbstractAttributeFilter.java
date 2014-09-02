package org.aieonf.util.filter;

//Condast imports
import org.aieonf.util.StringStyler;
import org.aieonf.util.Utils;
import org.aieonf.util.logger.Logger;

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public abstract class AbstractAttributeFilter<T,U extends Object> extends AbstractFilter<T>
{
  //The supported rule set
  public enum Rules{
  	CONTAINS, 
  	CONTAINS_NOT, 
  	EQUALS, 
  	EQUALS_NOT, 
  	WILDCARD;

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
  public String refVal;

  private Logger logger;
  
  /**
   * Create the filter
   *
   * @throws FilterException
  */
  public AbstractAttributeFilter() throws FilterException
  {
    super.setName( AbstractAttributeFilter.class.getName() );
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @throws FilterException
  */
  public AbstractAttributeFilter( Rules rule ) throws FilterException
  {
    super( AbstractAttributeFilter.class.getName(), rule.name() );
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey String
   * @throws FilterException
  */
  public AbstractAttributeFilter( Rules rule, String refKey ) throws FilterException
  {
    super( AbstractAttributeFilter.class.getName(), rule.name() );
    this.refKey = refKey;
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey Attribute
   * @param refVal String
   * @throws FilterException
  */
  public AbstractAttributeFilter( Rules rule, String refKey, String refVal )
    throws FilterException
  {
    this( rule, refKey );
    this.refVal = refVal;
    logger = Logger.getLogger( this.getClass() );
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
  public AbstractAttributeFilter( Rules rule, String name, String refKey, String refVal )
    throws FilterException
  {
    this( rule, name, refKey );
    this.refVal = refVal;
    logger = Logger.getLogger( this.getClass() );
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
  public void setRefVal( String refVal )
  {
    this.refVal = refVal;
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
    return AbstractAttributeFilter.checkRule( rule );
  }

  protected abstract U getValue( Object obj );

  protected abstract String getValueAsString( Object obj );

  /**
   * If true, the given object is accepted when the 
   * filter is enabled
   * 
   * @param obj
   * @return
   * @throws FilterException
  */
  @Override
	protected boolean acceptEnabled( Object  obj ) throws FilterException
  {
    if( obj == null )
      return false;
    
    U value = getValue( obj );
    Rules rule = Rules.valueOf( super.getRule() );
    boolean contains = ( value != null );
    logger.trace( "The descriptor " + obj.toString() + " contains " + this.refKey + ": " + contains  + " " + value);
    
    switch( rule ){
    	case CONTAINS:
        return( contains );
    
    	case CONTAINS_NOT:
    		return( !contains );
    	
    	case EQUALS:
        if( contains )
          return( value.equals( this.refVal ));
        else
          return( this.refVal == null );

    	case EQUALS_NOT:
        logger.trace( "Accepting " + this.getRule() );
        logger.trace( "The descriptor contains " + this.refKey + ": " + contains  + " " + value);
        if( contains ){
          logger.trace( "Accepting " + this.refVal );
          return( value.equals( this.refVal ) == false );
        }
        else
          return( this.refVal != null );

    	case WILDCARD:
        String str = this.getValueAsString( this.refKey );
    	  if( Utils.isNull(str))
    		  return false;
        WildcardFilter filter = new WildcardFilter( this.refVal );
        return filter.accept( str );
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
