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
		setScope( Scope.PRIVATE);
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
	 * Set the name of the concept
	 *
	 * @param name String
	 */
	protected void setName( String name )
	{
		name = name.trim();
		if( this.checkName( name) == false )
			throw new IllegalArgumentException( S_ERR_INVALID_NAME + ": " + name );
		super.setValue( IDescriptor.Attributes.NAME, name );
	}
	
	@Override
	public String getSource() {
		return this.getValue( IConcept.Attributes.SOURCE );
	}

	/**
	 * Set the name of the concept
	 *
	 * @param name String
	 */
	protected void setSource( String source )
	{
		setValue( IConcept.Attributes.SOURCE, source );
	}

	@Override
	public String getURIPath() {
		return getValue( IConcept.Attributes.URI );
	}

	/**
	 * Get the scope of the concept (default public)
	 *
	 * @return int
	 */
	@Override
	public final Scope getScope()
	{
		String scopeStr = getValue( IConcept.Attributes.SCOPE );
		if(( scopeStr == null ) || ( scopeStr == "" ))
			return Scope.PRIVATE;
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
		setValue( IConcept.Attributes.SCOPE, scope.name());
	}

	/**
	 * Returns true if the concept is readonly
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isReadOnly()
	{
		String readOnly = getValue( IConcept.Attributes.READ_ONLY );
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
		setValue( IConcept.Attributes.READ_ONLY, String.valueOf( readonly ));
	}

	/**
	 * Returns true if the concept is hidden
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isHidden()
	{
		String result = getValue( IConcept.Attributes.HIDDEN );
		return  ( result == null )? false: result.toUpperCase().trim().equals( Boolean.TRUE.toString() );
	}

	/**
	 * Get or set the hidden option
	 *
	 * @param hidden boolean
	 */
	@Override
	public final void setHidden( boolean hidden )
	{
		setValue( IConcept.Attributes.HIDDEN, String.valueOf( hidden ));
	}

	protected String getValue( IConcept.Attributes enm) {
		return super.getBase().get(enm.name());
	}

	protected void setValue(IConcept.Attributes enm, String value) {
		super.getBase().set(enm.name(), value);
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
			Concept clone = this.getClass().getDeclaredConstructor(Concept.class).newInstance();
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
