package org.aieonf.concept.filter;

import java.util.Calendar;
import java.util.Date;

import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.core.Descriptor;
import org.aieonf.util.filter.AbstractComparableAttributeFilter;
import org.aieonf.util.filter.FilterException;

public class DateFilter<T extends IDescribable<?>> extends AbstractComparableAttributeFilter<T,Date>
{

	public DateFilter() throws FilterException
	{
		super();
	}

	public DateFilter( Rules rule, String refKey, Date refVal1, Date refVal2) throws FilterException
	{
		super(rule, refKey, refVal1, refVal2);
	}

	public DateFilter( Rules rule, String refKey, Date refVal) throws FilterException
	{
		super(rule, refKey, refVal);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( Long.MAX_VALUE );
		super.setMaxRefVal( calendar.getTime());
	}

	public DateFilter( Rules rule, String name, String refKey, Date refVal1, Date refVal2)
			throws FilterException
	{
		super(rule, name, refKey, refVal1, refVal2);
	}

	public DateFilter( Rules rule, String name, String refKey, Date refVal) throws FilterException
	{
		super(rule, name, refKey, refVal);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( Long.MAX_VALUE );
		super.setMaxRefVal( calendar.getTime());
	}

	@Override
	protected int compareTo( Date reference, Object obj)
	{
		if( !( obj instanceof IDescribable ))
			return -1;
		IDescriptor descriptor = (( IDescribable<?> )obj).getDescriptor();
		Date comp = Descriptor.getCreateDate( descriptor );
		if( comp.getTime() < reference.getTime() )
			return -1;
		if( comp.getTime() > reference.getTime() )
			return 1;
		return 0;
	}

}
