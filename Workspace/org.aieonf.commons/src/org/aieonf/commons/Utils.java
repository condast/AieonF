package org.aieonf.commons;

public class Utils
{

	/**
	 * returns true if the string is null or empty
	 * @param str
	 * @return
	 */
	public static final boolean isNull( String str ){
		return (( str == null) || ( str.trim().length() == 0 ));
	}

	/**
	 * Retuens true if hte given list is null or empty
	 * @param list
	 * @return
	 */
	public static boolean assertNull( Object[] list) {
		return ( list == null ) || (list.length == 0 );
	}
}
