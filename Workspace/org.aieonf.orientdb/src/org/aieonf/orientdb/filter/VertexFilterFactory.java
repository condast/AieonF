package org.aieonf.orientdb.filter;

import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.concept.filter.AttributeFilter;
import org.aieonf.concept.filter.FilterFactory;
import org.aieonf.concept.filter.FilterFactory.Filters;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class VertexFilterFactory {

	private OrientGraph graph;
	
	public VertexFilterFactory(OrientGraph graph) {
		super();
		this.graph = graph;
	}

	/**
	 * Create a filter by providing the correct attributes
	 * @param <D>
	 * @param name
	 * @param attributes
	 * @return
	 */
	public IGraphFilter createFilter( Filters name, Map<FilterFactory.Attributes, String> attributes){
		IGraphFilter filter = null;
		AttributeFilter.Rules rule = AttributeFilter.Rules.valueOf( StringStyler.styleToEnum( attributes.get(FilterFactory.Attributes.RULES)));
		String ref = attributes.get(FilterFactory.Attributes.REFERENCE);
		String value = attributes.get(FilterFactory.Attributes.VALUE);
		switch( name) {
		case ATTRIBUTE_LIST:
			String[] split = value.split("[\\s]");
			filter = new VertexAttributeListFilter( graph, VertexAttributeListFilter.class.getName(), rule, ref, split );
			break;
		default:
			filter = new VertexAttributeFilter( graph, VertexAttributeFilter.class.getName(), rule, ref, value );
			break;
		}
		return filter;
	}

}
