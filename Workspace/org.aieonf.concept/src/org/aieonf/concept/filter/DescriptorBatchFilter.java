package org.aieonf.concept.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aieonf.concept.IDescribable;
import org.aieonf.util.filter.BatchFilter;
import org.aieonf.util.filter.FilterException;
import org.aieonf.util.filter.IFilter;

public class DescriptorBatchFilter<T extends IDescribable<?>> extends BatchFilter<T>
{

	@SuppressWarnings("unchecked")
	public DescriptorBatchFilter( DescriptorFilter.Rules rule, Collection<?> descriptors)
	{
		super( new DescriptorFilter<T>( rule, (T)descriptors.iterator().next() ), descriptors );
	}

	/* (non-Javadoc)
	 * @see org.condast.util.filter.BatchFilter#accept(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean accept(Object obj) throws FilterException
	{
		if( !( obj instanceof IDescribable ))
			return false;
		Collection<?> batch = super.getBatch();
		for( Object item: batch ){
			IFilter<T> filter = new DescriptorFilter<T>( DescriptorFilter.Rules.valueOf( super.getRule() ), ( T )item );
			if( filter.accept(obj))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.condast.util.filter.BatchFilter#doFilter(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> doFilter(Collection<T> list) throws FilterException
	{
		Collection<?> batch = super.getBatch();
		List<T> results = new ArrayList<T>();
		int amount = super.getAmount();
		for( Object item: batch ){
			amount = ( super.getAmount() > results.size() )? ( super.getAmount() - results.size() ): 0;  
			IFilter<T> filter = new DescriptorFilter<T>( DescriptorFilter.Rules.valueOf( super.getRule() ), ( T )item );
			filter.setAmount(amount);
			results.addAll( filter.doFilter( list ));
		}
		return results;
	}

	/* (non-Javadoc)
	 * @see org.condast.util.filter.BatchFilter#doFilter(java.util.Collection, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> doFilter(Collection<T> list, int amount)
			throws FilterException
	{
		Collection<?> batch = super.getBatch();
		List<T> results = new ArrayList<T>();
		int amnt;
		for( Object item: batch ){
			amnt = ( amount > results.size() )? ( amount - results.size() ): 0;  
			IFilter<T> filter = new DescriptorFilter<T>( DescriptorFilter.Rules.valueOf( super.getRule() ), ( T )item );
			filter.setAmount(amnt);
			results.addAll( filter.doFilter( list ));
		}
		return results;
	}	
}