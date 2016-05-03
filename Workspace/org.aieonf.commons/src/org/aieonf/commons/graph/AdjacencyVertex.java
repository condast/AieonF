/**
 * Title:        Graph
 * Version:
 * Copyright:    Copyright (c) 2001
 * Author:       Kees Pieters
 * Company:      Condast B.V.
 * Description:  This package implements a number of
 * classes that implement graphs
*/
package org.aieonf.commons.graph;

//J2SE imports
import java.util.*;

/**
 * This class implemets the vertex for an adjacency list structure.
 * NOT TESTED YET
*/
public class AdjacencyVertex<T,U> extends Vertex<T>
{

  /**
   This vector contains a list of in- and outgoing edges
  */
  List<Edge<T,U>> edges;

  /**
   CONSTRUCTOR: Initialize vertex
  */
  public AdjacencyVertex()
  {
    super();
    edges  = new ArrayList<Edge<T,U>>();
  }

  /**
   * Gets the adjacency list of edges
   *
   * @return List
  */
  public Collection<Edge<T,U>> getEdges()
  {
    return edges;
  }

  /**
   * Gets an enumeration of incident edges
   *
   * @return Enumeration
  */
  public Collection<Edge<T,U>> incidentEdges()
  {
    return edges;
  }

  /**
   * Adds an edge to the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
  */
  public int addEdge(Edge<T,U> edge)
  {
    edges.add(edge);
    super.addDegree();
    if (edges.size() == super.degree()) {
      return super.degree();
    } else {
      return -1;
    }
  }

  /**
   * Removes an edge from the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
   */
  public int removeEdge(IEdge<T, U> edge)
  {
    if (edges.contains(edge) == false) {
      return -1;
    } else {
      edges.remove(edge);
      super.removeDegree();
      if (edges.size() == super.degree()) {
        return super.degree();
      } else {
        return -1;
      }
    }
  }
}
