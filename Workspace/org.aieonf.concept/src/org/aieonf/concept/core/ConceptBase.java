/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept.core;

import java.util.*;

import org.aieonf.commons.Utils;
import org.aieonf.commons.strings.StringStyler;

/**
 * Create a concept, using a properties file
 */
public class ConceptBase implements IConceptBase
{
	//The properties collection that contains the data
	private Map<String, String> properties;

	//If true, something has changed
	private transient boolean changed;

	public ConceptBase()
	{
		properties = new HashMap<String, String>();
		this.changed = false;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#hasChanged()
	 */
	@Override
	public boolean hasChanged()
	{
		return this.changed;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#setChanged(boolean)
	 */
	@Override
	public void setChanged( boolean choice )
	{
		this.changed = choice;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#clear()
	 */
	@Override
	public void clear()
	{
		this.properties.clear();
		this.changed = true;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#get(java.lang.String)
	 */
	@Override
	public final String get( String key )
	{
		return properties.get( key );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#set(java.lang.String, java.lang.String)
	 */
	@Override
	public void set( String key, String value )
	{
		if( key == null )
			throw new NullPointerException();
		if( key.contains(" ")){
			throw new IllegalArgumentException( "Invalid key " + key + " contains <space> character" );
		}

		if( Utils.assertNull( value ))
			properties.remove(key);
		else
			properties.put( key, value );
		this.changed = true;
	}

	/**
	 * Get the value belonging to a key of the concept
	 *
	 * @param key String
	 * @return String
	 */
	protected final String get( Class<?> clss, String key )
	{
		return get( getAttributeName( clss, key ));
	}

	/**
	 * This variant creates a key based on the class name.key
	 * @param clss
	 * @param key
	 * @param value
	 */
	protected void set( Class<?> clss, String key, String value )
	{
		this.set( getAttributeName( clss, key ), value );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#get(java.lang.Enum)
	 */
	@Override
	public String get( Enum<?> enm )
	{
		return get( getAttributeKey( enm ));
	}

	/**
	 * Set the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	@Override
	public void set( Enum<?> enm, String value )
	{
		set( getAttributeKey( enm ), value );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#remove(java.lang.Enum)
	 */
	@Override
	public void remove( Enum<?> enm )
	{
		remove( getAttributeKey( enm ));
	}


	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#getBoolean(java.lang.String)
	 */
	@Override
	public final boolean getBoolean( String attribute )
	{
		if( attribute == null )
			return false;
		String val = this.get( attribute );
		if( val == null )
			return false;

		val = val.toLowerCase().trim();
		if( val.equals("" ))
			return false;
		boolean yes = val.equals( "true" );
		if(( yes == false ) && ( val.equals( "false" ) == false ))
			return false;
		return yes;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#getInteger(java.lang.String)
	 */
	@Override
	public final int getInteger( String attribute )
	{
		String val = this.get( attribute );
		if( val == null )
			return 0;

		val = val.toLowerCase().trim();
		return Integer.parseInt( val );
	}


	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#remove(java.lang.String)
	 */
	@Override
	public void remove( String key )
	{
		this.properties.remove( key );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#size()
	 */
	@Override
	public int size()
	{
		return properties.size();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.concept.core.IConceptBase#iterator()
	 */
	@Override
	public final Iterator<String> iterator()
	{
		return this.properties.keySet().iterator() ;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		IConceptBase newBase = new ConceptBase();
		Iterator<String> iterator = iterator();
		while( iterator.hasNext() ){
			String key = iterator.next();
			newBase.set(key, get( key));
		}
		return newBase;
	}
	
	@Override
	public String toString() {
		return this.properties.toString().replace(",", ",\n");
	}

	/**
	 * Get the attribute group name of the concept of the given class
	 *
	 * @param key String
	 * @return String
	 */
	public static final String getAttributeClassifier( Class<?> clss )
	{
		return clss.getCanonicalName() + ".";
	}  

	/**
	 * Get the value belonging to a key of the concept
	 *
	 * @param key String
	 * @return String
	 */
	public static final String getAttributeName( Class<?> clss, String key )
	{
		return getAttributeClassifier(clss) + StringStyler.prettyString( key );
	}  

	/**
	 * Get the key belonging to the given enumeration
	 * @param key Attribute
	 * @return String
	 */
	public static String getAttributeKey( Enum<?> key )
	{
		String str = key.getClass().getCanonicalName()  + "." + key.toString(); 
		return str;
	}

	/**
	 * Parse the given string to a boolean value;
	 * @param val String
	 * @return boolean
	 * @throws ConceptException
	 */
	public final static boolean parseBoolean( String val )
	{
		//BY DEFINITION a value is false if it doesn't exist
		if( val == null )
			return false;

		val = val.toLowerCase().trim();
		boolean yes = val.equals( "true" );
		if(( yes == false ) && ( val.equals( "false" ) == false ))
			throw new IllegalArgumentException( S_ERR_INVALID_ATTRIBUTE + ": " + val );
		return yes;
	}
}
