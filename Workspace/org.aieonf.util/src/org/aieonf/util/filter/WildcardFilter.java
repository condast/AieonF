package org.aieonf.util.filter;

/*************************************************************************
 * File Name: WildcardSearch.java
 * Date: Jan 9, 2004
 *
 * This class will search all files in a directory using the
 * asterisk (*) and/or question mark (?) as wildcards which may be
 * used together in the same file name.  A File [] is returned containing
 * an array of all files found that match the wildcard specifications.
 *
 * Command line example:
 * c:\>java WildcardSearch c:\windows s??t*.ini
 * New sWild: s.{1}.{1}t.*.ini
 * system.ini
 *
 * Command line break down: Java Program = java WildcardSearch
 *                          Search Directory (arg[0]) = C:\Windows
 *                          Files To Search (arg[1]) = s??t*.ini
 *
 * Note:  Some commands will not work from the command line for arg[1]
 *        such as *.*, however, this will work if you if it is passed
 *        within Java (hard coded)
 *
 * @author kmportner
 **************************************************************************/

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * <p>Title: Conceptual Network Database</p>
 *
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast BV</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class WildcardFilter implements FilenameFilter
{
  public static final String S_ALL = "*";
  
	/**
	 * Supported rules
	 * @author Kees
	 *
	 */
  public enum Rules{
		CaseSensitive,
		CaseInsensitive
	}
	private Rules rule;
	
  private String sWild = "";

  /**
   * Create the filter for the given wildcard
   *
   * @param wildCard String
  */
  public WildcardFilter( String wildCard )
  {
  	sWild = replaceWildcards( wildCard );
  	this.rule = Rules.CaseInsensitive;
  }

  /**
   * Create the filter for the given wildcard
   *
   * @param wildCard String
   * @param rule Rules
  */
  public WildcardFilter( String wildCard, Rules rule )
  {
  	sWild = replaceWildcards( wildCard );
  	this.rule = rule;
  }

  /**
   * If true, the given filename is accepted in the given directory
   *
   * @param dir File
   * @param name String
   * @return boolean
  */
  @Override
	public boolean accept( File dir, String name )
  {
    if( name == null )
    	return false;
    if( this.rule.equals( Rules.CaseInsensitive ))
    	name = name.trim().toLowerCase();
    return (name.matches(sWild));
  }

  /**
   * If true, the given filename is accepted in the given directory
   *
   * @param name String
   * @return boolean
  */
  public boolean accept( String name )
  {
    if( name == null )
    	return false;
    if( this.rule.equals( Rules.CaseInsensitive ))
    	name = name.trim().toLowerCase();
    return( name.matches( sWild ));
  }

  /**
   * Checks for * and ? in the wildcard variable and replaces them correct
   * pattern characters.
   *
   * @param wild String
   * @return String
  */
  private String replaceWildcards( String wild )
  {
    if( wild == null )
    	wild = "";
  	StringBuffer buffer = new StringBuffer();
    char [] chars = wild.toCharArray();
    for (int i = 0; i < chars.length; ++i)
    {
      if (chars[i] == '*')
        buffer.append(".*");
      else if (chars[i] == '?')
        buffer.append(".");
      else if ("+()^$.{}[]|\\".indexOf(chars[i]) != -1)
        buffer.append('\\').append(chars[i]);
      else
        buffer.append(chars[i]);
    }
    return buffer.toString().toLowerCase();
  }
}
