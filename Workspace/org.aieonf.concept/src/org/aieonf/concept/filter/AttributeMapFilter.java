package org.aieonf.concept.filter;

//J2SE
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aieonf.commons.filter.*;
import org.aieonf.concept.*;

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class AttributeMapFilter<T extends IDescriptor> extends AbstractFilter<T>
{
  //The supported rule set
  public enum Rules
  {
  	ContainsAll, 
  	ContainsNone, 
  	EqualsAll, 
  	EqualsNone, 
  	WildcardOr, 
  	WildcardAnd
  }

  /**
   * A map of attribute key-value pairs that are used as reference
  */
  public Map<String,String> refMap;

  //logger
  private Logger logger = Logger.getLogger( this.getClass().getName());

  /**
   * Create the filter
   *
   * @throws FilterException
  */
  public AttributeMapFilter() throws FilterException
  {
    super.setName( AttributeMapFilter.class.getName() );
  }

  /**
   * Create the filter
   *
   * @param rule String
   * @param attributes Map
   * @throws FilterException
  */
  public AttributeMapFilter( Rules rule, Map<String,String> attributes )
    throws FilterException
  {
    super( AttributeMapFilter.class.getName(), rule.name() );
    this.refMap = attributes;
  }

  /**
   * Create the filter
   *
   * @param name String
   * @param rule String
   * @param attributes Map
   * @throws FilterException
  */
  public AttributeMapFilter( Rules rule, String name , Map<String,String> attributes )
    throws FilterException
  {
    super( name, rule.name() );
    this.refMap = attributes;
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
    return AttributeMapFilter.checkRule( rule );
  }

  /**
   * Helper method to translate the rules of this filter to those of an
   * attribute filter
   *
   * @return String
  */
  protected AttributeFilter.Rules translateRule()
  {
    if( this.getRule().equals( AttributeMapFilter.Rules.ContainsAll.name() ))
      return( AttributeFilter.Rules.CONTAINS );
    if( this.getRule().equals( AttributeMapFilter.Rules.ContainsNone.name() ))
      return( AttributeFilter.Rules.CONTAINS_NOT );
    if( this.getRule().equals( AttributeMapFilter.Rules.EqualsAll.name() ))
      return( AttributeFilter.Rules.EQUALS );
    if( this.getRule().equals( AttributeMapFilter.Rules.EqualsNone.name() ))
      return( AttributeFilter.Rules.EQUALS_NOT );
    if( this.getRule().equals( AttributeMapFilter.Rules.WildcardAnd.name() ))
      return( AttributeFilter.Rules.WILDCARD );
    if( this.getRule().equals( AttributeMapFilter.Rules.WildcardOr.name() ))
      return( AttributeFilter.Rules.WILDCARD );
    return null;
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
	protected boolean acceptEnabled( T descriptor ) throws FilterException
  {
    if( descriptor == null )
    	return false;
    
  	logger.log( Level.FINE, "Testing Descriptor: " + descriptor.toString() );

    Set<Map.Entry<String,String>> set = refMap.entrySet();
    Iterator<Map.Entry<String,String>> iterator = set.iterator();
    AttributeFilter<T> filter = new AttributeFilter<T>( this.translateRule() );
    Map.Entry<String,String> map;
    String key, value;
    while( iterator.hasNext() ){
      map = iterator.next();
      key = map.getKey();
      value = map.getValue();
      filter.setRefKey( key );
      filter.setValue( value );
      if( filter.accept( descriptor ) == false ){
        if( this.getRule().equals( AttributeMapFilter.Rules.WildcardAnd.name() ) == true )
          continue;
        logger.log( Level.FINE, "Descriptor not accepted: " + descriptor.toString() + ", key: " + key + "-"  + value );
        return false;
      }
      if( this.getRule().equals( AttributeMapFilter.Rules.WildcardOr.name() ) == true ){
        logger.log( Level.FINE, "Descriptor not accepted: " + descriptor.toString() + ", accepted" );
        return true;
      }
    }
    logger.log( Level.FINE, "Descriptor not accepted: " + descriptor.toString() + ", not accepted" );
    return ( this.getRule().equals( AttributeMapFilter.Rules.WildcardAnd.name() ) == false );
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
