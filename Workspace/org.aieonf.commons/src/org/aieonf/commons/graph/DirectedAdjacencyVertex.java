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
 * This class implements a vertex for an ajacency list representing a
 * directed graph
 * NOT TESTED YET
*/
public class DirectedAdjacencyVertex<T,U> extends DirectedVertex<T>
{

  /**
   The adjacency list of in- and outgoing edges for this vertex
  */
  List<Edge<T,U>> inEdges;
  List<Edge<T,U>> outEdges;

  /**
   CONSTRUCTOR:
  */
  public DirectedAdjacencyVertex()
  {
    super();
    inEdges  = new ArrayList<Edge<T,U>>();
    outEdges = new ArrayList<Edge<T,U>>();
  }

  /**
   * Gets the adjacency list of ingoing edges
   *
   * @return List
   */
  public List<Edge<T,U>> getInEdges()
  {
    return inEdges;
  }

  /**
   * Gets the adjacency list of outgoing edges
   *
   * @return List
  */
  public List<Edge<T,U>> getOutEdges()
  {
    return outEdges;
  }


  /**
   * Adds an in-going edge to the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
  */
  public int addInEdge(Edge<T,U> edge)
  {
    inEdges.add(edge);
    super.addInDegree();
    if (inEdges.size() == super.inDegree()) {
      return super.inDegree();
    } else {
      return -1;
    }
  }

  /**
   * Removes an ingoing edge from the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
  */
  public int removeInEdge(IEdge<T, U> edge)
  {
    if (inEdges.contains(edge) == false) {
      return -1;
    } else {
      inEdges.remove(edge);
      super.removeInDegree();
      if (inEdges.size() == super.inDegree()) {
        return super.inDegree();
      } else {
        return -1;
      }
    }
  }

  /**
   * Adds an outgoing edge to the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
  */
  public int addOutEdge(Edge<T,U> edge)
  {
    outEdges.add(edge);
    super.addOutDegree();
    if (outEdges.size() == super.outDegree()) {
      return super.outDegree();
    } else {
      return -1;
    }
  }

  /**
   * Removes an outgoing edge from the edge list and returns the degree, or -1
   * if the size of edges does not match the degree
   *
   * @param edge Edge
   * @return int
  */
  public int removeOutEdge(IEdge<T, U> edge)
  {
    if (outEdges.contains(edge) == false) {
      return -1;
    } else {
      outEdges.remove(edge);
      super.removeOutDegree();
      if (outEdges.size() == super.outDegree()) {
        return super.outDegree();
      } else {
        return -1;
      }
    }
  }
}
