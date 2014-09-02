package org.aieonf.concept.filter;

//Condast imports
import org.aieonf.concept.*;
import org.aieonf.util.filter.*;
import org.aieonf.util.logger.Logger;

//Concept imports

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class AttributeFilter<T extends IDescribable<?>> extends AbstractFilter<T>
{
  //The supported rule set
  public enum Rules{
  	Contains, 
  	ContainsNot, 
  	Equals, 
  	EqualsNot, 
  	Wildcard
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
  public AttributeFilter() throws FilterException
  {
    super.setName( AttributeFilter.class.getName() );
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @throws FilterException
  */
  public AttributeFilter( Rules rule ) throws FilterException
  {
    super( AttributeFilter.class.getName(), rule.name() );
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey String
   * @throws FilterException
  */
  public AttributeFilter( Rules rule, String refKey ) throws FilterException
  {
    super( AttributeFilter.class.getName(), rule.name() );
    this.refKey = refKey;
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param refKey String
   * @throws FilterException
  */
  public AttributeFilter( Rules rule, IDescriptor.Attributes refKey ) throws FilterException
  {
    this( rule, refKey.toString() );
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
  public AttributeFilter( Rules rule, String refKey, String refVal )
    throws FilterException
  {
    this( rule, refKey );
    this.refVal = refVal;
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
  public AttributeFilter( Rules rule, IDescriptor.Attributes refKey, String refVal )
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
  public AttributeFilter( Rules rule, String name, String refKey, String refVal )
    throws FilterException
  {
    this( rule, name, refKey );
    this.refVal = refVal;
    logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create the filter
   *
   * @param name String
   * @param rule String
   * @param refKey Attribute
   * @param refVal String
   * @throws FilterException
  */
  public AttributeFilter( Rules rule, String name, IDescriptor.Attributes refKey, String refVal )
    throws FilterException
  {
    this( rule, name, refKey.toString() );
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
    return AttributeFilter.checkRule( rule );
  }

  /**
   * If true, the given object is accepted when the 
   * filter is enabled
   * 
   * @param obj
   * @return
   * @throws FilterException
  */
  @Override
	protected boolean acceptEnabled( Object obj ) throws FilterException
  {
    if( obj == null )
      return false;
    
    if(!( obj instanceof IDescribable ))
    	return false;    
    IDescribable<?> desc = ( IDescribable<?> )obj;
    String value = desc.getDescriptor().get( this.refKey );
    boolean contains = ( value != null );
    logger.trace( "The descriptor " + obj.toString() + " contains " + this.refKey + ": " + contains  + " " + value);
    switch( Rules.valueOf( super.getRule() )){
    	case Contains:
    		return contains;
    
    	case ContainsNot:
    		return !contains;

      //return true if the attribute is present and equal, or it is not present
      //and the reference is null
    	case Equals:
    		if( contains )
    			return( value.equals( this.refVal ));
    		else
    			return( this.refVal == null );
    	

      //return true if the attribute is present and not equal, or it is not present
      //and the reference is null
    	case EqualsNot:
    		logger.trace( "Accepting " + this.getRule() );
    		logger.trace( "The descriptor contains " + this.refKey + ": " + contains  + " " + value);
    		if( contains ){
    			logger.trace( "Accepting " + this.refVal );
    			return( value.equals( this.refVal ) == false );
    		}
    		else
    			return( this.refVal != null );

    	default:
    		//return true if the attribute accepts the wildcard
        //and the reference is null
        if( desc.getDescriptor().get( this.refKey ) == null )
          return false;
        WildcardFilter filter = new WildcardFilter( this.refVal );
        return filter.accept( desc.getDescriptor().get( this.refKey ));
      }
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
