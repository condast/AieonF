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

import java.util.Collection;

/**
 * This class contains the general methods for a graph
 * Author:  @author
 * Version: @version
 * Date:    %date%
 */
public interface IModel<T,U>
{

	/**
	 * A root graph always has one root node
	 * @return
	 */
	public IVertex<T> getRoot();
	
	/**
	 * Get the list of vertices containing the given object.
	 * Returns null if none were found
	 * @param obj T
	 * @return Vertex
	 */
	public Collection<IVertex<T>> getVertices( T obj );

	/**
	 * Returns the edges containing the given object.
	 * Returns null if none were found
	 * @param obj U
	 * @return List
	 */
	public Collection<IEdge<T,U>> getEdges( U obj );

	/**
	 * Insert and return an edge between two vertices V and W. Object object is
	 * stored in the edge
	 *
	 * @param vertexV Vertex
	 * @param vertexW Vertex
	 * @param object Object
	 * @return Edge
	 */
	public void add( IEdge<T,U> edge );

	/**
	 * Remove and return an edge between two vertices V and W. Object object is
	 * stored in the edge
	 *
	 * @param vertexV Vertex
	 * @param vertexW Vertex
	 * @param object Object
	 * @return Edge
	 */
	public void remove( IEdge<T,U> edge );

	/**
	 * Insert and return an edge between two vertices V and W. Object object is
	 * stored in the edge
	 *
	 * @param vertexV Vertex
	 * @param vertexW Vertex
	 * @param object Object
	 * @return Edge
	 */
	public IEdge<T, U> insert(IVertex<T> vertexV, IVertex<T> vertexW, U object);
}
