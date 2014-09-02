package org.aieonf.concept.filter;

//J2SE
import java.util.*;

import org.aieonf.concept.*;
import org.aieonf.util.filter.*;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p> Filters objects who are relationships of the given concept
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class ConceptRelationFilter<T extends IDescriptor> extends AbstractFilter<T>
{
  //The supported rule set
  public enum Rules{
  	Contains, 
  	ContainsNot
  }

  //The concept that is used as base
  private IFixedConcept concept;

  /**
   * Create a concept relation filter
   * @throws FilterException
  */
  public ConceptRelationFilter()
  	throws FilterException
  {
  	super.setName( ConceptRelationFilter.class.getName() );
  }

  public ConceptRelationFilter( String rule, IFixedConcept concept )
    throws FilterException
  {
    super( ConceptRelationFilter.class.getName(), rule  );
    this.concept = concept;
  }

  public ConceptRelationFilter( String name, String rule, IFixedConcept concept )
    throws FilterException
  {
    super( name, rule );
    this.concept = concept;
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
	protected boolean acceptEnabled( Object descriptor ) throws FilterException
  {
    List<IRelationship> relations = concept.getRelationships();

    if( this.getRule().equals( AttributeFilter.Rules.Contains.name() )){
      return( relations.contains( descriptor ));
    }
    if( this.getRule().equals( AttributeFilter.Rules.ContainsNot.name() )){
      return( !relations.contains( descriptor ));
    }
    return false;
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
   * acceptRule
   *
   * @param string String
   * @return boolean
  */
  @Override
	protected boolean acceptRule( String rule )
  {
    return ConceptRelationFilter.checkRule( rule );
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
