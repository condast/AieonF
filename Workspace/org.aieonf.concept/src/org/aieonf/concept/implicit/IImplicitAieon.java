package org.aieonf.concept.implicit;

import org.aieonf.concept.IDescriptor;
import org.aieonf.util.implicit.IImplicit;

/**
 * An implicit concept adds two methods to a standard concept that
 * extend the 'equals'.
 * @author Kees Pieters
 *
 */
public interface IImplicitAieon<T extends IDescriptor> extends IDescriptor, IImplicit<T>
{
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
