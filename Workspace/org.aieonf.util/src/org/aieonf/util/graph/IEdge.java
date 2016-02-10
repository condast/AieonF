package org.aieonf.util.graph;

import java.util.Collection;

public interface IEdge<T, U> {

	/**
	 * Puts an object
	 *
	 * @param v Vertex
	 * @param w Vertex
	 * @param obj Object
	 */
	public abstract void put(IVertex<T> v, IVertex<T> w, U obj);

	/**
	 * Get an unique id for this vertex
	 * @return
	 */
	public Object getID();

	/**
	 * Get an object
	 *
	 * @return Object
	 */
	public abstract U get();

	/**
	 * Replace an object
	 *
	 * @param obj Object
	 */
	public abstract void replace(U obj);

	/**
	 * Returns the end vertices of the edge
	 *
	 * @return Collection
	 */
	public abstract Collection<IVertex<T>> endVertices();

	/**
	 * Returns true if the edge contains a given vertex
	 *
	 * @param vertex Vertex
	 * @return boolean
	 */
	public abstract boolean containsVertex(IVertex<T> vertex);

	/**
	 * Returns the first vertex entered
	 *
	 * @return Vertex
	 */
	public abstract IVertex<T> first();

	/**
	 * Returns the last vertex entered
	 *
	 * @return Vertex
	 */
	public abstract IVertex<T> last();

	/**
	 * Returns the endpoint of an edge distinct from a given vertex, and null if
	 * the vertex is invalid
	 *
	 * @param vertex Vertex
	 * @return Vertex
	 */
	public abstract IVertex<T> opposite(IVertex<T> vertex);

}