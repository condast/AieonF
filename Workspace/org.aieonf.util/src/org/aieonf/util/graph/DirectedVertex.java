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

/**
  This class implements the vertex for a directed graph. It extends the vertex
  for an undirected graph
*/
public class DirectedVertex<T> extends Vertex<T>
{
  /**
    The in- and outDegree represent the number of incoming and outgoing
    edges of this vertex
  */
  private int inDegree;
  private int outDegree;

  /**
   * CONSTRUCTOR: initialize this vertex
   */
  public DirectedVertex()
  {
    super();
    inDegree = 0;
    outDegree = 0;
  }

  /**
   * CONSTRUCTOR: initialize this vertex
   *
   * @param object Object
  */
  public DirectedVertex( T object )
  {
    super( object );
    inDegree = 0;
    outDegree = 0;
  }

  /**
   * Returns the inDegree of this vertex
   *
   * @return int
  */
  public int inDegree()
  {
    return inDegree;
  }

  /**
   * Increases and returns the inDegree of this vertex. It modifies the
   * degree (inDegree + outDegree)
   *
   * @return int
  */
  public int addInDegree()
  {
    inDegree++;
    super.addDegree();
    return inDegree;
  }

  /**
   * Decreases and returns the inDegree of this vertex. The total degree
   * (inDegree + outDegree) is modified
   *
   * @return int
   */
  public int removeInDegree()
  {
    if (inDegree > 0) {
      inDegree--;
      super.removeDegree();
    }
    return inDegree;
  }

  /**
   * Returns the outDegree of this vertex
   *
   * @return int
  */
  public int outDegree()
  {
    return outDegree;
  }

  /**
   * Increases and returns the outDegree of this vertex. It modifies the
   * degree (inDegree + outDegree)
   *
   * @return int
   */
  public int addOutDegree()
  {
    outDegree++;
    super.addDegree();
    return outDegree;
  }

  /**
   * Decreases and returns the outDegree of this vertex. The total degree
   * (inDegree + outDegree) is modified
   *
   * @return int
  */
  public int removeOutDegree()
  {
    if (outDegree > 0) {
      outDegree--;
      super.removeDegree();
    }
    return outDegree;
  }
}
