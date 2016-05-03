/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

import java.io.*;
import java.util.Iterator;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.core.ConceptException;

/**
 * Defines a concept, basically a wrapper around the underlying XML definition
 */
public interface IDescriptor extends IDescribable<IDescriptor>, Serializable, Cloneable, Comparable<IDescriptor>
{
	/**
	 * Defines the name
	 */
	public static final String DESCRIPTOR  = "Descriptor";

	/**
	 * The basic elements of a descriptor
	 */
	public enum Attributes
	{
		ID,
		NAME,
		VERSION,
		DESCRIPTION,
		CREATE_DATE,
		UPDATE_DATE,
		CLASS,
		SIGNATURE,
		PROVIDER,
		PROVIDER_NAME,
		EXTENDED_KEY;

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
	 * Get the name of the changeable object
	 * @return
	 */
	public String getName();

	/**
	 * Get the id of the changeable object
	 * @return
	 */
	public String getID();

	/**
	 * Get the version of the changeable object
	 * @return
	 */
	public int getVersion();

	/**
	 * Clears ALL the attributes of the descriptor
	 */
	public void clear();

	/**
	 * Get the value belonging to a key of the concept
	 *
	 * @param key String
	 * @return String
	 */
	public String get( String key );

	/**
	 * Set the value belonging to a key of the concept
	 *
	 * @param key String
	 * @param value String
	 */
	void set( String key, String value );

	/**
	 * Get the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	public String get( Enum<?> enm );

	/**
	 * Set the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	public void set( Enum<?> enm, String value );

	/**
	 * Most concepts will use an extended key format for their attributes.
	 * This get function will get the attributes that are grouped this way 
	 * @param enm
	 * @param name
	 * @return
	 */
	public String getFromExtendedKey( String key );

	/**
	 * Set the version of the concept
	 *
	 * @param version int
	 */
	public void setVersion( int version );
	/**
	 * Get the signature. May be null if the concept has not been assigned to
	 * a database
	 *
	 * @return String
	 */
	public String getSignature();

	/**
	 * Get the description of the concept (default public)
	 *
	 * @return String
	 */
	public String getDescription();

	/**
	 * Set the description of the concept
	 *
	 * @param description String
	 */
	public void setDescription( String description );

	/**
	 * Get the class name of the concept
	 *
	 * @return String
	 */
	public String getClassName();

	/**
	 * Get the provider of the concept.
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @return String
	 */
	public String getProvider();

	/**
	 * Set the provider of the concept
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @param propator String
	 */
	public void setProvider( String provider );

	/**
	 * Get the common name of the provider of the concept.
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @return String
	 */
	public String getProviderName();

	/**
	 * Set the common name of the provider of the concept
	 * This is the source that provides the concept (usually a database or a provider)
	 *
	 * @param propator String
	 */
	public void setProviderName( String provider );

	/**
	 * Sign the given descriptor 
	 * @param signature
	 * @throws ConceptException
	 */
	public void sign( String signature ) throws ConceptException;

	/**
	 * Get the boolean value of a concept. Throws an eception if the
	 * attribute is not a boolean
	 *
	 * @param attribute String
	 * @return boolean
	 */
	public boolean getBoolean( String attribute );

	/**
	 * Get the integer representation of an attribute. Throws an exception if
	 * the attribute does not have the correct format
	 *
	 * @param attribute String
	 * @return int
	 */
	public int getInteger( String attribute );

	/**
	 * Returns true if the given object is equal to this one
	 * (is equals at object level)
	 *
	 * @param obj Object
	 * @return boolean
	 */
	public boolean objEquals( Object obj );

	/**
	 * Returns a string representation of the descriptor (XML)
	 *
	 * @return String
	 */
	@Override
	public String toString();

	/**
	 * Returns a array string representation of the descriptor
	 * [name, id, version]
	 *
	 * @return String
	 */
	public String toStringArray();

	/**
	 * Create the xML representation of this descriptor
	 *
	 * @return String
	 * @throws Exception
	 */
	public String toXML() throws Exception;

	/**
	 * Get an enumeration of the concept properties keys
	 *
	 * @return Enumeration
	 */
	public Iterator<String> iterator();

	/**
	 * The size of a concept is defined as the amount of properties of the concept
	 *
	 * @return int
	 */
	public int size();

	/**
	 * Get the viewer for this descriptor
	 * @return
	 */
	public IDescriptorViewer getViewer();

}
