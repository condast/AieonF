package org.aieonf.util.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.util.filter.AbstractFilter.Mode;


public class BatchFilter<T extends Object> implements IFilter<T>
{
	public static final String S_BATCH = "Batch:";
	private IFilter<T> filter;
	
	private Collection<?> batch;
	
	public BatchFilter( IFilter<T> filter, Collection<?> descriptors )
	{
		this.filter = filter;
	}

	@Override
	public String getName()
	{
		return S_BATCH + this.filter.getName();
	}

	@Override
	public String getRule()
	{
		return this.filter.getRule();
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

	
	/**
	 * @return the batch
	 */
	protected final Collection<?> getBatch()
	{
		return batch;
	}

	@Override
	public boolean accept(Object obj) throws FilterException
	{
		if( !this.batch.contains(obj))
			return false;
		return this.filter.accept(obj);
	}

	@Override
	public Collection<T> doFilter(Collection<T> list) throws FilterException
	{
		Collection<T> temp = new ArrayList<T>();
		for( T item: list )
			if( batch.contains( item ))
				temp.add( item );
		return this.filter.doFilter( temp );
	}

	@Override
	public Collection<T> doFilter(Collection<T> list, int amount)
			throws FilterException
	{
		Collection<T> temp = new ArrayList<T>();
		for( T item: list )
			if( batch.contains( item ))
				temp.add( item );
		return this.filter.doFilter(list, amount);
	}

	@Override
	public Collection<T> getRejected()
	{
		return this.filter.getRejected();
	}

	
}
