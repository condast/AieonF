package org.aieonf.concept.implicit;

import org.aieonf.commons.implicit.IImplicit;
import org.aieonf.concept.IDescriptor;

/**
 * An implicit concept adds two methods to a standard concept that
 * extend the 'equals'.
 * @author Kees Pieters
 *
 */
public interface IImplicitAieon<T extends IDescriptor> extends IDescriptor, IImplicit<T>
{

	  /**
	   * An accept is basically an extension to equals. Depending on the structure
	   * and the requirements, additional restrictions or freedom may be granted to
	   * the default equals. This is especially true for, for instance, versioning.
	   * Accept differs from implies that it usually is reserved for similar concepts,
	   * so the implicit objects must be the same
	   *
	   * @param descriptor IDescriptor
	   * @return boolean
	  */
	  public boolean accept( T implicit );
	  
	  /**
	   * Returns true if the given object is of the same family as this one. This means that
	   * the required structure is correct, but the values may differ 
	   * @param implicit
	   * @return true if the descriptor is of the correct family
	  */
	  public boolean isFamily( Object implicit );

	/**
   * Every implicit descriptor should reserve one attribute for
   * a class identification. A none-null value for this attribute implies
   * that a concept is implicit to this descriptor
   * @return String
   */
  public String getClassAttribute();

  /**
   * Every implicit descriptor should reserve one attribute that uniquely
   * identifies the implicit (normally the value of the class attribute).
   * If different values
   * @return String
  */
  public String getAttributeValue();

  /**
   * Get the key identifying value for the given descriptor
   * @param descriptor IDescriptor
   * @return String
   */
  public String getAttributeValue( IDescriptor descriptor );
}
