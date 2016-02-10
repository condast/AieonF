package org.aieonf.util.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

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
public abstract class AbstractFilter<T> implements IFilter<T>
{
	/**
	 * Define the various modes of operation:
	 * Disabled: allow all objects
	 * Enabled: filter objects
	 * Blocked: block all objects
	 * @author Kees
	 *
	 */
	public enum Mode
	{
		Disabled,
		Enabled,
		Block
	}

	/**
	 * The name of the filter
	 */
	private String name;

	/**
	 * A filter also contains a rule, which is a concise description
	 * of what the filter does
	 */
	private String rule;

	//The reject-list contains concepts that were not accepted during
	//a doFilter operation
	private Collection<T> rejectList;

	//Determine the mode of operation
	private Mode mode;

	private int amount;

	/**
	 * Create a filter for the given rule
	 *
	 */
	public AbstractFilter()
	{
		this.rejectList = new ArrayList<T>();
		this.name = AbstractFilter.class.getName();
		this.mode = Mode.Enabled;
		this.amount = -1;
	}

	/**
	 * Create a filter for the given rule
	 *
	 * @param rule String
	 */
	public AbstractFilter( String rule )
	{
		this();
		this.setRule( rule );
	}

	/**
	 * Create the filter
	 *
	 * @param name String
	 * @param rule String
	 */
	public AbstractFilter( String name, String rule )
	{
		this( rule );
		this.name = name;
	}

	/**
	 * Create the filter
	 *
	 * @param name String
	 * @param rule String
	 */
	public AbstractFilter( String name, String rule, int amount )
	{
		this( rule );
		this.name = name;
		this.amount = amount;
	}

	/**
	 * Get the name of the filter
	 * @return String
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the name of this filter
	 * @param name
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * Get the rule of the filter
	 * @return String
	 */
	@Override
	public String getRule()
	{
		return this.rule;
	}

	/**
	 * Get the mode of operation
	 * @return
	 */
	@Override
	public Mode getMode()
	{
		return this.mode;
	}

	/**
	 * Set the mode of operation
	 * @param mode
	 */
	public void setMode( Mode mode )
	{
		this.mode = mode;
	}


	/**
	 * @return the amount
	 */
	@Override
	public final int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	@Override
	public final void setAmount(int amount)
	{
		this.amount = amount;
	}

	/**
	 * Set the rule of this application. Is always done in the constructor
	 * to allow checking
	 * @param rule String
	 */
	public void setRule( String rule )
	{
		if( !this.acceptRule( rule ))
			throw new FilterException( this.getName(), rule,
					FilterException.S_RULE_NOT_SUPPORTED_MSG );
		this.rule = rule;
	}

	/**
	 * If true, the given rule is accepted by this filter
	 *
	 * @param rule String
	 * @return boolean
	 */
	protected boolean acceptRule( String rule ){
		for( String str: this.getRules() ){
			if( str.equals( rule ))
				return true;
		}
		return false;
	}

	/**
	 * Get a list of accepted rules
	 *
	 * return rule String[]
	 * @return boolean
	 */
	protected abstract String[] getRules();

	/**
	 * If true, the given object is accepted when the 
	 * filter is enabled
	 * 
	 * @param obj
	 * @return
	 * @throws FilterException
	 */
	protected abstract boolean acceptEnabled( Object obj ) throws FilterException;

	/**
	 * This empty method can be overridden to prepare a filter prior to a doFilter operation
	 */
	protected void prepareFilter(){};

	/**
	 * Returns true if the given object has passed the filter
	 *
	 * @param obj Object
	 * @return boolean
	 * @throws FilterException
	 */
	@Override
	public synchronized final boolean accept( Object obj ) throws FilterException
	{
		switch( this.mode )
		{
		case Disabled:
			return true;
		case Block:
			return false;
		case Enabled:
			return this.acceptEnabled( obj );
		}
		return false;
	}

	/**
	 * Returns a filtered list of objects provided by the input list
	 *
	 * @param list List
	 * @return List
	 * @throws FilterException
	 */
	@Override
	public Collection<T> doFilter( Collection<T> list ) throws FilterException
	{
		return doFilter( list, amount );
	}

	/**
	 * Returns a filtered list of objects provided by the input list
	 *
	 * @param list List
	 * @param int amount
	 * @return List
	 * @throws FilterException
	 */
	@Override
	public Collection<T> doFilter( Collection<T> list, int amount ) throws FilterException
	{
		this.prepareFilter();
		Collection<T> results = new ArrayList<T>();

		this.rejectList.clear();
		Iterator<T> iterator = list.iterator();
		T value;
		while( iterator.hasNext() ){
			value = iterator.next();
			if( this.accept( value )){
				results.add(  value );
				if(( amount > 0 ) && ( results.size() == amount ))
					break;
			}

			else
				this.rejectList.add( value );
		}
		return results;
	}

	/**
	 * Get an enumeration of items that are rejected by the doFilter
	 * operation. This list will be retained until a new doFilter operation
	 * is carried out. It is initially empty
	 *
	 * @return List<T>
	 */
	@Override
	public Collection<T> getRejected()
	{
		return this.rejectList;
	}

	/**
	 * Get the rules based on the given enumeration. One value of the enum must be passed in
	 * order to parse the full enum
	 * @param enm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] getRules( Enum<?> clss )
	{
		Set<Enum<?>> set = EnumSet.allOf( clss.getClass() );
		String[] str = new String[ set.size() ];
		int i=0;
		for( Enum<?> obj: set)
			str[i++] = obj.name();
		return str;
	}

}
