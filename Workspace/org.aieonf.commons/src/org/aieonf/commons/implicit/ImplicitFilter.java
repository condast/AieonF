package org.aieonf.commons.implicit;

import org.aieonf.commons.filter.*;
import org.aieonf.commons.strings.StringStyler;

public class ImplicitFilter<T extends Object> extends AbstractFilter<T>
{

	//Supported rules
	public enum Rules
	{
		IMPLIES,
		DOES_NOT_IMPLY;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static String[] names(){
			String[] names = new String[ values().length ];
			for( int i=0; i< values().length; i++ )
				names[i] = values()[i].toString();
			return names;
		}

		/**
		 * If true, the given rule is accepted by this filter
		 *
		 * @param rule String
		 * @return boolean
		 */
		public static boolean isValid( String ruleName )
		{
			Rules[] rules = Rules.values();
			for( Rules rule: rules )
				if( rule.toString().equals( ruleName ))
					return true;
			return false;
		}

	}

	private IImplicit<T> implicit;

	/**
	 * Create a default implicit filter
	 * @throws FilterException
	 */
	public ImplicitFilter( IImplicit<T> implicit ) throws FilterException 
	{
		this( Rules.IMPLIES, implicit );
	}

	/**
	 * Create an implicit filter for the given aieon
	 * @param rule Rule
	 * @param implicit
	 * @throws FilterException
	 */
	public ImplicitFilter( Rules rule, IImplicit<T> implicit ) throws FilterException 
	{
		super( ImplicitFilter.class.getName(), rule.toString() );
		this.implicit = implicit;
	}

	/**
	 * Returns true if the object is implied by the given implicit descriptor
	 * 
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
		Rules rule = Rules.valueOf( StringStyler.styleToEnum( super.getRule() ));
		switch( rule ){
		case IMPLIES:
			return implicit.test( descriptor );
		case DOES_NOT_IMPLY:
			return !implicit.test( descriptor );
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
	 * Returns true if the given rule is valid for this filter
	 */
	@Override
	protected boolean acceptRule( String rule )
	{
		return Rules.isValid( rule );
	}
}
