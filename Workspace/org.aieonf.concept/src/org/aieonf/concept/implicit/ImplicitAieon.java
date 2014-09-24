package org.aieonf.concept.implicit;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IDescriptorViewer;
import org.aieonf.concept.core.*;
import org.aieonf.concept.wrapper.ConceptWrapper;
import org.aieonf.util.implicit.IImplicit;

public class ImplicitAieon extends ConceptWrapper implements IImplicitAieon<IDescriptor>
{
	/**
	 * for serialisation purposes
	 */
	private static final long serialVersionUID = 1L;

	private String attribute;

	/**
	 * Create a default implicit aieon
	 * @throws ConceptException
	 */
	public ImplicitAieon()
	{
		super( new Concept() );
		this.setClassName( this.getClass().getName() );
	}

	/**
	 * Create an implicit aieon for the given name
	 * @param attribute
	 * @throws ConceptException
	 */
	protected ImplicitAieon( String name )
	{
		this( new Concept( name ), name );
	}

	/**
	 * Create an implicit aieon for the given attribute
	 * @param attribute
	 * @throws ConceptException
	 */
	public ImplicitAieon( String name, String attribute )
	{
		this( new Concept( name ), attribute );
	}

	/**
	 * Create an implicit aieon for the given attribute and value
	 * @param attribute
	 * @param value
	 * @throws ConceptException
	 */
	public ImplicitAieon( String name, String attribute, String value )
	{
		this( name, attribute );
		this.set( attribute.trim(), value );
	}

	/**
	 * Create an implicit aieon for the given concept and using the attribute
	 * @param concept
	 * @param attribute
	 * @throws ConceptException
	 */
	public ImplicitAieon( IDescriptor descriptor, String attribute )
	{
		super( descriptor );
		this.setClassName( this.getClass().getName() );
		this.attribute = attribute.trim();
	}

	/**
	 * Set the attribute
	 * @param attribute String
	 */
	public final void setAttribute( String attribute )
	{
		this.attribute = attribute;
	}

	/**
	 * If implies is true, the given descriptor is considered to be equal to this one,
	 * even though the form and structure (descriptor!) may be different.
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	@Override
	public boolean implies( Object descriptor)
	{
		if( descriptor == null )
			return false;
		if( super.getDescriptor().objEquals( descriptor ))
			return true;

		if(( descriptor instanceof IConcept ) == false )
			return false;
		IConcept concept = ( IConcept )descriptor;
		return ( concept.get( attribute ) != null ) &&
				concept.get( attribute ).equals( this.get( attribute ));
	}

	/**
	 * An accept is basically an extension to equals. Depending on the structure
	 * and the requirements, additional restrictions or freedom may be granted to
	 * the default equals. This is especially true for, for instance, versioning.
	 * Accept differs from implies that it usually is reserved for similar concepts,
	 * so the descriptors must be the same
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	@Override
	public boolean accept( IDescriptor descriptor)
	{
		if( this.objEquals( descriptor ))
			return true;

		if( this.equals( descriptor ) == false )
			return false;

		String source = descriptor.get( this.attribute );
		if(( source == null ) && ( this.getAttributeValue() == null ))
			return true;
		if(( source == null ) || ( this.getAttributeValue() == null ))
			return false;

		return ( this.getAttributeValue().equals( source ));
	}

	/**
	 * Every implicit descriptor should reserve one attribute for
	 * a class identification. A none-null value for this attribute implies
	 * that a concept is implicit to this descriptor
	 * @return String
	 */
	@Override
	public String getClassAttribute()
	{
		return this.attribute;
	}

	/**
	 * Every implicit descriptor should reserve on attribute that uniquely
	 * identifies the implicit (normally the value of the class attribute).
	 * If different values
	 * @return String
	 */
	@Override
	public String getAttributeValue()
	{
		return this.get( this.attribute );
	}

	/**
	 * Get the key identifying value for the given descriptor
	 * @param descriptor IDescriptor
	 * @return String
	 */
	@Override
	public String getAttributeValue(IDescriptor descriptor)
	{
		return descriptor.get( this.attribute );
	}

	/**
	 * Returns true if the given descriptor is of the same class as this one
	 * This usually means that the name is equal and that they share the same
	 * attributes
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	public boolean isImplicitClass( IDescriptor descriptor )
	{
		if( this.getName().equals( descriptor.getName()) == false )
			return false;

		return ( this.get( this.attribute ) != null ) &&
				( descriptor.get( this.attribute ) != null );
	}

	/**
	 * The compare to should uniquely identify different implicit aieons 
	 * If the given object is not implicit, then a regular compare is performed.
	 * If the object is not an implicit aieon, the descriptors compare-to is used,
	 * and if 
	 */
	@Override
	public int compareTo( IDescriptor descriptor )
	{
		if(( descriptor instanceof IImplicit ) == false  )
			return super.compareTo(descriptor);
		if(( descriptor instanceof IImplicitAieon ) == false  )
			return -descriptor.compareTo( this );
		IImplicitAieon<?> implicit = ( IImplicitAieon<?> )descriptor;
		if( this.getAttributeValue() == null )
			return -1;
		if( implicit.getAttributeValue() == null )
			return 1;
		return this.getAttributeValue().compareTo( implicit.getAttributeValue() );
	}

	@Override
	public boolean isFamily( Object descriptor)
	{
		if( !( descriptor instanceof IDescriptor ))
			return false;
		IDescriptor desc = ( IDescriptor )descriptor;
		return isFamily( this, attribute, desc );
	}

	@Override
	public IDescriptorViewer getViewer()
	{
		return new ImplicitDescriptorViewer<IDescriptor>( this );
	}

	/**
	 * Default implies operation. This happens when the given descriptor is of the same family as the reference
	 * @param descriptor
	 * @return
	 */
	public static boolean defaultImplies(IDescriptor reference, String attribute, IDescriptor descriptor)
	{
		if( !descriptor.getName().equals( reference.getName() ))
			return false;
		String refVal = reference.get( attribute );
		if( Descriptor.isNull( refVal ))
			return false;
		if( Descriptor.isNull( descriptor.get( attribute )))
			return false;
		return refVal.equals( descriptor.get( attribute ));
	}

	/**
	 * 
	 * @param descriptor
	 * @return
	 */
	public static boolean isFamily(IImplicit<?> implicit, String reference, Object desc)
	{
		if( !implicit.isFamily( desc ))
			return false;
		IDescriptor descriptor = ( IDescriptor )desc;
		return !Descriptor.isNull( descriptor.get( reference ));
	}

}