package org.aieonf.commons.number;

import org.aieonf.commons.strings.StringUtils;

public class NumberUtils {

	/**
	 * Test if the given string is numerical
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
	    if (StringUtils.isEmpty(str)) {
	        return false;
	    }
	    String test = str.trim();
	    int sz = test.length();
	    for (int i = 0; i < sz; i++) {
	        if(( i==0 ) && test.charAt(i)=='-')
	        	continue;
	    	if (Character.isDigit(test.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Parse the given String for a long value
	 * @param value
	 * @return
	 */
	public static long parseLong( String value ) {
		if( StringUtils.isEmpty(value) || !isNumeric(value))
			return -1;
		return Long.parseLong(value);
	}
}
