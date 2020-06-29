package org.aieonf.concept.filter;

import org.aieonf.commons.filter.*;
//Concept imports
import org.aieonf.concept.*;
import org.aieonf.concept.core.ConceptException;
import org.aieonf.concept.core.Descriptor;

//Condast imports

/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class DescriptorFilter<T extends IDescribable> extends AbstractFilter<T>
{
	//The supported rule set
	public enum Rules{
		Equals, 
		VersionEquals, 
		IDEquals, 
		NameEquals, 
		DescriptorAndVersionEquals,
		VersionEqualOrLarger, 
		VersionLarger, 
		VersionEqualOrSmaller, 
		VersionSmaller
	}

	/**
	 * The descriptor used as reference
	 */
	public IDescribable refDescriptor;

	//If true, then the match is not case sensitive
	public boolean caseInsensitive;

	/**
	 * Create the filter
	 *
	 * @param rule String
	 * @param descriptor IDescriptor
	 * @throws FilterException
	 */
	public DescriptorFilter() throws FilterException
	{
		super.setName( DescriptorFilter.class.getName() );
		this.caseInsensitive = false;
	}

	/**
	 * Create the filter
	 *
	 * @param rule String
	 * @param descriptor IDescriptor
	 * @throws FilterException, ConceptException
	 */
	public DescriptorFilter( Rules rule, IDescribable descriptor )
			throws FilterException{
		this( rule, descriptor, false );
	}

	/**
	 * Create the filter
	 *
	 * @param rule String
	 * @param descriptor IDescriptor
	 * @throws FilterException, ConceptException
	 */
	public DescriptorFilter( Rules rule, IDescribable descriptor, boolean caseInsensitive )
			throws FilterException
	{
		super( DescriptorFilter.class.getName(), rule.name() );
		this.refDescriptor = descriptor;
		this.caseInsensitive = caseInsensitive;
		if( this.caseInsensitive ){
			try {
				if( descriptor instanceof IConcept ){
					this.refDescriptor = this.caseInsensitiveDescriptor( descriptor );
				}
			}
			catch (ConceptException e) {
				throw new FilterException( e );
			}
		}
	}

	/**
	 * Create the filter
	 *
	 * @param name String
	 * @param rule String
	 * @param descriptor IDescriptor
	 * @throws FilterException, ConceptException
	 */
	public DescriptorFilter( String name, Rules rule, IDescribable descriptor, boolean caseInsensitive )
			throws FilterException, ConceptException
	{
		super( name, rule.name() );
		this.caseInsensitive = caseInsensitive;
		if( this.caseInsensitive )
			this.refDescriptor = this.caseInsensitiveDescriptor( descriptor );
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
	 * Create a case insensitive descriptor
	 * @param descriptor
	 * @return
	 * @throws ConceptException
	 */
	protected IDescribable caseInsensitiveDescriptor( IDescribable descriptor )
			throws ConceptException
	{
		if( this.caseInsensitive == false )
			return descriptor;
		long id = descriptor.getDescriptor().getID();
		String name = descriptor.getDescriptor().getName();
		if( name != null )
			name = name.trim().toLowerCase();
		IDescriptor newDescriptor = new Descriptor( id );
		newDescriptor.setVersion( descriptor.getDescriptor().getVersion() );
		return newDescriptor;
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
		return DescriptorFilter.checkRule( rule );
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
	protected boolean acceptEnabled( T obj ) throws FilterException
	{
		if(!( obj instanceof IDescribable ))
			return false;    
		T desc = ( T )obj;
		IDescribable descriptor = desc.getDescriptor();
		try{
			if( this.caseInsensitive )
				descriptor = this.caseInsensitiveDescriptor( desc );
		}
		catch( ConceptException ex ){
			throw new FilterException( ex.getMessage(), ex );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.Equals.name() )){
			return( refDescriptor.equals( descriptor ));
		}

		if( this.getRule().equals( DescriptorFilter.Rules.DescriptorAndVersionEquals.name() )){
			return(( refDescriptor.equals( descriptor )) &&
					( refDescriptor.getDescriptor().getVersion() == descriptor.getDescriptor().getVersion() ));
		}
		if( this.getRule().equals( DescriptorFilter.Rules.IDEquals.name() )){
			return( refDescriptor.getDescriptor().getID() == descriptor.getDescriptor().getID() );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.NameEquals.name() )){
			return( refDescriptor.getDescriptor().getName().equals( descriptor.getDescriptor().getName() ));
		}
		if( this.getRule().equals( DescriptorFilter.Rules.VersionEquals.name() )){
			return( refDescriptor.getDescriptor().getVersion() == descriptor.getDescriptor().getVersion() );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.VersionEqualOrLarger.name())){
			return( refDescriptor.getDescriptor().getVersion() >= descriptor.getDescriptor().getVersion() );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.VersionLarger.name() )){
			return( refDescriptor.getDescriptor().getVersion() > descriptor.getDescriptor().getVersion() );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.VersionEqualOrSmaller.name() )){
			return( refDescriptor.getDescriptor().getVersion() <= descriptor.getDescriptor().getVersion() );
		}
		if( this.getRule().equals( DescriptorFilter.Rules.VersionSmaller.name() )){
			return( refDescriptor.getDescriptor().getVersion() < descriptor.getDescriptor().getVersion() );
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
