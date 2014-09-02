/**
 * <p>Title: Conceptual Network Database</p>
 * <p>Description: Stores concepts; XML pages that describe functions</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Condast BV</p>
 * @author Kees Pieters
 * @version 1.0
 */
package org.aieonf.concept;

import org.aieonf.concept.core.ConceptException;
import org.aieonf.util.StringStyler;

/**
 * Defines a concept, basically a wrapper around the underlying XML definition
 */
public interface IExtendedConcept extends IDescriptor
{
	//The concept name
	public static final String CONCEPT = "Concept";

	//This interface defines the relationships with other concepts
	public final String ATTRIBUTES = "Attributes";

	/**
	 * The basic elements of a concept
	 */
	public static enum Attribute
	{
		AUTHOR,
		COPYRIGHT,
		HOTLISTED,
		PROPATOR,
		WEIGHT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}

	/**
	 * Get the propator of the concept.
	 * This is the original source that created the concept,
	 * and should not be altered
	 *
	 * @return String
	 */
	public String getPropator();

	/**
	 * Set the propator of the concept
	 * This is the original source that created the concept
	 *
	 * @param propator String
	 * @throws ConceptException
	 */
	public void setPropator( String propator ) throws ConceptException;

	/**
	 * Returns true if the concept is hot-listed
	 *
	 * @return boolean
	 * @throws ConceptException
	 */
	public boolean isHotlisted() throws ConceptException;

	/**
	 * Get or set the hot-list option
	 *
	 * @param hotlist boolean
	 * @throws ConceptException
	 */
	public void setHotlist( boolean hotlist ) throws ConceptException;

	/**
	 * A concept by definition has a 'weight' that can be used for popularity, etc.
	 * The user determines the meaning.
	 *
	 * @return int
	 * @throws ConceptException
	 */
	public int getWeight() throws ConceptException;

	/**
	 * A concept by definition has a 'weight' that can be used for popularity, etc.
	 * The user determines the  meaning.
	 *
	 * @param weight int
	 * @throws ConceptException
	 */
	public void setWeight( int weight ) throws ConceptException;
}
