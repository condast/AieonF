/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.library;

import org.aieonf.commons.strings.StringStyler;
//Concept imports
import org.aieonf.concept.*;
import org.aieonf.concept.core.*;
import org.aieonf.concept.implicit.IImplicitAieon;
import org.aieonf.concept.implicit.ImplicitAieon;
import org.aieonf.concept.wrapper.ConceptWrapper;

/**
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Implements a category</p>
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class CategoryAieon extends ConceptWrapper implements IImplicitAieon<IDescriptor>
{
	private static final long serialVersionUID = 1559367558005028566L;

	//The string associated with category
	public static final String OLD_CATEGORY = "OldCategory";

	public enum Attributes{
		CATEGORY;

		@Override
		public String toString() {
			return StringStyler.prettyString(super.toString());
		}
	}
	/**
	 * Create a concept that implements a category
	 *
	 * @throws ConceptException
	 */
	public CategoryAieon()
	{
		this( new Concept());
		this.set( IDescriptor.Attributes.NAME.name(), Attributes.CATEGORY.toString() );
	}

	/**
	 * Create a concept that implements a category
	 *
	 * @param category String
	 */
	public CategoryAieon( String category )
	{
		this();
		this.setCategory( category );
		this.setClassName( CategoryAieon.class.getCanonicalName() );
	}

	/**
	 * Create a cateogry that implements a category based on the given descriptor
	 *
	 * @param descriptor IDescriptor
	 * @param category String
	 */
	public CategoryAieon( IDescriptor descriptor, String category )
	{
		super( descriptor );
		this.set( IDescriptor.Attributes.NAME.name(),  Attributes.CATEGORY.toString() );
		this.setCategory( category );
		this.setClassName( this.getClass().getCanonicalName() );
		this.setProvider( descriptor.getProvider());
		this.setProviderName(descriptor.getProviderName());
	}

	public CategoryAieon(IDescriptor descriptor)
	{
		super( descriptor );
		this.fill();
	}

	protected void fill(){
		this.set( IDescriptor.Attributes.NAME.name(),  Attributes.CATEGORY.toString() );
		this.setClassName( this.getClass().getCanonicalName() );
	}

	/**
	 * Get the category name
	 * @return String
	 */
	public String getCategory()
	{
		return this.get(  Attributes.CATEGORY.name() );
	}

	/**
	 * Set the category name
	 *
	 * @param category String
	 */
	public void setCategory( String category )
	{
		this.set(  Attributes.CATEGORY.name(), category );
	}

	/**
	 * If true, the given concept is a category
	 *
	 * @param descriptor IConcept
	 * @return boolean
	 */
	@Override
	public boolean isA( IDescriptor descriptor )
	{
		return Descriptor.hasAttribute( descriptor,  Attributes.CATEGORY.toString() );
	}

	/**
	 * Creates a string array from the given descriptor: [name, id, version]
	 * @return String
	 */
	@Override
	public String toStringArray()
	{
		String str = super.toStringArray().replace(']',',');
		str += this.getCategory() + "]";
		return str;
	}

	/**
	 * If true, the given concept is a category
	 *
	 * @param concept IConcept
	 * @return boolean
	 */
	public static boolean isCategory( IDescriptor descriptor )
	{
		return ( descriptor.get(  Attributes.CATEGORY.name() ) != null );
	}

	/**
	 * Get the category name of a concept. Return null if the concept is not
	 * a category
	 *
	 * @param concept IConcept
	 * @return String
	 */
	public static String getCategoryName( IDescriptor descriptor )
	{
		if( isCategory( descriptor ))
			return ( descriptor.get(  Attributes.CATEGORY.name() ));
		return null;
	}

	/**
	 * If implies is true, the given descriptor is considered to be equal to this one,
	 * even though the form and structure (descriptor!) may be different.
	 *
	 * @param descriptor IDescriptor
	 * @return boolean
	 */
	@Override
	public boolean test( IDescriptor descriptor )
	{
		if( super.objEquals( descriptor ) == true )
			return true;

		String attribute = this.getClassAttribute();
		if( attribute == null )
			return false;
		if( !( descriptor instanceof IDescriptor ))
			return false;

		String value = (( IDescriptor )descriptor ).get( attribute );
		String thisVal = this.get( attribute );
		if(( value == null ) && ( thisVal == null ))
			return true;
		if(( value == null ) || ( thisVal == null ))
			return false;
		value = value.trim().toLowerCase();
		thisVal = thisVal.trim().toLowerCase();
		return value.equals( thisVal );
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
	public boolean accept( IDescriptor descriptor )
	{
		if( super.objEquals( descriptor ))
			return true;
		if( this.equals( descriptor ) == false )
			return false;
		if(( descriptor instanceof IConcept ) == false )
			return false;

		IConcept concept = ( IConcept )descriptor;
		if( CategoryAieon.isCategory( concept ) == false )
			return false;
		return this.getCategory().equals( CategoryAieon.getCategoryName( concept ));
	}

	/**
	 * Every implicit descriptor should reserve one attribute for
	 * a class identification. A none-null value for this attribute implies
	 * that a concept is implicit to this descriptor
	 * @return String
	 */
	@Override
	public String getClassAttribute() {
		return Attributes.CATEGORY.name();
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
		return this.getCategory();
	}

	/**
	 * Get the key identifying value for the given descriptor
	 * @param descriptor IDescriptor
	 * @return String
	 */
	@Override
	public String getAttributeValue(IDescriptor descriptor) {
		return descriptor.get( this.getClassAttribute() );
	}

	@Override
	public boolean isFamily(Object descriptor)
	{
		if(!( descriptor instanceof IDescriptor ))
			return false;
		return ImplicitAieon.isFamily( this, Attributes.CATEGORY.name(), descriptor);
	}
}
