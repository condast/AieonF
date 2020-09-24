/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

import java.util.*;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.*;
import org.aieonf.concept.body.BodyFactory;

/**
 * Create a minimal concept, using a properties file
 */
public class Concept extends Descriptor implements IConcept
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7987983663448269749L;

	//Supported error messages
	public static final String S_ERR_INVALID_CLASS =
			"The assigned class for this transformation is not a concept";
	public static final String S_ERR_INVALID_NUMBER =
			"The provided entry number is not valid";

	/**
	 * Create a minimal concept
	 */
	public Concept()
	{
		super();
	}

	/**
	 * Create a minimal concept
	 */
	protected Concept( IConceptBase base ){
		super( base );
	}

	/**
	 * Create a minimal concept with the given name
	 * @param name String
	 */
	public Concept( String name )
	{
		this();
		this.setName( name );
	}

	/**
	 * Create a minimal concept with the given name
	 * @param name String
	 */
	public Concept( IDescriptor descriptor ){
		this( descriptor.getBase());
	}

	/**
	 * Create a minimal concept with the given name
	 *
	 * @param id String
	 * @param name String
	 */
	public Concept( long id, String name )
	{
		super( id, name );
	}


	/* (non-Javadoc)
	 * @see org.condast.concept.core.Descriptor#fill()
	 */
	@Override
	public void fill()
	{
		this.setScope( Scope.PUBLIC );
		super.fill();
	}

	/**
	 * Set the id of the concept. Is only possible in this package
	 *
	 * @param id String
	 */
	final void setID( String id )
	{
		super.set( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Set the name of the concept
	 *
	 * @param name String
	 */
	protected void setName( String name )
	{
		name = name.trim();
		if( this.checkName( name) == false )
			throw new IllegalArgumentException( S_ERR_INVALID_NAME + ": " + name );
		super.set( IDescriptor.Attributes.NAME, name );
	}
	
	@Override
	public String getSource() {
		return this.get( IConcept.Attributes.SOURCE );
	}

	/**
	 * Set the name of the concept
	 *
	 * @param name String
	 */
	protected void setSource( String source )
	{
		super.set( IConcept.Attributes.SOURCE, source );
	}

	@Override
	public String getURIPath() {
		return super.get( IConcept.Attributes.URI.name() );
	}

	/**
	 * Get the scope of the concept (default public)
	 *
	 * @return int
	 */
	@Override
	public final Scope getScope()
	{
		String scopeStr = super.get( IConcept.Attributes.SCOPE );
		if(( scopeStr == null ) || ( scopeStr == "" ))
			return Scope.UNKNOWN;
		return Scope.valueOf( StringStyler.styleToEnum( scopeStr ));
	}

	/**
	 * Set the scope of the concept (default public)
	 *
	 * @param scope int
	 */
	@Override
	public final void setScope( Scope scope )
	{
		super.set( IConcept.Attributes.SCOPE, scope.toString());
	}

	/**
	 * Returns true if the concept is readonly
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isReadOnly()
	{
		String readOnly = super.get( IConcept.Attributes.READ_ONLY );
		return ( readOnly == null )? false: readOnly.toLowerCase().equals( "true" );
	}

	/**
	 * Get or set the readonly option
	 *
	 * @param readonly boolean
	 */
	@Override
	public final void setReadOnly( boolean readonly )
	{
		super.set( IConcept.Attributes.READ_ONLY, String.valueOf( readonly ));
	}

	/**
	 * Returns true if the concept is hidden
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isHidden()
	{
		String result = super.get( IConcept.Attributes.HIDDEN );
		return  ( result == null )? false: result.toLowerCase().trim().equals( Boolean.TRUE );
	}

	/**
	 * Get or set the hidden option
	 *
	 * @param hidden boolean
	 */
	@Override
	public final void setHidden( boolean hidden )
	{
		super.set( IConcept.Attributes.HIDDEN, String.valueOf( hidden ));
	}

	@Override
	public String get(Enum<?> enm) {
		return super.getBase().get(enm);
	}

	@Override
	public void set(Enum<?> enm, String value) {
		super.getBase().set(enm, value);
	}

	/**
	 * The default behaviour returns true;
	 *
	 * @param descriptor IConcept
	 * @return boolean
	 */
	@Override
	public boolean isA( IDescriptor descriptor )
	{
		return true;
	}

	/**
	 * Returns true if the given concept contains the given key
	 *
	 * @param key String
	 * @return boolean
	 */
	@Override
	public boolean hasA( String key )
	{
		return( this.get( key ) != null );
	}

	/**
	 * Clones the concept
	 *
	 * @return Object
	 */
	@Override
	public Object clone()
	{
		try{
			Concept clone = this.getClass().newInstance();
			BodyFactory.transfer( clone, this, true );
			return clone;
		}
		catch( Exception e ){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the difference between the given concept and a minimal concept
	 *
	 * @param desc Descriptor
	 * @return Properties
	 * @throws ConceptException
	 */
	public static Properties getDifference( Descriptor desc ) throws ConceptException
	{
		Concept mc = new Concept();
		return Descriptor.getDifference( mc, desc );
	}
}
