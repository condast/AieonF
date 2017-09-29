package org.aieonf.commons.i8n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Language {

	public static final String S_RESOURCE_LOCATION = "language.lan";

	public static final String S_ERR_EXCEPTION = "Exception on: ";

	public static final String S_MSG_EXTENSION = "_MSG";
	public static final String S_INFO_EXTENSION = "_INFO";

	private ResourceBundle resources; 
	private String id;
	
	public Language( String id ) {
		this( id, S_RESOURCE_LOCATION, Locale.US );
	}
	public Language( String id, String country, String language ) {
		this( id, S_RESOURCE_LOCATION, new Locale( country, language ));
	}

	public Language( String id, String location, String country, String language ) {
		this( id, location, new Locale( country, language ));
	}

	public Language( String id, Locale locale ) {
		this( id, S_RESOURCE_LOCATION, locale );
	}
	
	public Language( String id, String location, Locale locale ) {
		this.id = id;
		try{
			resources = ResourceBundle.getBundle( location, locale, this.getClass().getClassLoader());
		}
		catch( Exception ex ){
			Logger.getLogger( this.getClass().getName() ).severe( S_ERR_EXCEPTION + location + 
					": " + ex.getMessage() );
			ex.printStackTrace();
		}
	}
	
	/**
	 * returns an internationalised string, or the original key if none was found
	 * @param key
	 * @return
	 */
	public String getString( String key ){
		String retval = null;
		try{
			retval = resources.getString( key );
		}
		catch( Exception ex ){
			Logger.getLogger( this.getClass().getName() ).severe( S_ERR_EXCEPTION + 
					"[" + id + "]:"+ key + ": " + ex.getMessage() );
			ex.printStackTrace();
		}
		return retval;
	}

	/**
	 * returns an internationalised string array,
	 * @param key
	 * @return
	 */
	public String[] getStringArray( String[] keys ){
		String[] retval = new String[ keys.length];
		for( int i=0; i<retval.length; i++ ){
			String str = getString( keys[i] );
			retval[ i ] = ( str != null)? str: keys[i];
		}
		return retval;
	}

	/**
	 * Get the message for the given enum type
	 * @param enm
	 * @return
	 */
	public String getString( Enum<?> enm ){
		return getString( enm.name() );
	}

	/**
	 * returns an internationalised string array,
	 * @param key
	 * @return
	 */
	public String[] toStrArray( Enum<?>[] keys ){
		String[] retval = new String[ keys.length];
		for( int i=0; i<retval.length; i++ ){
			retval[i] = getString( keys[i].name() );
		}
		return retval;
	}

	/**
	 * A message is provided as the key + "_MSG"
	 * @param key
	 * @return
	 */
	protected String getMessage( String key, String extension ){
		return getString( key + extension );
	}

	/**
	 * A message is provided as the key + "_MSG"
	 * @param key
	 * @return
	 */
	public String getMessage( String key ){
		return getMessage( key, S_MSG_EXTENSION );
	}

	/**
	 * A message is provided as the key + "_MSG"
	 * @param key
	 * @return
	 */
	public String getMessage( Enum<?> key ){
		return getMessage( key.name(), S_MSG_EXTENSION );
	}

	/**
	 * A message is provided as the key + "_MSG"
	 * @param key
	 * @return
	 */
	public String getInfo( String key ){
		return getMessage( key, S_INFO_EXTENSION );
	}

	/**
	 * A message is provided as the key + "_MSG"
	 * @param key
	 * @return
	 */
	public String getInfo( Enum<?> key ){
		return getMessage( key.name(), S_INFO_EXTENSION );
	}

	protected static String[] translate( Language language, String[] items ){
		String[] results = new String[ items.length ];
		for( int i=0; i<results.length; i++ ){
			results[i] = language.getString( items[i]);
		}
		return results;
	}
}
