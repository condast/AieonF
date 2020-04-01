package org.aieonf.commons.implicit;

import org.aieonf.commons.strings.StringStyler;

/**
 * An implicit object adds three methods to a standard object that
 * extend the 'equals'.
 * @author Kees Pieters
 *
 */
@FunctionalInterface
public interface IImplicit<T extends Object>
{
	/**
	 * Get the rules when two descriptors are considered the same. By default, it is 'equals',
	 * but for instance a category s the same when the 'category' attributes has the same name
	 * @author Kees Pieters
	 */
	public enum Conditions
	{
		EQUAL,
		ID,
		ON_ATTRIBUTE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

 /**
   * If implies is true, the given implicit object is considered to be equal to this one,
   * even though the form and structure may be different.
   *
   * @param implicit
   * @return boolean
  */
  public boolean implies( Object implicit );

}
