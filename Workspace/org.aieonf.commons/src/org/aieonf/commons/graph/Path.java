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

//J2SE import
import java.util.*;

/**
 * This class provides some utility methods to create and manage paths from a
 * given graph.
*/
public class Path<T,U>
{

  /**
   * The graph that is used
  */
  private IGraph<T,U> graph;

  /**
   * CONSTRUCTOR: Initialise relevvant methods
   *
   * @param newGraph Graph
  */
  public Path( IGraph<T,U> newGraph )
  {
    graph = newGraph;
  }

  /**
   * This method creates a unique path based on an integer number
   *
   * @param number int
   * @return List
  */
  public synchronized List<?> createPath( int number )
  {
    ArrayList<Object> path = new ArrayList<Object>();
    Collection<?> indexTable = graph.selection();

    // Calculate the faculty of the size of the selection
    int numberOfPaths = 1;
    for ( int i = 1; i < indexTable.size(); i++ ) {
      numberOfPaths *= i;
    }

    // Check if the number is valid
    if ( number < 0 ) {
      number = 0;
    } else {
      if ( number > numberOfPaths ) {
        number = numberOfPaths;
      }
    }

    Iterator<?> iterator = indexTable.iterator();
    if( iterator.hasNext() == false )
      return path;

    path.add( iterator.next() );
    indexTable.remove( 0 );

    int divisor;
    while ( iterator.hasNext() ) {
      divisor = number % indexTable.size();
      number = number / indexTable.size();
      path.add( iterator.next() );
      indexTable.remove( divisor );
    }
    path.add( path.get( 0 )); // Make path a cycle
    return path;
  }

  /**
   * This method creates a number from a path
   *
   * @param path ArrayList
   * @return int
  */
  public synchronized int createNumber( ArrayList<?> path )
  {
    Collection<?> indexTable = graph.selection();
    Iterator<?> iterator = indexTable.iterator();
    if( iterator.hasNext() == false )
      return -1;

    if( path.get( 0 ) != iterator.next() ) {
      return -1;
    }

    int number = 1;
    int index = 1;
    for ( int i = 1; i < path.size(); i ++ ) {
      if ( indexTable.contains( path.get( i )) == false ) {
        return -1;
      } else {
        index++;
        number *= index;
        indexTable.remove( path.get( i ));
      }
    }
    return number;
  }

  /**
   * This support method calculates the maximum number of possible paths,
   * which equals the faculty of the number of vertices
   *
   * @return int
  */
  public synchronized int maxNumberOfPaths()
  {
    // Calculate the number of paths of the selection
    int numberOfPaths = 1;
    for ( int i = 1; i < graph.numVertices(); i++ ) {
      numberOfPaths *= i;
    }
    return numberOfPaths;
  }
}
