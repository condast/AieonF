package org.aieonf.concept.filter;

import org.aieonf.concept.*;
import org.aieonf.concept.filter.AttributeMapFilter.Rules;
import org.aieonf.util.filter.*;
import org.aieonf.util.logger.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class VersionFilter<T extends IDescribable<?>> extends AbstractFilter<T>
{
  //Additional
  //The options for a strict get
  public enum Compare{
	  ALL,
	  EXACT,
	  EQUAL_GREATER,
	  EQUAL_SMALLER,
	  GREATER,
	  SMALLER;
  }
  private String version;

  //private Logger logger;

  /**
   * Create a version filter for the given reference version
   * @throws FilterException
   */
  public VersionFilter() throws FilterException
  {
    super.setName( VersionFilter.class.getName() );
    //this.logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create a version filter for the given reference version
   * @param vc VersionFilter.Compare
   * @throws FilterException
   */
  public VersionFilter( VersionFilter.Compare vc ) throws FilterException
  {
    super( VersionFilter.class.getName(), vc.name() );
    //this.logger = Logger.getLogger( this.getClass() );
  }

  /**
   * Create a version filter for the given reference version
   * @param version String
   * @param vc VersionFilter.Compare
   * @throws FilterException
   */
  public VersionFilter( String version, VersionFilter.Compare vc ) throws FilterException
  {
    this( vc );
    this.version = version;
  }

  /**
   * Create a version filter for the given reference version
   *
   * @param version int
   * @param vc Compare
   * @throws FilterException
  */
  public VersionFilter( int version, VersionFilter.Compare vc ) throws FilterException
  {
    this( String.valueOf( version ), vc );
  }

  public VersionFilter( String name, String version, VersionFilter.Compare vc ) throws FilterException
  {
    super( name, vc.name() );
    //logger = Logger.getLogger( this.getClass() );
    this.version = version;
  }

	/**
   * accept the given string depending on the provided rule. Returns true if the
   * object is accepted, otherwise return false
   *
   * @param checkVersion String
   * @return boolean
   * @throws FilterException
  */
  public boolean accept( String checkVersion ) throws FilterException
  {
    VersionFilter.Compare vc = VersionFilter.Compare.valueOf( super.getRule() );
    switch( vc ){
      case ALL:
        return true;
      case EQUAL_GREATER:
        return( checkVersion.compareTo( version ) >= 0 );
      case EQUAL_SMALLER:
        return( checkVersion.compareTo( version ) <= 0 );
      case EXACT:
        return( checkVersion.compareTo( version ) == 0 );
      case GREATER:
        return( checkVersion.compareTo( version ) > 0 );
      case SMALLER:
        return( checkVersion.compareTo( version ) < 0 );
    }
    return false;
  }

  /**
   * accept the given object depending on the provided rule. Returns true if the
   * object is accepted, otherwise return false
   * 
   * @param obj
   * @return
   * @throws FilterException
  */
  @Override
	protected boolean acceptEnabled( Object obj ) throws FilterException
  {
    if(!( obj instanceof IDescribable ))
    	return false;    
    IDescribable<?> desc = ( IDescribable<?> )obj;
  	return this.accept( desc.getDescriptor().get( IDescriptor.Attributes.VERSION.name() ));
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
   * Returns true if the given rule is applicable for this filter
   *
   * @param rule String
   * @return boolean
  */
  @Override
	protected boolean acceptRule( String rule )
  {
    Logger logger = Logger.getLogger( this.getClass() );
    logger.trace( "Checking rule: " + rule );
    try{
      Compare.valueOf( rule );
      return true;
    }
    catch( IllegalStateException ex ){
        logger.trace( "Rule not accepted: " + ex.getMessage() );
    	return false;
    }
    catch( Exception ex ){
      logger.trace( "Rule not accepted: " + ex.getMessage() );
      return false;
    }
  }
}
