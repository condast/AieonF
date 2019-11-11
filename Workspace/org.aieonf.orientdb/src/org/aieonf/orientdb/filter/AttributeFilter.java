package org.aieonf.orientdb.filter;

import org.aieonf.commons.filter.AbstractAttributeFilter;
import org.aieonf.commons.filter.AbstractFilter;
import org.aieonf.commons.filter.FilterException;
import org.aieonf.commons.filter.WildcardFilter;
import org.aieonf.commons.strings.StringUtils;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.filter.AttributeFilter.Rules;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class AttributeFilter extends AbstractFilter<IDescriptor> {

	public String SQL_BASE = "SELECT FROM ";

	private OrientGraph graph;
	private String refKey;
	private String refVal;

	protected AttributeFilter(OrientGraph graph, String domain, String rule, String key) {
		this( graph, domain, rule, key, null );
	}
	
	protected AttributeFilter(OrientGraph graph, String domain, String rule, String key, String refVal) {
		super(domain, rule);
		this.graph = graph;
		this.refKey = key;
		this.refVal = refVal;
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
	protected boolean acceptRule( String rule )
	{
		return org.aieonf.concept.filter.AttributeFilter.checkRule( rule );
	}

	@Override
	protected boolean acceptEnabled(IDescriptor vertex) throws FilterException {
		if( vertex == null )
			return false;
		boolean contains = false;
		String val = vertex.get(this.refKey);
		contains = !StringUtils.isEmpty(val);
		switch( Rules.valueOf( super.getRule() )){
		case Contains:
			return contains;
		case ContainsNot:
			return !contains;
		case Equals:
			if( val.equals( this.refVal ))
				return true;
			break;
		case EqualsNot:
			if( !val.equals( this.refVal ))
				return true;
			break;

		default:
			WildcardFilter filter = new WildcardFilter( val);
			return filter.accept( this.refVal );
		}
		return false;
	}

	
}
