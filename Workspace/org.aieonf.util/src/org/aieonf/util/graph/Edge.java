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

public class Edge<T,U> implements IEdge<T, U>
{
	/**
    The object stored in the edge
	 */
	private U object;

	/**
	 * A set of vertices
	 */
	private List<IVertex<T>> vertices;

	/**
    CONSTRUCTOR: create and initialize an edge object
	 */
	public Edge()
	{
		object = null;
		vertices = new ArrayList<IVertex<T>>();
	}

	/**
	 * CONSTRUCTOR: create and initialize an edge object, with a given object
	 *
	 * @param vertexV Vertex
	 * @param vertexW Vertex
	 * @param object Object
	 */
	public Edge( IVertex<T> vertexV, IVertex<T> vertexW, U object )
	{
		this();
		this.object = object;
		vertexV.addDegree();
		vertices.add( vertexV );
		vertexW.addDegree();
		vertices.add( vertexW );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#put(org.aieonf.util.graph.Vertex, org.aieonf.util.graph.Vertex, U)
	 */
	@Override
	public void put( IVertex<T> v, IVertex<T> w, U obj )
	{
		object = obj;
		vertices.clear();
		v.addDegree();
		vertices.add( v );
		w.addDegree();
		vertices.add( w );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#get()
	 */
	@Override
	public U get()
	{
		return object;
	}

	@Override
	public Object getID(){
		return this.hashCode();
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#replace(U)
	 */
	@Override
	public void replace( U obj )
	{
		object = obj;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#endVertices()
	 */
	@Override
	public List<IVertex<T>>endVertices()
	{
		return vertices;
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#containsVertex(org.aieonf.util.graph.IVertex)
	 */
	@Override
	public boolean containsVertex ( IVertex<T> vertex )
	{
		return vertices.contains(vertex);
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#first()
	 */
	@Override
	public IVertex<T> first()
	{
		return vertices.get( 0 );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#last()
	 */
	@Override
	public IVertex<T> last()
	{
		return vertices.get( 1 );
	}

	/* (non-Javadoc)
	 * @see org.aieonf.util.graph.IEdge#opposite(org.aieonf.util.graph.IVertex)
	 */
	@Override
	public IVertex<T> opposite( IVertex<T> vertex )
	{
		if ( vertices.contains(vertex) == false ) {
			return null;
		} else {
			if ( vertices.get( 0 ) == vertex ) {
				return vertices.get( 1 );
			} else {
				return vertices.get( 0 );
			}
		}
	}

	/**
	 * Removes an edge
	 */
	@Override
	public void finalize()
	{
		IVertex<T> v = vertices.get( 0 );
		v.removeDegree();
		v = vertices.get( 1 );
		v.removeDegree();
		vertices.clear();
		object = null;
	}
}
