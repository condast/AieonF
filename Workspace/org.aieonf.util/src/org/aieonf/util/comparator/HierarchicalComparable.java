package org.aieonf.util.comparator;

import java.util.Collection;

public class HierarchicalComparable<T extends Comparable<?>> implements
		IHierarchicalComparable<T>
{
	private T reference;
	private IHierarchicalComparator<T> hc;
	
	public HierarchicalComparable( IHierarchicalComparator<T> hc, T reference )
	{
		this.hc = hc;
	}

	@Override
	public int compareTo(T arg0)
	{
		return hc.compare( reference, arg0);
	}

	@Override
	public int compareRange(Comparable<?> target)
	{
		return hc.compareRange( reference, target);
	}

	@Override
	public int compareSubrange(int level, Comparable<?> target)
	{
		return hc.compareSubrange( level, reference, target);
	}

	@Override
	public boolean isInRange(Collection<Comparable<?>> targets)
	{
		for( Comparable<?> target: targets )
			if( this.hc.compareRange( reference, target) != 0 )
				return false;
		return true;
	}

	@Override
	public boolean isInSubrange(int level, Collection<T> targets)
	{
		for( T target: targets )
			if( this.hc.compareSubrange( level, reference, target) != 0 )
				return false;
		return true;
	}

	@Override
	public int size()
	{
		return this.hc.size();
	}

}
