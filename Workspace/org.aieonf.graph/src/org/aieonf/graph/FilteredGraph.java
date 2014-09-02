package org.aieonf.graph;

import java.util.Collection;

import org.aieonf.concept.IConcept;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.IRelationship;
import org.aieonf.util.filter.AbstractFilter;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.graph.DirectedEdgeList;
import org.aieonf.util.graph.IEdge;
import org.aieonf.util.graph.IGraph;

public class FilteredGraph extends AbstractConceptGraph implements IConceptGraph
{

	//The graph that provides the data
	private IConceptGraph graph;

	//The filter that is used with this graph
	private AbstractFilter<IDescriptor> filter;
	
	/**
	 * Create the filtered graph using the given graph and filter 
	 * @param graph IConceptGraph
	 * @param graph IConceptGraph
	 * @param descriptor
	 */
	public FilteredGraph( IConceptGraph graph, AbstractFilter<IDescriptor> filter )
	{
		this.graph = graph;
		super.setGraph( new DirectedEdgeList<IConcept, IRelationship>());
		this.filter = filter;
	}
	
	/**
	 * Create a new graph based on the given selection 
	*/
	@Override
	public void createGraph() throws Exception
	{
		Collection<IEdge<IConcept, IRelationship>> edges = this.graph.getGraph().edges();
		super.clear();
		if(( edges ==  null ) || ( edges.isEmpty() ))
			return;
		switch( super.getSelectedSearch() ){
			case Parent:
				this.doFilterParents( edges );
				break;
			case Children:
				this.doFilterChildren( edges );
				break;
			case All:
				this.doFilterAll( edges );
				break;
		}
	}
	
	/**
	 * Filter on the parents of this graph
	 * @param edges Collection<Edge<IConcept, IRelationship>>
	 * @throws FilterException
	*/
	protected void doFilterParents( Collection<IEdge<IConcept, IRelationship>> edges ) throws FilterException
	{
		IGraph<IConcept, IRelationship> grph = super.getGraph();
		for( IEdge<IConcept, IRelationship> edge: edges ){
			if( this.filter.accept( edge.first().get() ))
				grph.add( edge );
		}
	}
	
	/**
	 * Filter on the children of this graph
	 * @param edges Collection<Edge<IConcept, IRelationship>>
	 * @throws FilterException
	*/
	protected void doFilterChildren( Collection<IEdge<IConcept, IRelationship>> edges ) throws FilterException
	{
		IGraph<IConcept, IRelationship> grph = super.getGraph();
		for( IEdge<IConcept, IRelationship> edge: edges ){
			if( this.filter.accept( edge.last().get() ))
				grph.add( edge );
		}
	}

	/**
	 * Filter on the both the parents and the children of this graph
	 * @param edges Collection<Edge<IConcept, IRelationship>>
	 * @throws FilterException
	*/
	protected void doFilterAll( Collection<IEdge<IConcept, IRelationship>> edges ) throws FilterException
	{
		IGraph<IConcept, IRelationship> grph = super.getGraph();
		for( IEdge<IConcept, IRelationship> edge: edges ){
			if(( this.filter.accept( edge.first().get() ) && ( this.filter.accept( edge.last().get() ))))
				grph.add( edge );
		}
	}
}
