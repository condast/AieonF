/**
 * Title:        Graph
 * Version:
 * Copyright:    Copyright (c) 2001
 * Author:       Kees Pieters
 * Company:      Condast B.V.
 * Description:  This package implements a number of
 * classes that implement graphs
*/
package org.aieonf.util.graph;

//J2SE imports
import java.util.*;

/**
 * This class implements an adjacency list representing a graph
 * NOT TESTED YET
*/
public class AdjacencyList<T,U> extends EdgeList<T,U>
{

  /**
   CONSTRUCTOR:
  */
  public AdjacencyList()
  {
    super();
  }

  /**
   * Returns an enumeration of the vertices adjacent to a vertex
   *
   * @param vertex AdjacencyVertex
   * @return Enumeration
  */
  public List<IVertex<T>> adjacentVertices( AdjacencyVertex<T,U> vertex )
  {
    List<IVertex<T>> adjacent = new ArrayList<IVertex<T>>();
    Collection<Edge<T,U>> edges = vertex.getEdges();

    // Collect all the opposites of vertex
    for ( IEdge<T, U> edge: edges ){
      if ( edge.containsVertex( vertex ) == true ) {
        adjacent.add(edge.opposite( vertex ));
      }
    }
    return adjacent;
  }

  /**
   * Returns an enumeration of the edges incident to a vertex
   *
   * @param vertex AdjacencyVertex
   * @return Collection
   */
  public Collection<Edge<T,U>> incidentEdges( AdjacencyVertex<T,U> vertex )
  {
    return vertex.incidentEdges();
  }

  /**
   * Returns true if two vertices v and v are adjacent
   *
   * @param vertexV AdjacencyVertex
   * @param vertexW AdjacencyVertex
   * @return boolean
  */
  public boolean areAdjacent( AdjacencyVertex<T,U> vertexV, AdjacencyVertex<T,U> vertexW )
  {
    Collection<Edge<T,U>> edgesV = vertexV.getEdges();
    Collection<Edge<T,U>> edgesW = vertexW.getEdges();
    Collection<Edge<T,U>> edges;

    // Look for at least one edge that connect v and w
    if (edgesV.size() < edgesW.size()) {
      edges = edgesV;
    } else {
      edges = edgesW;
    }

    for ( IEdge<T, U> edge: edges ){
      if (( vertexV.equals( edge.opposite( vertexW ))) ||
          ( vertexW.equals( edge.opposite( vertexW )))) {
        return true;
      }
    }
    return false;
  }
}
