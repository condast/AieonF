/**
 * Title:        Graph
 * Version:
 * Copyright:    Copyright (c) 2001
 * Author:       Kees Pieters
 * Company:      Condast B.V.
 * Description:  This package implements a number of
 *               classes that implement graphs
*/
package org.aieonf.util.graph;

/**
 This class implements the vertex object
*/
public class Vertex<T> implements Comparable<Vertex<T>>, IVertex<T>
{

	/**
	 * The actual object contained in the vertex
	 */
	private T object;

	/**
	 * The degree is the number of in- and outgoing edges for this vertex.
	 */
	private int degree;

	/**
    CONSTRUCTOR: create and initialize a vertex object
	 */
	public Vertex()
	{
		object = null;
		degree = 0;
	}

	/**
	 * CONSTRUCTOR: create and initialize a vertex object, with a given object
	 *
	 * @param obj Object
	 */
	public Vertex( T obj )
	{
		this();
		object = obj;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IVertex#put(T)
	 */
	@Override
	public void put( T obj )
	{
		object = obj;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IVertex#get()
	 */
	@Override
	public T get()
	{
		return object;
	}

	@Override
	public Object getID(){
		return this.hashCode();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IVertex#remove()
	 */
	@Override
	public void remove()
	{
		object = null;
	}

	/**
	 * Gets and returns the degree of this vertex
	 *
	 * @return int
	 */
	public int degree()
	{
		return degree;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IVertex#addDegree()
	 */
	@Override
	public int addDegree()
	{
		degree++;
		return degree;
	}

	/**
	 * Decreases and returns the degree of this vertex
	 *
	 * @return int
	 */
	@Override
	public void removeDegree()
	{
		if (degree > 0) {
			degree--;
		}
	}

	@Override
	public int compareTo( Vertex<T> o) {
		return ( this.hashCode() - o.hashCode() );
	}
}
