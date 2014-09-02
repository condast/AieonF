package org.aieonf.util.filter;

import java.util.Collection;

public class HierarchicalFilter<T extends Object> extends FilterChain<T>
{	
	private IFilter<T> parentFilter, childFilter;
	
	public HierarchicalFilter( IFilter<T> parentFilter, IFilter<T> childFilter) throws FilterException
	{
		this( Rules.OrChain, parentFilter, childFilter );
	}

	public HierarchicalFilter(Rules chainRule, IFilter<T> parentFilter, IFilter<T> childFilter) throws FilterException
	{
		super(chainRule);
		super.addFilter( parentFilter );
		super.addFilter( childFilter );
		this.parentFilter = parentFilter;
		this.childFilter = childFilter;
	}

	/**
	 * @return the parentFilter
	 */
	protected final IFilter<T> getParentFilter()
	{
		return parentFilter;
	}

	/**
	 * @return the childFilter
	 */
	protected final IFilter<T> getChildFilter()
	{
		return childFilter;
	}

	public Collection<T> getParents( Collection<T> collection ){
		return this.parentFilter.doFilter(collection);
	}

	public Collection<T> getParents( Collection<T> collection, int amount ){
		return this.parentFilter.doFilter(collection, amount );
	}

	public Collection<T> getChildren( Collection<T> collection ){
		return this.childFilter.doFilter(collection);
	}

	public Collection<T> getChildren( Collection<T> collection, int amount ){
		return this.childFilter.doFilter( collection, amount );
	}

}
