package org.aieonf.concept.wrapper;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.ConceptBase;
import org.aieonf.concept.core.ConceptException;

/**
 * In general, a concept can best be wrapped in
 * another object in order to more efficiently change its 
 * content. This can for instance, be the case when the
 * contents of an concept stored in an xml sheet is processed.
 * This class provides the base functionality for such a wrapper 
 * @author Kees Pieters
 *
 */
public class ConceptWrapper extends DescriptorWrapper implements IConcept
{
	private static final long serialVersionUID = 1342477347056704267L;

	public static final String S_ERR_INVALID_CONCEPT = 
			"The provided concept is not valid for this type: ";


	public ConceptWrapper( IDescriptor descriptor )
	{
		super( descriptor );
	}

	protected ConceptWrapper( String id, IDescriptor descriptor ) throws ConceptException
	{
		super( id, descriptor );
	}

	/**
	 * Get the descriptor
	 * @return
	 */
	protected IDescriptor getStoredDescriptor()
	{
		return super.getDescriptor();
	}

	/**
	 * Set the name
	 * @param name
	 */
	protected void setName( String name ){
		set( IDescriptor.Attributes.NAME, name );
	}

	/**
	 * Set the id
	 * @param id
	 */
	protected void setID( String id ){
		set( IDescriptor.Attributes.ID, id );
	}

	/**
	 * Get the scope of the concept (default public)
	 *
	 * @return int
	 */
	@Override
	public final Scope getScope()
	{
		String str = super.getDescriptor().get( IConcept.Attributes.SCOPE );
		if( Utils.assertNull(str))
			return Scope.APPLICATION;
		return Scope.valueOf(  StringStyler.styleToEnum( str ));
	}

	/**
	 * Set the scope of the concept (default public)
	 *
	 * @param scope int
	 */
	@Override
	public final void setScope( Scope scope )
	{
		set( IConcept.Attributes.SCOPE, scope.toString() );
	}

	@Override
	public String getSource() {
		return super.getDescriptor().get( IConcept.Attributes.SOURCE );
	}

	
	@Override
	public String getURIPath() {
		return super.getDescriptor().get( IConcept.Attributes.URI.name() );
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Set the scope of the concept (default public)
	 *
	 * @param scope int
	 */
	protected void setSource( String source )
	{
		set( IConcept.Attributes.SOURCE, source );
	}

	/**
	 * Returns true if the concept is read-only
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isReadOnly()
	{
		return super.getDescriptor().getBoolean( ConceptBase.getAttributeKey( IConcept.Attributes.READ_ONLY ));
	}

	/**
	 * Get or set the read-only option
	 *
	 * @param readonly boolean
	 */
	@Override
	public final void setReadOnly( boolean readonly )
	{
		set( IConcept.Attributes.READ_ONLY , String.valueOf( readonly ));
	}

	/**
	 * Returns true if the concept is hidden
	 *
	 * @return boolean
	 */
	@Override
	public final boolean isHidden()
	{
		return super.getDescriptor().getBoolean( ConceptBase.getAttributeKey( IConcept.Attributes.HIDDEN ));
	}

	/**
	 * Get or set the hidden option
	 *
	 * @param hidden boolean
	 */
	@Override
	public final void setHidden( boolean hidden )
	{
		set( IConcept.Attributes.HIDDEN, String.valueOf( hidden ));
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
		return ( descriptor instanceof IConcept );
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
		return(( super.getDescriptor().get( key ) != null ));
	}
}
