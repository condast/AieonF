package org.aieonf.concept.core;

import java.util.Iterator;
import java.util.Map;

public interface IConceptBase extends Cloneable{

	public static final String NULL = "null";
	//The error messages
	public static final String S_ERR_INVALID_ATTRIBUTE = "The attribute does not exist or is not valid for this operation";
	public static final String S_KEY_NOT_FOUND_ERR = "Key not found";

	/**
	 * If true, the values have changed
	 * @return
	 */
	public abstract boolean hasChanged();

	/**
	 * Set the update flag.
	 * @param choice
	 */
	public abstract void setChanged(boolean choice);

	/**
	 * Clears ALL the entries of the concept, leaving the object
	 * to be more or less an empty shell.
	 */
	public abstract void clear();

	/**
	 * Get the value belonging to a key of the concept
	 *
	 * @param key String
	 * @return String
	 */
	public abstract String get(String key);

	/**
	 * Set the value belonging to a key of the concept
	 *
	 * @param key String
	 * @param value String
	 */
	public abstract void set(String key, String value);

	/**
	 * Get the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	public abstract String get(Enum<?> enm);

	/**
	 * Set the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	public void set( Enum<?> enm, String value );

	/**
	 * Remove the attribute value associated with the given enum 
	 * @param enm
	 * @param name
	 * @return
	 */
	public abstract void remove(Enum<?> enm);

	/**
	 * Get the boolean value of a concept. Throws an exception if the
	 * attribute is not a boolean
	 *
	 * @param attribute String
	 * @return boolean
	 */
	public abstract boolean getBoolean(String attribute);

	/**
	 * Get the integer representation of an attribute. Throws an exception if
	 * the attribute does not have the correct format
	 * @param attribute String
	 * @return int
	 */
	public abstract int getInteger(String attribute);

	/**
	 * Remove the value belonging to a key of the concept
	 *
	 * @param key String
	 */
	public abstract void remove(String key);

	/**
	 * The size of a concept is defined as the amount of properties of the concept
	 *
	 * @return int
	 */
	public abstract int size();

	/**
	 * Get an iterator of the concept properties
	 *
	 * @return Iterator<String>
	 */
	public abstract Iterator<String> keySet();

	/**
	 * Get an iterator of the concept properties
	 *
	 * @return Iterator<String>
	 */
	public abstract Iterator<Map.Entry<String, String>> iterator();
}