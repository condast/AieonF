package org.aieonf.model.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.model.IModelLeaf;
import org.aieonf.util.StringStyler;
import org.aieonf.util.filter.AbstractFilter;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public class HierarchicalFilter<T extends IModelLeaf<?>> extends AbstractFilter<T>
{
	public enum Rules{
		FILTER_PARENTS,
		FILTER_CHILD;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		public static String[] getRules(){
			Collection<String> results = new ArrayList<String>();
			for( Rules rule: values())
				results.add( rule.toString() );
			return results.toArray( new String[ results.size() ]);
		}
	}
	
	private IFilter<T> filter;
	
	public HierarchicalFilter( IFilter<T> filter)
			throws FilterException
	{
		this.filter = filter;
	}

	@Override
	protected String[] getRules() {
		return Rules.getRules();
	}

	@Override
	protected boolean acceptEnabled(Object obj) throws FilterException {
		return filter.accept(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> doFilter(Collection<T> list, int amount)
			throws FilterException {
		Collection<T> results = new ArrayList<T>();
		Collection<T> accept = super.doFilter(list, amount);
		for( T model: accept ){
			if( model.getParent() != null )
				results.add( (T) model.getParent() );
		}
		return results;
	}
	
	
}
