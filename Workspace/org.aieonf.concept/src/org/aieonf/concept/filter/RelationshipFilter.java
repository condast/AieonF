package org.aieonf.concept.filter;

//Condast imports
import java.util.List;

import org.aieonf.concept.*;
import org.aieonf.util.filter.*;

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class RelationshipFilter<T extends IDescriptor> extends AbstractFilter<T>
{
  public AbstractFilter<IDescriptor> filter;

  /**
   * Create the relationship filter. Passes all concepts who have a
   * relationship that pass the given filter
   *
   * @throws FilterException
  */
  public RelationshipFilter( AbstractFilter<IDescriptor> filter ) throws FilterException
  {
    super.setName( RelationshipFilter.class.getName() );
    this.filter = filter;
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
  	return this.filter.getRules();
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
    return true;
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
    if( descriptor == null )
      return false;

    if(( descriptor instanceof IFixedConcept ) == false )
    	return false;
    
    IFixedConcept concept = ( IFixedConcept )descriptor;
    try{
    	List<IRelationship> relationships = concept.getRelationships();
    
    	if(( relationships == null ) || ( relationships.size() == 0 ))
    		return false;
    
    	for( IRelationship relationship: relationships ){
    		if( this.filter.accept(relationship ))
    			return true;
    	}
    	return false;
    }
    catch( Exception ex){
    	throw new FilterException( ex.getMessage(), ex );
    }
  }
}
