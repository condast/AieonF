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
public class CategoryAieon extends ImplicitAieon implements IImplicitAieon<IDescriptor>
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
	public CategoryAieon(){
		super();
		this.setName( Attributes.CATEGORY.name() );
		this.setImplicit(Attributes.CATEGORY.name() );
	}

	/**
	 * Create a concept that implements a category
	 *
	 * @param category String
	 */
	public CategoryAieon( String category ){
		this();
		this.setCategory( category );
	}

	/**
	 * Create a concept that implements a category
	 *
	 * @param category String
	 */
	public CategoryAieon( IDescriptor descriptor ){
		super( descriptor.getBase());
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
}