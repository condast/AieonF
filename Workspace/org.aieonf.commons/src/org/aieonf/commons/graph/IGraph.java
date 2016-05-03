/**
 * Title:        Graph
 * Version:
 * Copyright:    Copyright (c) 2001
 * Author:       Kees Pieters
 * Company:      Condast B.V.
 * Description:  This package implements a number of
 *               classes that implement graphs
*/
package org.aieonf.commons.graph;

import java.util.Collection;

/**
 * This class contains the general methods for a graph
 * Author:  @author
 * Version: @version
 * Date:    %date%
 */
public interface IGraph<T,U>
{

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
   Returns the number of vertices of the graph
   *
   * @return int
  */
  public int numVertices();

  /**
   * Returns the number of edges of the graph
   *
   * @return int
  */
  public int numEdges();

  /**
   * Returns a collection of the edges of the graph
   *
   * @return Collection
  */
  public Collection<IEdge<T,U>> edges();

  /**
   * Returns a collection of the vertices of the graph
   *
   * @return Collection
  */
  public Collection<IVertex<T>> vertices();

  /**
   * Returns a selection of all the possible contents of the graph. This can
   * be the contents of the vertices or the edges, depending on which
   * representation is used
   *
   * @return List
  */
  public Collection<T> selection();

  /**
   * Returns an enumeration of the vertices adjacent to a vertex
   *
   * @param vertex Vertex
   * @return Enumeration
  */
  public Collection<IVertex<T>> adjacentVertices( IVertex<T> vertex );

  /**
   Returns an enumeration of the edges incident to a vertex
   *
   * @param vertex Vertex
   * @return Enumeration
  */
  public Collection<IEdge<T,U>> incidentEdges( IVertex<T> vertex );

  /**
   * Returns true if two vertices v and W are adjacent
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @return boolean
  */
  public boolean areAdjacent( IVertex<T> vertexV, IVertex<T> vertexW );

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
   * Insert and return an edge between two vertices V and W. Object object is
   * stored in the edge
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @param object Object
   * @return Edge
   */
  public IEdge<T, U> insert( IVertex<T> vertexV, IVertex<T> vertexW, U object );

  /**
   * Insert and return a vertex and store Object object at this position
   *
   * @param object Object
   * @return Vertex
   */
  public IVertex<T> insert( T object );

  /**
   * Replace vertex V with vertex W
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
  */
  public void replace(IVertex<T> vertexV, Vertex<T> vertexW);

  /**
   * Replace edge X with edge Y
   *
   * @param edgeX Edge
   * @param edgeY Edge
  */
  public void replace(IEdge<T, U> edgeX, IEdge<T,U> edgeY);

  /**
   * Swap vertex V and vertex W
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
  */
  public void swap(IVertex<T> vertexV, IVertex<T> vertexW);

  /**
   * Swap edge X and edge Y
   *
   * @param edgeX Edge
   * @param edgeY Edge
  */
  public void swap(IEdge<T, U> edgeX, IEdge<T, U> edgeY);
}
