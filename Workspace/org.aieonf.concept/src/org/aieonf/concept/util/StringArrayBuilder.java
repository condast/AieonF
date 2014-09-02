package org.aieonf.concept.util;

import java.util.*;

import org.aieonf.concept.IConcept;

/**
 * Builds a string of an array of selected attributes. These can be sent to, for instance,
 * a browser for display.
 * @author k.pieters
 *
 */
public class StringArrayBuilder 
{
	/**
	 * The attributes that have to be rperesented in the array
	*/
	private List<String> attributes;
	
	/**
	 * Create a builder using the given list of attributes
	 * @param attributes
	
	public StringArrayBuilder( List<String> attributes )
	{
		this.attributes = attributes;
	}
	
	/**
	 * Add an attribute to the list
	 * @param attribute
	*/
	public void add( String attribute )
	{
		this.attributes.add( attribute );
	}
	
	/**
	 * Remove an attribute from the list
	 * @param attribute
	*/
	public void remove( String attribute )
	{
		this.attributes.remove( attribute );
	}

	/**
	 * Get the size of the attribute list
	 * @return
	 */
	public int size()
	{
		return this.attributes.size();
	}
	
	/**
	 * Get the attribute at the given index location
	 * @param index
	 * @return
	 */
	public String getAttribute( int index )
	{
		return this.attributes.get( index );
	}

	/**
	 * Get the list of attributes as a string array
	 * @param concept
	 * @return
	*/
	public String attributes()
	{
		StringBuffer buffer = new StringBuffer(); 
		buffer.append( "[" );
		String attribute;
		for( int i = 0; i < attributes.size(); i++ ){
			attribute = attributes.get( i );
			buffer.append( attribute );
			if( i < attributes.size() - 1 )
				buffer.append( ",");
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * build a concept as an array of attributes
	 * @param concept
	 * @return
	 */
	public String build( IConcept concept )
	{
		StringBuffer buffer = new StringBuffer(); 
		buffer.append( "[" );
		String attribute;
		for( int i = 0; i < attributes.size(); i++ ){
			attribute = attributes.get( i );
			buffer.append( concept.get( attribute ));
			if( i < attributes.size() - 1 )
				buffer.append( ",");
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * build a list of concepts as a multi-dimensional array of attributes
	 * @param concept
	 * @return
	*/
	public String build( List<IConcept> concepts )
	{
		StringBuffer buffer = new StringBuffer(); 
		buffer.append( "[" );
		for( IConcept concept: concepts ){
			buffer.append( build( concept ));
		}
		buffer.append("]");
		return buffer.toString();
	}
}
