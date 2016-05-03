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
 * This class implements a directed edge. Note that the class Edge, from which
 * this class is extended, implicitely is a directed graph
 *
 * <p>Title: Utilities</p>
 *
 * <p>Description: Varios utilities</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Condast</p>
 *
 * @author Kees Pieters
 * @version 1.0
*/
public class DirectedEdge<T,U> extends Edge<T,U>
{

    /**
     * CONSTRUCTOR: create and initialize an edge object, with a given object
    */
    public DirectedEdge()
    {
      super();
    }

    /**
     * CONSTRUCTOR: create and initialize an edge object, with a given object
     *
     * @param v Vertex
     * @param w Vertex
     * @param obj T
    */
    public DirectedEdge( Vertex<T> v, Vertex<T> w, U obj )
    {
    	super( v, w, obj );
    }

    /**
     * Returns the origin of this edge.
     *
     * @return Vertex
    */
    public IVertex<T> origin()
    {
      return super.first();
    }

    /**
     * Returns the destination of this edge.
     *
     * @return Vertex
    */
    public IVertex<T> destination()
    {
      return super.last();
    }

   /**
    * Changes the direction of this edge.
   */
   public void reverseDirection()
   {
     List<IVertex<T>> vertices = super.endVertices();
     IVertex<T> v = vertices.get( 0 );
     IVertex<T> w = vertices.get( 1 );
     vertices.clear();
     vertices.add( w );
     vertices.add( v );
   }
}
