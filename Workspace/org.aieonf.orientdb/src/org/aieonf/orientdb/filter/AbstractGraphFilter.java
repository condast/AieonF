package org.aieonf.orientdb.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.aieonf.commons.filter.AbstractAttributeFilter;
import org.aieonf.commons.filter.AbstractFilter;
import org.aieonf.concept.IDescriptor;
import org.aieonf.orientdb.core.VertexConceptBase;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public abstract class AbstractGraphFilter extends AbstractFilter<Vertex> implements IGraphFilter {

	public String SQL_BASE = "SELECT FROM ";

	private OrientGraph graph;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected AbstractGraphFilter(OrientGraph graph, String name, String rule) {
		super(name, rule);
		this.graph = graph;
	}

	protected OrientGraph getGraph() {
		return graph;
	}

	@Override
	protected String[] getRules() {
		return AbstractAttributeFilter.Rules.items();
	}

	/**
	 * If true, the given rule is accepted by this filter
	 *
	 * @param rule String
	 * @return boolean
	 */
	@Override
	protected boolean acceptRule( String rule ){
		return org.aieonf.concept.filter.AttributeFilter.checkRule( rule );
	}

	protected Iterator<Vertex> getIterator( OrientGraph graph ){
		return graph.getVerticesOfClass( IDescriptor.DESCRIPTORS ).iterator();
	}
	
	@Override
	public Collection<Vertex> doFilter() {
		Iterator<Vertex> iterator = getIterator( this.graph);
		Collection<Vertex> results = new ArrayList<>();
		while( iterator.hasNext() ) {
			Vertex vertex = iterator.next();
			logger.fine( new VertexConceptBase( vertex ).toString());
			if( acceptEnabled(vertex))
				results.add(vertex);
		}
		return results;
	}
	
}
