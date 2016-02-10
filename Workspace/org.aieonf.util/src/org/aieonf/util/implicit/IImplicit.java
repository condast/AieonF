package org.aieonf.util.implicit;

/**
 * An implicit object adds three methods to a standard object that
 * extend the 'equals'.
 * @author Kees Pieters
 *
 */
public interface IImplicit<T extends Object>
{
  /**
   * If implies is true, the given implicit object is considered to be equal to this one,
   * even though the form and structure may be different.
   *
   * @param implicit
   * @return boolean
  */
  public boolean implies( Object implicit );

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
}
