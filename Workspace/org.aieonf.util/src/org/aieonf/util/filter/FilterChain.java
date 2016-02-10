package org.aieonf.util.filter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Title: Util</p>
 *
 * <p>Description: Create a filter chain of filters.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast B.V</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class FilterChain<T> extends AbstractFilter<T>
{
  //Supported chain rules
  public enum Rules
  { 
  	AndChain, 
  	OrChain
  }

  /**
   * Create the filter chain
   */
  private Collection<IFilter<T>> filterChain;

  /**
   * Create the filter chain
   *
   * @throws FilterException
  */
  public FilterChain() throws FilterException
  {
    super.setName( FilterChain.class.getName() );
    this.filterChain = new ArrayList<IFilter<T>>();
  }

  /**
   * Create the filter chain
   *
   * @param chainRule String
   * @throws FilterException
  */
  public FilterChain( Rules chainRule ) throws FilterException
  {
    super( FilterChain.class.getName(), chainRule.name() );
    this.filterChain = new ArrayList<IFilter<T>>();
  }

  /**
   * Perform a logical AND on the filter chain. This means traversing all
   * the filters one by one and providing the result from one one filter
   * to the next in the chain;
   *
   * @param list List
   * @return List
   * @throws FilterException
  */
  private Collection<T> andFilter( Collection<T> list ) throws FilterException
  {
  	Collection<T> results = list;
    for( IFilter<T> filter: this.filterChain ){
    	if( results.size() == 0 )
    		break;
    	results = filter.doFilter( results );
    }
    return results;
  }

  /**
   * Perform a logical OR on the filter chain. This means traversing all
   * the filters one by one adding all the elements that pass the filter
   * to a result set
   *
   * @param list List
   * @return List
   * @throws FilterException
  */
  private Collection<T> orFilter( Collection<T> list ) throws FilterException
  {
    Collection<T> results = new ArrayList<T>();
    Collection<T> tempList; //temporary list
    for( IFilter<T> filter: this.filterChain ){
      tempList = filter.doFilter( list );
      for( T t: tempList ){
        if( results.contains( t ) == false )
          results.add( t );
      }
    }
    return results;
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
    return FilterChain.checkRule( rule );
  }

  /**
   * Add a filter to the chain
   *
   * @param filter Filter
   */
  public void addFilter( IFilter<T> filter )
  {
    this.filterChain.add( filter );
  }

  /**
   * Remove a filter from the chain
   *
   * @param filter Filter
  */
  public void removeFilter( IFilter<T> filter )
  {
    this.filterChain.remove( filter );
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
	@SuppressWarnings("unchecked")
	protected boolean acceptEnabled( Object obj ) throws FilterException
  {
  	Collection<T> list = new ArrayList<T>();
    try{
  	  list.add(( T )obj );
    }
    catch( ClassCastException ex ){
    	return false;
    }
    Collection<T> result = this.doFilter( list );
    return (( result != null ) && ( result.size() > 0 ));
  }

  /**
   * perform a filter chain
   *
   * @param list List
   * @return List
   * @throws FilterException
  */
  @Override
  public Collection<T> doFilter( Collection<T> list )  throws FilterException
  {
  	super.prepareFilter();
  	Collection<T> results = list;
    switch( super.getMode() ){
    	case Disabled:
    		results.addAll( list );
    		break;
    	case Block:
    		break;
    	case Enabled:
    		if( this.getRule().equals( Rules.AndChain.name() )){
    			results =  this.andFilter( list );
    			break;
    		}
    		if( this.getRule().equals( Rules.OrChain.name() )){
    			results = this.orFilter( list );
    			break;
    		}
    		throw new FilterException( FilterException.S_CHAIN_RULE_NOT_SUPPORTED_MSG +
    				this.getRule() );
    }
    return results;
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

	@Override
	public Collection<T> getRejected()
	{
    Collection<T> results = new ArrayList<T>();
    Collection<T> rejected, temp;
    for( IFilter<T> filter: this.filterChain ){
    	rejected = filter.getRejected();
      if( results.isEmpty() && !rejected.isEmpty() ){
      	results.addAll( filter.getRejected() );
      	continue;
      }
      temp = new ArrayList<T>();
      for( T reject: rejected ){
      	if( results.contains( reject ))
      		temp.add( reject );
      }
      results = temp;
    }
    return results;
	}
}
