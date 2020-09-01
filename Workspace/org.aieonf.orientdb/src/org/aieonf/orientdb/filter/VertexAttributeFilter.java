package org.aieonf.orientdb.filter;

import org.aieonf.commons.Utils;
import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.filter.AttributeFilter;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class VertexAttributeFilter extends AbstractGraphFilter {

	private String refKey;
	private String refVal;

	protected VertexAttributeFilter(OrientGraph graph, String name, AttributeFilter.Rules rule, String key) {
		this( graph, name, rule, key, null );
	}
	
	protected VertexAttributeFilter(OrientGraph graph, String name, AttributeFilter.Rules rule, String key, String refVal) {
		super(graph, name, rule.toString());
		this.refKey = key.replace(".", "_");
		this.refVal = refVal;
	}

	@Override
	protected boolean acceptEnabled(Vertex vertex) throws FilterException {
		if(( vertex == null ) || ( Utils.assertNull(vertex.getPropertyKeys())))
			return false;
		boolean contains = false;
		String val = vertex.getProperty(this.refKey);
		contains = !StringUtils.isEmpty(val);
		switch( org.aieonf.concept.filter.AttributeFilter.Rules.valueOf( super.getRule() )){
		case CONTAINS:
			return contains;
		case CONTAINS_NOT:
			return !contains;
		case EQUALS:
			if( contains && val.equals( this.refVal ))
				return true;
			break;
		case EQUALS_NOT:
			if( !val.equals( this.refVal ))
				return true;
			break;

		default:
			if( StringUtils.isEmpty(val))
				break;
			WildcardFilter filter = new WildcardFilter( StringStyler.styleToEnum( val ));
			return filter.accept( this.refVal );
		}
		return false;
	}
}
