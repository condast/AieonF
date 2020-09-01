package org.aieonf.commons.implicit;

import java.util.function.Predicate;

import org.aieonf.commons.strings.StringStyler;

/**
 * An implicit object adds three methods to a standard object that
 * extend the 'equals'.
 * @author Kees Pieters
 *
 */
@FunctionalInterface
public interface IImplicit<D extends Object> extends Predicate<D>
{
	/**
	 * The basic elements of a descriptor
	 */
	public enum Attributes
	{
		IMPLICIT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ){
			for( Attributes attr: values() ){
				if( attr.name().equals( str ))
					return true;
			}
			return false;
		}
	}
	
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
}
