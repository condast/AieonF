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
 * A directed edgelist implementation of a graph
*/
public class DirectedEdgeList<T,U> extends EdgeList<T,U>
{

  /**
    This class implements a directed graph, using an edge list data structure
  */
  public DirectedEdgeList()
  {
    super();
    super.setDirected();
  }

  /**
   * This class implements a directed graph, using an edge list data structure
   * and whose edge's contents are sorted according to the given comparator
   *
   * @param comparator Comparator
  */
  public DirectedEdgeList( Comparator<U> comparator )
  {
    super( comparator );
    super.setDirected();
  }

  /**
   * Get a collection of origins
   * @return
  */
  public Collection<IVertex<T>> getOrigins()
  {
    Collection<IEdge<T,U>> edges = super.edges();
    Iterator<IEdge<T,U>> iterator=  edges.iterator();
    IEdge<T, U> edge;
    Set<IVertex<T>> vertices = new TreeSet<IVertex<T>>();
    IVertex<T> vertex;
    while( iterator.hasNext()){
      edge = iterator.next();
      vertex = edge.first();
      if( vertex == null )
      	continue;
      if( vertices.contains( vertex ))
      	continue;
      vertices.add( vertex );
    }
    return vertices;  	
  }

  /**
   * Get a collection of destinations
   * @return
  */
  public Collection<IVertex<T>> getDestinations()
  {
    Collection<IEdge<T,U>> edges = super.edges();
    Iterator<IEdge<T,U>> iterator=  edges.iterator();
    IEdge<T, U> edge;
    Set<IVertex<T>> vertices = new TreeSet<IVertex<T>>();
    IVertex<T> vertex;
    while( iterator.hasNext()){
      edge = iterator.next();
      vertex = edge.last();
      if( vertex == null )
      	continue;
      if( vertices.contains( vertex ))
      	continue;
      vertices.add( vertex );
    }
    return vertices;  	
  }

  /**
   * Returns an enumeration of the vertices adjacent to a vertex
   *
   * @param vertex DirectedVertex
   * @return Collection
  */
  public Collection<DirectedVertex<T>>adjacentVertices( DirectedVertex<T> vertex )
  {
    Vector<DirectedVertex<T>> adjacent = new Vector<DirectedVertex<T>>();
    DirectedEdge<T,U> edge;

    // Collect all the opposites of vertex
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = ( DirectedEdge<T,U> )iterator.next();
      if( edge == null )
       	continue;

      // Collect all the destinations for edges that have vertex as origin,
      // and the other way round
      if ( edge.origin() == vertex ) {
        adjacent.add(( DirectedVertex<T> )edge.destination() );
      } else {
        if ( edge.destination() == vertex ) {
          adjacent.add(( DirectedVertex<T> )edge.origin() );
        }
      }
    }
    return Collections.list( adjacent.elements() );
  }

  /**
   * Returns an enumeration of the vertices inAdjacent to a vertex. This means
   * along the incoming edges
   *
   * @param vertex DirectedVertex
   * @return Enumeration
  */
  public Collection<DirectedVertex<T>> inAdjacentVertices( DirectedVertex<T> vertex )
  {
    List<DirectedVertex<T>> adjacent = new ArrayList<DirectedVertex<T>>();
    DirectedEdge<T,U> edge;

    // Iterate through the edge list
    // Collect all the opposites of vertex
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = ( DirectedEdge<T,U> )iterator.next();
      if( edge == null )
    	  continue;

      // Collect all the destinations for edges that have vertex as origin,
      // and the other way round
      if ( edge.destination() == vertex) {
        adjacent.add(( DirectedVertex<T> )edge.origin());
      }
    }
    return adjacent;
  }

  /**
   Returns an enumeration of the vertices outAdjacent to a vertex. This means
   along the outgoing edges
   *
   * @param vertex DirectedVertex
   * @return Collection
  */
  public Collection<DirectedVertex<T>> outAdjacentVertices( DirectedVertex<T> vertex )
  {
    List<DirectedVertex<T>> adjacent = new ArrayList<DirectedVertex<T>>();
    DirectedEdge<T,U> edge;

    // Collect all the opposites of vertex
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = ( DirectedEdge<T,U> )iterator.next();
      if( edge == null )
    	  continue;

      // Collect all the destinations for edges that have vertex as origin,
      // and the other way round
      if ( edge.origin() == vertex) {
        adjacent.add(( DirectedVertex<T> )edge.destination());
      }
    }
    return adjacent;
  }

  /**
   * Returns an enumeration of the incoming edges incident to a vertex
   *
   * @param vertex Vertex
   * @return Collection
   */
  public Collection<DirectedEdge<T,U>> inIncidentEdges( IVertex<T> vertex )
  {
    List<DirectedEdge<T,U>> incident = new ArrayList<DirectedEdge<T,U>>();
    DirectedEdge<T,U> edge;

    // Iterate through the edge list
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = ( DirectedEdge<T,U> )iterator.next();
      if( edge == null )
    	  continue;

      // collect all the edges that contain the vertex
      if ( edge.destination() == vertex )
        incident.add( edge );
    }
    return incident;
  }

  /**
   * Returns a collection of the outgoing edges incident to a vertex
   *
   * @param vertex Vertex
   * @return Enumeration
  */
  public Collection<DirectedEdge<T,U>> outIncidentEdges( IVertex<T> vertex )
  {
    List<DirectedEdge<T,U>> incident = new ArrayList<DirectedEdge<T,U>>();
    DirectedEdge<T,U> edge;

    // Iterate through the edge list
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = ( DirectedEdge<T,U> )iterator.next();
      if( edge == null )
    	  continue;

      // collect all the edges that contain the vertex
      if ( edge.origin() == vertex ) {
        incident.add( edge );
      }
    }
    return incident;
  }

  /**
   Returns true if two vertices v and v are adjacent
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @return boolean
  */
  @Override
	public boolean areAdjacent( IVertex<T> vertexV, IVertex<T> vertexW )
  {
    IEdge<T, U> edge;
  	DirectedEdge<T,U> dEdge;

    // Iterate through the edge list
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = iterator.next();
      if( edge == null )
    	  continue;
      
      dEdge = ( DirectedEdge<T,U> )edge;
      // Look for at least one edge that connect v and w
      if (( vertexV == dEdge.opposite( vertexW )) ||
          ( vertexW == dEdge.opposite( vertexV ))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns an enumeration of predecessor edges that are before a given one
   *
   * @param edge DirectedEdge
   * @return Collection
  */
  public Collection<DirectedEdge<T,U>> previous( DirectedEdge<T,U> edge )
  {
    IVertex<T> vertex = edge.origin();
    return this.inIncidentEdges( vertex );
  }

  /**
   * Returns a collection of predecessor edges that of the given one
   *
   * @param edge DirectedEdge
   * @return Collection
  */
  public Collection<DirectedEdge<T,U>> getPredecessors( DirectedEdge<T,U> edge )
  {
    List<DirectedEdge<T,U>> predecessors = new ArrayList<DirectedEdge<T,U>>();
    predecessors.addAll( this.previous( edge ));
    return predecessors;
  }

  /**
   * Returns an Enumeration of successor edges of the given one
   *
   * @param edge DirectedEdge
   * @return Collection
  */
  public Collection<DirectedEdge<T,U>> next( DirectedEdge<T,U> edge )
  {
    IVertex<T> vertex = edge.destination();
    return this.outIncidentEdges( vertex );
  }

  /**
   * Returns an ArrayList of successor edges that of the given one
   *
   * @param edge DirectedEdge
   * @return Collection
   */
  public Collection<DirectedEdge<T,U>> getSuccessors( DirectedEdge<T,U> edge )
  {
    List<DirectedEdge<T,U>> successors = new ArrayList<DirectedEdge<T,U>>();
    successors.addAll( this.next( edge ));
    return successors;
  }

  /**
   * Returns an edge if one exists between two vertices v and w, or null
   * otherwise
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @return Edge
  */
  public IEdge<T,U> exists( IVertex<T> vertexV, IVertex<T> vertexW )
  {
    IEdge<T,U> edge;
  	DirectedEdge<T,U> dEdge;

    // Iterate through the edge list
    Iterator<IEdge<T,U>> iterator = super.iterator();
    while ( iterator.hasNext() == true ) {
      edge = iterator.next();
      if( edge == null )
    	  continue;

      // Look for at least one edge that connect v and w
      dEdge = ( DirectedEdge<T,U> )edge;
      if (( vertexV == dEdge.opposite( vertexW )) ||
          ( vertexW == dEdge.opposite( vertexV ))) {
        return edge;
      }
    }
    return null;
  }
}
