/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package  org.aieonf.commons.strings;

import java.util.Set;
import java.util.TreeSet;

/*
 * @author jverp
 * 
 */
public final class StringUtils {

	/**
	 * This code is based on the hashcode implementations in JAVA
	 * Keep an eye out on Levenshtein distances:
	 * @See https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
	 * @See https://en.wikipedia.org/wiki/Levenshtein_distance 
	 * @return
	 */
	public static int computeStringDistance(CharSequence lhs, CharSequence rhs, boolean leftToRight) {      
		//use the shortest of the two 
		int length = ( lhs.length() < rhs.length())? lhs.length(): rhs.length();
		int hash = 0;
		int weight;
		int index = 0;
		for (int i = 0; i <= length-1; i++){
			index = ( leftToRight )? i: (length - i - 1);
			weight = lhs.charAt(index) - rhs.charAt(index);
			hash += (hash << 5 ) - hash + weight;
		}
		return hash;                           
	} 	

	/**
	 * This calculates the differences as long as it fits in a long (8 bytes max). This will
	 * work for postal codes
	 * @return
	 */
	public static long computeSmallStringDistance(CharSequence lhs, CharSequence rhs, boolean leftToRight ) {      
		//use the shortest of the two 
		int length = ( lhs.length() < rhs.length())? lhs.length(): rhs.length();
		long hash = 0;
		short weight = 0;
		int index = 0;
		for (int i = 0; i <= length; i++){
			index = ( leftToRight )? i: (length - i - 1);
			weight = (short)( lhs.charAt(index) - rhs.charAt(index));
			hash <<=8;
			hash += weight;
		}
		return hash;                           
	} 	

	/**
	 * This code is based on the hashcode implementations in JAVA
	 * Keep an eye out on Levenshtein distances:
	 * @See https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
	 * @See https://en.wikipedia.org/wiki/Levenshtein_distance 
	 * @return
	 */
	public static long computeStringDistance(CharSequence lhs, CharSequence rhs, boolean leftToRight, byte compress) {      
		//use the shortest of the two 
		int length = ( lhs.length() < rhs.length())? lhs.length(): rhs.length();
		long hash = 0;
		short weight = 0;
		int index = 0;
		int maxval = 1 <<( 8 + compress );
		for (int i = 0; i <= length; i++){
			index = ( leftToRight )? i: (length - i - 1);
			weight = (short)( lhs.charAt(index) - rhs.charAt(index));
			if( weight == 0 )
				continue;
			int store = weight;
			while( store < maxval ){
				store<<=1; 
			}
			hash += (store>>compress);
		}
		return hash;                           
	} 	

	public static String checkStringForNull(String text){
		String rtv = "";
		if (text != null)
			rtv=text;
		return rtv;
		
	}
	
    public static boolean hasText(String text) {
       boolean retval = false;
        if (text != null && text.trim().length() > 0) {
            retval = true;
        }
        return retval;
    }
	/**
	 * Replaces non letters of digits with an '_'.
	 * @param s
	 * @return
	 */
	public static String replaceUnprintables(String s) {		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length(); i++) {
			if (! Character.isLetterOrDigit(sb.charAt(i))) {
				sb.setCharAt(i, '_');		}
		}
		return sb.toString();
	}	
	public static boolean isEmpty( String str ){
		return (( str == null ) || ( str.trim().length()  == 0 ));
	}
	public static boolean isNullOrEmpty( String str ){		return (( str == null ) || ( str.trim().length()  == 0 ));	}

	/**
	 * Set the first character of the string to uppercase
	 * @param strng
	 * @return
	 */
	public static String firstUpperCaseString( String strng ){
		char chr = strng.charAt(0);
		String str = strng.toString().toLowerCase().substring(1);
		return String.valueOf(chr) + str;		
	}

	/**
	 * Create a pretty string from the given one
	 * @param strng
	 * @return
	 */
	public static String prettyString( String strng ){
		if( !strng.toUpperCase().equals(strng ) && !strng.toLowerCase().equals(strng ))
			return strng;
		String[] split = strng.split("[_]");
		StringBuffer buffer = new StringBuffer();
		for( String str: split )
			buffer.append( firstUpperCaseString( str ));
		return buffer.toString();
	}
	
	public static String removeLastTwoCharacters(String in){
		String rtv = "";
		StringBuilder sb = new StringBuilder(in);
		final int length = sb.length();
		if(length>0){
			sb.delete(length-2, length); 
			rtv = sb.toString();
		}	
			
		return rtv;
	}
	
	
	
	public static Set<String> getLastStringsSet(Set<String> set, String remove){
		Set<String> rtv = new TreeSet<String>();
		for(String s:set){
			String snew = s.replace(remove,"");
			snew = snew.trim();
			if(!snew.isEmpty())
			 rtv.add(snew);
		}
		
		return rtv;		
	}
	
	public static String[] splitStringOnWhitSpacing(String s){
		s = s.trim();
		String[] split = s.split("\\s+");
		
		return split;
	}
	
	
	
	

}
