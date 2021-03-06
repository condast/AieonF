package org.aieonf.commons.filter;

import java.util.Collection;

public class NotFilter<T extends Object> implements IFilter<T>
{
	public static final String S_NOT = "Not";
	
	private IFilter<T> filter;
	private Collection<T> results;
	
	public NotFilter( IFilter<T> filter)
	{
		this.filter = filter;
	}
	
	@Override
	public String getName()
	{
		return S_NOT + this.filter.getName();
	}

	@Override
	public String getRule()
	{
		return S_NOT + this.filter.getRule();
	}

	@Override
	public Mode getMode()
	{
		return this.filter.getMode();
	}

	@Override
	public int getAmount()
	{
		return this.filter.getAmount();
	}

	@Override
	public void setAmount(int amount)
	{
		this.filter.setAmount(amount);
	}

	@Override
	public void setMode(Mode mode) {
		filter.setMode(mode);
	}

	@Override
	public boolean accept( T obj) throws FilterException
	{
		return !this.filter.accept(obj);
	}

	@Override
	public Collection<T> doFilter(Collection<T> collection)
			throws FilterException
	{
		results = this.filter.doFilter( collection );
		return this.filter.getRejected();
	}

	@Override
	public Collection<T> doFilter(Collection<T> collection, int amount)
			throws FilterException
	{
		results = this.filter.doFilter(collection, amount);
		return this.filter.getRejected();
	}

	@Override
	public Collection<T> getRejected()
	{
		return this.results;
	}

}
