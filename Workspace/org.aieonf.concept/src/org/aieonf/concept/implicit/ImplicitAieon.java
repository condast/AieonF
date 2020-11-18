package org.aieonf.concept.implicit;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.*;

public class ImplicitAieon extends Concept implements IImplicitAieon<IDescriptor>{
	private static final long serialVersionUID = 1L;

	public ImplicitAieon() {
		super();
	}

	public ImplicitAieon(IConceptBase base, String implicit) {
		super(base);
		super.set(IImplicit.Attributes.IMPLICIT.name(), implicit);
	}

	public ImplicitAieon(long id, String name, String implicit) {
		super(id, name);
		super.set(IImplicit.Attributes.IMPLICIT.name(), implicit);
	}

	public ImplicitAieon(String name, String implicit) {
		super(name);
		super.set(IImplicit.Attributes.IMPLICIT.name(), implicit);
	}

	public ImplicitAieon(IConceptBase base) {
		super(base);
	}

	@Override
	public String getImplicit() {
		return super.get(IImplicit.Attributes.IMPLICIT.name());
	}
	
	protected void setImplicit( String implicit ) {
		super.set(IImplicit.Attributes.IMPLICIT.name(), implicit);
	}

	protected void setImplicit( Enum<?> implicit ) {
		super.set(IImplicit.Attributes.IMPLICIT.name(), ConceptBase.getAttributeKey( implicit ));
	}

	/**
	 * If implies is true, the given descriptor is considered to be equal to this one,
	 * even though the form and structure (descriptor!) may be different.
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	@Override
	public boolean test( IDescriptor descriptor){
		String implicit = getImplicit();
		return test( implicit, descriptor);
	}

	/**
	 * If implies is true, the given descriptor is considered to be equal to this one,
	 * even though the form and structure (descriptor!) may be different.
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	protected boolean test( String implicit, IDescriptor descriptor)
	{
		if( descriptor == null )
			return false;
		if( super.getDescriptor().equals( descriptor ))
			return true;
		String impl = get( implicit );
		if( StringUtils.isEmpty(impl))
			return false;
		String impl1 = descriptor.get( implicit );
		return (  !StringUtils.isEmpty(impl1)) && impl.equals( impl1 );
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
		if( this.objEquals( descriptor ) || this.equals( descriptor ))
			return true;

		String implicit = getImplicit();
		if( StringUtils.isEmpty(implicit))
			return false;
		String reference = get( implicit );
		if( reference == null )
			return false;
		String source = descriptor.get( implicit );
		return ( source == null )? false: ( reference.equals( source ));
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
		if( this.getImplicit() == null )
			return -1;
		if( implicit.getImplicit() == null )
			return 1;
		return this.getImplicit().compareTo( implicit.getImplicit() );
	}

	@Override
	public boolean isFamily( Object descriptor)
	{
		if( !( descriptor instanceof IDescriptor ))
			return false;
		IDescriptor desc = ( IDescriptor )descriptor;
		String implicit = getImplicit();
		return isFamily( this, implicit, desc );
	}

	/**
	 * returns true if the given concept base contains an implicit directive
	 * @param base
	 * @return
	 */
	public static boolean isImplicit( IConceptBase base ) {
		String implicit = base.get(IImplicit.Attributes.IMPLICIT.name());
		return !StringUtils.isEmpty(implicit);
	}

	/**
	 * returns true if the given concept base contains an implicit directive
	 * @param base
	 * @return
	 */
	public static boolean isImplicit( IDescriptor descriptor ) {
		String implicit = descriptor.get(IImplicit.Attributes.IMPLICIT.name());
		return !StringUtils.isEmpty(implicit);
	}

	/**
	 * Make the given descriptor implicit
	 * @param descriptor
	 * @param implicit
	 */
	public static void setImplicit( IDescriptor descriptor, String implicit ) {
		descriptor.set(IImplicit.Attributes.IMPLICIT.name(), implicit);
	}

	/**
	 * Returns true if the two base objects are implicit
	 * @param ref
	 * @param test
	 * @return
	 */
	public static boolean areImplicit( IConceptBase ref, IConceptBase test ) {
		ImplicitAieon implicit = null;
		boolean result = false;
		if( ImplicitAieon.isImplicit(ref)) {
			implicit = new ImplicitAieon( ref );
			result = implicit.accept(new Descriptor( test ));
		}else if( ImplicitAieon.isImplicit(test)) {
			implicit = new ImplicitAieon( test );
			result = implicit.accept(new Descriptor( ref ));
			if( result )
				ref.set(IImplicit.Attributes.IMPLICIT.name(), test.get(IImplicit.Attributes.IMPLICIT.name()));
		}
		return result;
	
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
		if( Descriptor.assertNull( refVal ))
			return false;
		if( Descriptor.assertNull( descriptor.get( attribute )))
			return false;
		return refVal.equals( descriptor.get( attribute ));
	}

	/**
	 * 
	 * @param descriptor
	 * @return
	 */
	public static boolean isFamily(IImplicitAieon<?> implicit, String reference, Object desc)
	{
		if( !implicit.isFamily( desc ))
			return false;
		IDescriptor descriptor = ( IDescriptor )desc;
		return !Descriptor.assertNull( descriptor.get( reference ));
	}

}