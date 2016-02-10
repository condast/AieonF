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

//J2SE imports
import java.util.*;

/**
 * This class implements a graph as a list of edges. The graph implements the
 * Collections interface. All the methods are synchronized
 *
*/
public class EdgeList<T, U> implements IGraph<T,U>
{
  /**
   * A list of vertices and a list of edges. They are represented internally as
   * tree maps with the objects they contain as keys
  */
  private Map<T, IVertex<T>> verticeMap;
  private Map<U, IEdge<T,U>> edgeList;

  /**
   *  Determines whether the graph is directed or not
  */
  private boolean directedGraph = false;

  /**
   * CONSTRUCTOR: initialise edges and vertices
  */
  public EdgeList()
  {
    verticeMap = Collections.synchronizedMap( new HashMap<T, IVertex<T>>() );
    edgeList    = Collections.synchronizedMap( new HashMap<U, IEdge<T,U>>() );
    this.clear();
  }

  /**
   * CONSTRUCTOR: initialise edges and vertices and stores the edges
   *              in a sorting order of the contents of the edges,
   *              based on the comparator.
   *
   * @param edgeComparator Comparator
  */
  public EdgeList( Comparator<U> edgeComparator )
  {
    verticeMap = Collections.synchronizedMap( new HashMap<T, IVertex<T>>() );
    edgeList   = Collections.synchronizedMap( new TreeMap<U, IEdge<T,U>>( edgeComparator ));
    this.clear();
  }

  /**
   * Clears the graph
  */
  public synchronized void clear()
  {
    verticeMap.clear();
    edgeList.clear();
    directedGraph = false;
  }

  /**
   * Get the verticeList
   *
   * @return Map
  */
  protected synchronized Map<T, IVertex<T>> getVerticeList()
  {
    return verticeMap;
  }

  /**
   * Get the edgeList
   *
   * @return Map
  */
  protected synchronized Map<U, IEdge<T,U>> getEdgeList()
  {
    return edgeList;
  }

  /**
   * Get the edges of the graph
   */
  @Override
	public Collection<IEdge<T,U>> edges()
  {
    return this.edgeList.values();
  }

  /**
   * Get the list of vertices containing the given object.
   * Returns null if none were found
   * @param obj T
   * @return Vertex
  */
  @Override
	public List<IVertex<T>> getVertices( Object obj )
  {
    List<IVertex<T>> results = new ArrayList<IVertex<T>>();
    for( IVertex<T> vertex: this.vertices() ){
      if(( vertex.get() != null ) && ( vertex.get().equals( obj )))
        results.add( vertex );
    }
    if( results.size() == 0 )
      return null;
    return results;
  }

  /**
   * Returns the edges containing the given object.
   * Returns null if none were found
   * @param obj U
   * @return List
   */
  @Override
	public List<IEdge<T,U>> getEdges( Object obj )
  {
    List<IEdge<T,U>> results = new ArrayList<IEdge<T,U>>();
    for( IEdge<T,U> edge: this.edges() ){
      if( edge.get().equals( obj ))
        results.add( edge );
    }
    if( results.size() == 0 )
      return null;
    return results;
  }

  /**
   * Returns directedGraph
   *
   * @return boolean
  */
  public synchronized boolean isDirected()
  {
    return directedGraph;
  }

  /**
   * Sets directedGraph
  */
  public synchronized void setDirected()
  {
    directedGraph = true;
  }

  /**
   * Resets directedGraph
  */
  public synchronized void resetDirected()
  {
    directedGraph = false;
  }

  /**
   * Returns the number of vertices of the graph
   *
   * @return int
  */
  @Override
	public synchronized int numVertices()
  {
    return verticeMap.size();
  }

  /**
   * Returns the number of edges of the graph
   *
   * @return int
  */
  @Override
	public synchronized int numEdges()
  {
    return edgeList.size();
  }

  /**
   * The size is defined as the number of edges in the edge list
   *
   * @return int
  */
  public synchronized int size()
  {
    return edgeList.size();
  }

  /**
   * Returns true if the graph is empty
   *
   * @return boolean
  */
  public synchronized boolean isEmpty()
  {
    return edgeList.isEmpty();
  }

  /**
   * Returns the hash code for this collection
   *
   * @return int
  */
  @Override
	public synchronized int hashCode ()
  {
    return edgeList.hashCode();
  }

  /**
   * Returns an iterator for the edges of the graph
   *
   * @return Iterator
  */
  public synchronized Iterator<IEdge<T,U>> iterator()
  {
     return edgeList.values().iterator();
  }

  /**
   * Returns an array of the edges of the graph
   *
   * @return Object[]
  */
  public synchronized Object[] toArray()
  {
    Collection<IEdge<T,U>> edgeCollection = edgeList.values();
    return edgeCollection.toArray();
  }

  /**
   * Returns an array containing all the elements in this collection, whose
   * type is that of the given array
   *
   * @param array Object[]
   * @return Object[]
  */
  public synchronized Object[] toArray( Object[] array )
  {
    Collection<IEdge<T,U>> edgeCollection = edgeList.values();
    return edgeCollection.toArray( array );
  }

  /**
   * Returns an set of the vertices of the graph
   *
   * @return Collection
  */
  @Override
	public Collection<IVertex<T>> vertices()
  {
    return verticeMap.values();
  }

  /**
   * Returns an Vector of the vertices of the graph
   *
   * @return Collection
  */
  @Override
	public synchronized Collection<T> selection()
  {
    Map<T, IVertex<T>> verts = verticeMap;
    List<T> select = new ArrayList<T>();
    select.addAll( verts.keySet() );
    return select;
  }

  /**
   * Returns an Vector of the contents of the vertices
   *
   * @return Collection
  */
  public synchronized Collection<T> getVerticesContents()
  {
    List<T> verticeContents = new ArrayList<T>( verticeMap.keySet() );
    return verticeContents;
  }

  /**
   * Returns an Vector of the contents of the edges
   *
   * @return Collection
  */
  public synchronized Collection<U> getEdgesContents()
  {
    List<U> edgeContents = new ArrayList<U>( edgeList.keySet() );
    return edgeContents;
  }

  /**
   * Returns true if an edge of vertex is contained in this graph,
   * otherwise it returns false
   *
   * @param object Object
   * @return boolean
  */
  public synchronized boolean contains ( Object object )
  {
    if( object instanceof Edge<?,?> ){
      return( edgeList.containsValue( object ));
    }

    if( object instanceof Vertex<?> ){
      return ( verticeMap.containsValue( object ) == true );
    }// endif
    return false;
  }

  /**
   * Returns true if an edge between vertexV and vertexW already exists,
   * otherwise it returns false
   *
   * @param edge Edge
   * @return boolean
  */
  public synchronized boolean contains ( IEdge<T, U> edge )
  {
    return( edgeList.containsValue( edge ));
  }

  /**
   * Returns true if a given vertex exists
   *
   * @param vertex Vertex
   * @return boolean
  */
  public synchronized boolean contains( IVertex<T> vertex )
  {
    return ( verticeMap.containsValue( vertex ) == true );
  }

  /**
   * Returns true if a vertex with the given contents exists
   * exist
   *
   * @param contents Object
   * @return boolean
  */
  public synchronized boolean containsVertexContents( Object contents )
  {
    return ( verticeMap.containsKey( contents ) == true );
  }

  /**
   * Returns true if all the edges of the collection exist in the graph,
   * otherwise it returns false
   *
   * @param edgeCollection Collection
   * @return boolean
  */
  public synchronized boolean containsAll ( Collection<IEdge<T,U>> edgeCollection )
  {
    List<IEdge<T,U>> edgeArray = new ArrayList<IEdge<T,U>>( edgeList.values() );
    return( edgeArray.containsAll( edgeCollection ));
  }

  /**
   * Returns a vertex with the given contents or null if this vertex doesn't
   *
   * @param contents Object
   * @return Vertex
  */
  public IVertex<T> getVertex( Object contents )
  {
    return verticeMap.get( contents );
  }

  /**
   * Returns true if an edge with the given contents exists
   *
   * @param contents Object
   * @return boolean
  */
  public synchronized boolean containsEdgeContents( Object contents )
  {
    return ( edgeList.containsKey( contents ) == true );
  }

  /**
   * Returns a vertex with the given contents or null if this vertex doesn't
   * exist
   *
   * @param contents Object
   * @return Edge
  */
  public synchronized IEdge<T, U> getEdge( Object contents )
  {
    if( edgeList.containsKey( contents ) == true ){
      return edgeList.get( contents );
    } else {
      return null;
    }
  }

  /**
   * Returns the edge between two given vertices or null if none was found
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @return Edge
  */
  public IEdge<T, U> getEdge( IVertex<T> vertexV, IVertex<T> vertexW )
  {
    // Iterate through the edge list
    synchronized( edgeList ){
      Collection<IEdge<T,U>> edges = edgeList.values();
      Iterator<IEdge<T,U>> iterator = edges.iterator();
      IEdge<T, U> edge;
      while ( iterator.hasNext() == true ) {
        edge = iterator.next();
        if( edge != null ){
          if(( edge.containsVertex( vertexV ) == true ) &&
             ( edge.containsVertex( vertexW ) == true )){
            return edge;
          }
        }
      }
    }
    return null;
  }

  /**
   * Returns an enumeration of the vertices adjacent to a vertex
   *
   * @param vertex Vertex
   * @return Collection
  */
  @Override
	public synchronized Collection<IVertex<T>> adjacentVertices( IVertex<T> vertex )
  {
    List<IVertex<T>> adjacent = new ArrayList<IVertex<T>>();
    IEdge<T, U> edge;

    // Iterate through the edge list
    Set<Map.Entry<U, IEdge<T,U>>> edgeSet = Collections.synchronizedSet( edgeList.entrySet() );
    Iterator<Map.Entry<U, IEdge<T,U>>> iterator = edgeSet.iterator();
    Map.Entry<U, IEdge<T,U>> mapEntry;

    // Collect all the opposites of vertex
    while ( iterator.hasNext() == true ) {
      mapEntry = iterator.next();
      edge = mapEntry.getValue();
      if( edge != null ){
        if ( edge.containsVertex( vertex ) == true ) {
          adjacent.add( edge.opposite( vertex ));
        }
      }
    }
    return adjacent;
  }

  /**
   * Returns an enumeration of the edges incident to a vertex
   *
   * @param vertex Vertex
   * @return Collection
  */
  @Override
	public synchronized Collection<IEdge<T,U>> incidentEdges( IVertex<T> vertex )
  {
    List<IEdge<T,U>> incident = new ArrayList<IEdge<T,U>>();
    IEdge<T,U> edge;

    // Iterate through the edge list
    Set<Map.Entry<U, IEdge<T,U>>> edgeSet = Collections.synchronizedSet( edgeList.entrySet() );
    Iterator<Map.Entry<U, IEdge<T,U>>> iterator = edgeSet.iterator();
    Map.Entry<U, IEdge<T,U>> mapEntry;

    // Collect all the opposites of vertex
    while ( iterator.hasNext() == true ) {
      mapEntry = iterator.next();
      edge = mapEntry.getValue();
      if( edge != null ){
        if ( edge.containsVertex( vertex ) == true ) {
          incident.add( edge );
        }
      }
    }
    return incident;
  }

  /**
   * Returns true if two vertices v and v are adjacent
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @return boolean
  */
  @Override
	public synchronized boolean areAdjacent( IVertex<T> vertexV, IVertex<T> vertexW )
  {
    return( this.getEdge( vertexV, vertexW ) != null );
  }

  /**
   * Insert and returns an edge between two vertices V and W. Object object is
   * stored in the edge
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
   * @param object Object
   * @return Edge
  */
  @Override
	public synchronized IEdge<T, U> insert( IVertex<T> vertexV, IVertex<T> vertexW, U object )
  {
    if ( verticeMap.containsValue( vertexV ) == false ) {
      verticeMap.put( vertexV.get(), vertexV );
    }

    if ( verticeMap.containsValue( vertexW ) == false ) {
      verticeMap.put( vertexW.get(), vertexW );
    }

    IEdge<T,U> edge = new Edge<T,U>(vertexV, vertexW, object );
    edgeList.put( edge.get(), edge );
    return edge;
  }

  /**
   * Insert and returns an edge
   *
   * @param edge Edge
   * @return Edge
  */
  public synchronized IEdge<T, U> insert( Edge<T,U> edge )
  {
    IVertex<T> vertex = edge.first();

    if ( verticeMap.containsValue( vertex ) == false ) {
      verticeMap.put( vertex.get(), vertex );
    }

    vertex = edge.last();
    if ( verticeMap.containsValue( vertex ) == false ) {
      verticeMap.put( vertex.get(), vertex );
    }

    edgeList.put( edge.get(), edge );
    return edge;
  }

  /**
   * adds an edge and returns true if all went well, otherwise false
   *
   * @param edg Object
   * @return boolean
  */
  @Override
	public synchronized void add( IEdge<T,U> edge )
  {
    IVertex<T> vertex = edge.first();

    if ( verticeMap.containsValue( vertex ) == false ) {
      verticeMap.put( vertex.get(), vertex );
    }

    vertex = edge.last();
    if ( verticeMap.containsValue( vertex ) == false ) {
      verticeMap.put( vertex.get(), vertex );
    }

    edgeList.put( edge.get(), edge );
  }

  /**
   * adds a collection of edges to the graph and returns true if all went well,
   * otherwise false. Note that all the valid elements are collected, and that
   * return value serves just as a marker that at least one invalid entry
   * was encountered
   *
   * @param edgeCollection Collection
   * @return boolean
  */
  public synchronized boolean addAll( Collection<Edge<T,U>> edgeCollection )
  {
    Edge<T,U> edge;
    boolean retVal = true;
    Iterator<Edge<T,U>> iterator = edgeCollection.iterator();
    while( iterator.hasNext() == true ){

      Object retrieved = iterator.next();

      //Check if the element is an edge
      if( retrieved instanceof Edge<?,?> ){
        edge = iterator.next();
        IVertex<T> vertex = edge.first();

        if ( verticeMap.containsValue( vertex ) == false ) {
          verticeMap.put( vertex.get(), vertex );
        }

        vertex = edge.last();
        if ( verticeMap.containsValue( vertex ) == false ) {
          verticeMap.put( vertex.get(), vertex );
        }

        edgeList.put( edge.get(), edge );
      }else{
        retVal = false;
      }
    }
    return retVal;
  }

  /**
   * Remove an edge
   *
   * @param object Object
   * @return boolean
  */
  @SuppressWarnings("unchecked")
	public synchronized boolean remove( Object object )
  {
    if( object instanceof Edge ){
      IEdge<T, U> edge = ( IEdge<T, U> )object;
      if ( edgeList.remove( edge.get() ) != null ){
        return true;
      }
    }else{
      if( object instanceof Vertex ){
        this.removeVertex(( IVertex<T> )object );
      }
    }
    return false;
  }

  /**
   * Remove a collection of edges
   *
   * @param edgeCollection Collection
   * @return boolean
  */
  public synchronized boolean removeAll( Collection<Edge<T,U>> edgeCollection )
  {
    Set<Map.Entry<U, IEdge<T,U>>> edgeSet = edgeList.entrySet();
    return edgeSet.removeAll( edgeCollection );
  }

  /**
   * Retains ( keeps ) all the edges contained in the collection of edges
   *
   * @param edgeCollection Collection
   * @return boolean
  */
  public synchronized boolean retainAll( Collection<Edge<T,U>> edgeCollection )
  {
    Set<Map.Entry<U, IEdge<T,U>>> edgeSet = edgeList.entrySet();
    return edgeSet.retainAll( edgeCollection );
  }

  /**
   * Replace edge X with edge Y
   *
   * @param edgeX Edge
   * @param edgeY Edge
  */
  @Override
	public synchronized void replace( IEdge<T, U> edgeX, IEdge<T,U> edgeY )
  {
    remove ( edgeX );
    edgeList.put( edgeY.get(), edgeY );
  }

  /**
   * Insert and return a vertex and store Object object at this position
   *
   * @param object Object
   * @return Vertex
  */
  @Override
	public synchronized IVertex<T> insert( T object )
  {
    Vertex<T> v = new Vertex<T>(object );
    verticeMap.put( object, v );
    return v;
  }

  /**
   * Remove vertex V and all its incident edges
   *
   * @param vertex Vertex
  */
  public synchronized void removeVertex( IVertex<T> vertex )
  {
    IEdge<T, U> edge;

    // Iterate through the edge list
    Set<Map.Entry<U, IEdge<T,U>>> edgeSet = Collections.synchronizedSet( edgeList.entrySet() );
    Iterator<Map.Entry<U, IEdge<T,U>>> iterator = edgeSet.iterator();
    Map.Entry<U, IEdge<T,U>> mapEntry;

    // Collect all the opposites of vertex
    while ( iterator.hasNext() == true ) {
      mapEntry = iterator.next();
      edge = mapEntry.getValue();
      if( edge != null ){
        if ( edge.containsVertex( vertex ) == true ) {
          edgeList.remove( edge );
        }
      }
    }
    verticeMap.remove( vertex );
  }

  /**
   * Replace vertex V with vertex W
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
  */
  @Override
	public synchronized void replace( IVertex<T> vertexV, Vertex<T> vertexW )
  {
    remove( vertexV );
    verticeMap.put( vertexW.get(), vertexW );
  }

  /**
   * Swap vertex V with vertex W
   *
   * @param vertexV Vertex
   * @param vertexW Vertex
  */
  @Override
	public void swap( IVertex<T> vertexV, IVertex<T> vertexW )
  {
    T object = vertexW.get();
    vertexW.put( vertexV.get() );
    vertexV.put( object );
  }

  /**
   * Swap edge X with edge Y
   *
   * @param edgeX Edge
   * @param edgeY Edge
  */
  @Override
	public void swap( IEdge<T, U> edgeX, IEdge<T, U> edgeY )
  {
    U object = edgeY.get();
    List<IVertex<T>> vertices = (List<IVertex<T>>) edgeX.endVertices();
    edgeY.put( vertices.get( 0 ), vertices.get( 1 ), edgeX.get() );
    vertices = (List<IVertex<T>>) edgeY.endVertices();
    edgeX.put( vertices.get( 0 ), vertices.get( 1 ), object);
  }
}
