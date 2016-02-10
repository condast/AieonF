package org.aieonf.util.parser;

//J2SE
import java.util.*;

import org.aieonf.util.logger.Logger;

/**
 * <p>Title: Saight</p>
 *
 * <p>Description: A favourites generator for browsers</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Condast B.V.</p>
 *
 * @author Kees Pieters
 * @version 1.0
 */
public class LinkParser
{
  //The original uri
  private String rawUri;

  //The uri after parsing
  private String uri;

  //The parameters
  private Properties parameters;

  // the logger
  private Logger logger;

  public LinkParser( String rawUri )
  {
    super();
    this.rawUri = uri;
    logger = Logger.getLogger( this.getClass() );
    parameters = new Properties();
    String[] split = rawUri.split( "[?]" );
    this.uri = split[ 0 ];
    String params;
    String[] param;
    if( split.length > 1 ){
      params = rawUri.substring( uri.length() + 1, rawUri.length() );
      logger.trace( "Parsing parameter: " + params );
      split = params.split( "[&]");
      for( int i = 0; i < split.length; i++ ){
        param = split[ i ].split( "[=]" );
        if( param.length == 1 )
        	continue;
        if(( param[ 0 ] == null ) || ( param[ 0 ].trim() == "" ) ||
           ( param[ 0 ].trim() == "null" ))
          continue;
        if(( param[ 1 ] == null ) || ( param[ 1 ].trim() == "" ) ||
           ( param[ 1 ].trim() == "null" ))
          continue;

        logger.trace( "\nParameters found: " + param[ 0 ] + "=" + param[ 1 ] );
        parameters.setProperty( param[ 0 ], param[ 1 ] );
      }
    }
  }

  /**
   * Get the unprocessed uri
   * @return String
  */
  public String getFullUri()
  {
    return this.rawUri;
  }

  /**
   * Get the uri
   * @return String
  */
  public String getURI()
  {
    return this.uri;
  }

  /**
   * Get the parameter names
   * @return Enumeration
  */
  public Iterator<String> getParamaterNames(){
    Collection<String> keys = new ArrayList<String>();
    Enumeration<?> en =  parameters.keys();
    while( en.hasMoreElements() )
    	keys.add(( String )en.nextElement() );
  	return keys.iterator();
  }

  /**
   * Get the parameter value corresponding with the given paramter name
   * @param name String
   * @return String
  */
  public String getParameter( String name )
  {
    return this.parameters.getProperty( name );
  }

  /**
   * If true, the parser contains parameters
   * @return boolean
  */
  public boolean hasParameters()
  {
    return ( this.parameters.isEmpty() == false );
  }

  /**
   * Get the parameter string for the given properties
   *
   * @return String
  */
  public String getParameterString()
  {
    String str = "?";
    Iterator<String> names = getParamaterNames();
    String name, value;
    while( names.hasNext() ){
      name = names.next();
      value = getParameter( name );
      str += name + "=" + value + "&";
    }
    return str.substring( 0, str.length() - 1 );
  }

  /**
   * Get the parameters of the request
   * @return
   */
  public Map<String, String> getParameters()
  {
    Iterator<String> names = getParamaterNames();
    Map<String,String> parameters = new TreeMap<String, String>();
    String name, value;
    while( names.hasNext() ){
      name = names.next();
      value = getParameter( name );
      parameters.put( name, value);
    }
    return parameters;
  	
  }
  
  /**
   * Print the parameters. Mainly for debugging purposes
   * @return
  */
  public String printParameters()
  {
  	StringBuffer buffer = new StringBuffer();
  	buffer.append( "Parameters sent:");
  	Enumeration<?> en = this.parameters.propertyNames();
  	String name;
  	int index = 0;
  	while( en.hasMoreElements() ){
  		name = ( String )en.nextElement();
  		buffer.append( "\t" + name + "-" + this.parameters.getProperty( name ));
  		index++;
  	}
  	buffer.append( "Total parameters found: " + index );
  	return buffer.toString();
  }
  
  /**
   * Get the parameter string for the given properties
   * @param parameters Properties
   * @return String
  */
  public static String getParameterString( Properties parameters )
  {
    String str = "?";
    Enumeration<?> names = parameters.propertyNames();
    String name, value;
    while( names.hasMoreElements() ){
      name = ( String ) names.nextElement();
      value = parameters.getProperty( name );
      str += name + "=" + value + "&";
    }
    return str.substring( 0, str.length() - 1 );
  }
}
