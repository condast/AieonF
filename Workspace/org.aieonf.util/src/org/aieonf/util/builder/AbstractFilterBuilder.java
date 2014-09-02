package org.aieonf.util.builder;

import org.aieonf.util.filter.AbstractFilter;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public abstract class AbstractFilterBuilder<T extends Object> extends AbstractFilter<T> implements
		IBuilder<Object>
{
	public static final String S_BUILD_RULES = "Build";
	
	private IFilter<T> filter;
	
	public AbstractFilterBuilder( IFilter<T> filter )
	{
		this.filter = filter;
	}

	/**
	 * @return the filter
	 */
	protected IFilter<T> getFilter()
	{
		return filter;
	}


	@Override
	public String[] getRules()
	{
		String[] str = new String[1];
		str[0] = S_BUILD_RULES;
		return str;
	}

	@Override
	protected boolean acceptEnabled(Object obj) throws FilterException
	{
		this.build(obj);
		return filter.accept(obj);
	}
}
