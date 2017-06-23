package org.aieonf.commons.graph;

import org.aieonf.commons.strings.StringStyler;

public interface IVertex<T extends Object>{

	public static enum Attributes{
		DEGREE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

	}

	/**
	 * Get an unique id for this vertex
	 * @return
	 */
	public Object getID();
	
	/**
	 * Puts an object
	 *
	 * @param obj Object
	 */
	public abstract void put(T obj);

	/**
	 * Get an object
	 *
	 * @return Object
	 */
	public abstract T get();

	/**
	 * Removes an object
	 */
	public abstract void remove();

	/**
	 * Increases and returns the degree of this vertex
	 *
	 * @return int
	 */
	public abstract int addDegree();

	public abstract void removeDegree();
}