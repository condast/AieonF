package org.aieonf.concept.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aieonf.commons.filter.*;
//Condast imports
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptBase;

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
public class AttributeFilter<D extends IDescribable> extends AbstractFilter<D>
{
	//The supported rule set
	public enum Rules{
		CONTAINS, 
		CONTAINS_NOT, 
		EQUALS, 
		EQUALS_NOT, 
		WILDCARD
	}

	/**
	 * The attribute key and value that is used as reference
	 */
	public String refKey;
	public String refVal;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	/**
	 * Create the filter
	 *
	 * @throws FilterException
	 */
	public AttributeFilter() throws FilterException
	{
		super.setName( AttributeFilter.class.getName() );
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
	}

	/**
	 * Create the filter
	 *
	 * @param rule String
	 * @param refKey String
	 * @throws FilterException
	 */
	public AttributeFilter( Rules rule, Enum<?> refKey ) throws FilterException
	{
		this( rule, ConceptBase.getAttributeKey( refKey ));
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
	}

	/**
	 * Create the filter
	 *
	 * @param rule String
	 * @param refKey Attribute
	 * @param refVal String
	 * @throws FilterException
	 */
	public AttributeFilter( Rules rule, Enum<?> refKey, String refVal )
			throws FilterException
	{
		this( rule, refKey );
		this.refVal = refVal;
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
	public AttributeFilter( Rules rule, String name, Enum<?> refKey, String refVal )
			throws FilterException
	{
		this( rule, name, refKey.toString() );
		this.refVal = refVal;
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
	protected boolean acceptEnabled( D obj ) throws FilterException
	{
		if( obj == null )
			return false;

		if(!( obj instanceof IDescribable ))
			return false;    
		IDescribable desc = ( IDescribable )obj;
		String value = desc.getDescriptor().get( this.refKey );
		boolean contains = ( value != null );
		logger.log( Level.FINE, "The descriptor " + obj.toString() + " contains " + this.refKey + ": " + contains  + " " + value);
		switch( Rules.valueOf( super.getRule() )){
		case CONTAINS:
			return contains;

		case CONTAINS_NOT:
			return !contains;

			//return true if the attribute is present and equal, or it is not present
			//and the reference is null
		case EQUALS:
			if( contains )
				return( value.equals( this.refVal ));
			else
				return( this.refVal == null );


			//return true if the attribute is present and not equal, or it is not present
			//and the reference is null
		case EQUALS_NOT:
			logger.log( Level.FINE, "Accepting " + this.getRule() );
			logger.log( Level.FINE, "The descriptor contains " + this.refKey + ": " + contains  + " " + value);
			if( contains ){
				logger.log( Level.FINE, "Accepting " + this.refVal );
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
			return filter.accept( value );
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
