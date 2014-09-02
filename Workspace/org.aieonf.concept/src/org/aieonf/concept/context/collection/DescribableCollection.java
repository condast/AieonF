package org.aieonf.concept.context.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.aieonf.concept.IDescribable;
import org.aieonf.util.implicit.IImplicit;
import org.aieonf.util.implicit.ImplicitCollection;

public class DescribableCollection<E extends IDescribable<?>> extends TreeSet<E> implements
		Collection<E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7835746668186554975L;
	private Collection<IImplicit<?>> implicits;
	
	public DescribableCollection()
	{
		implicits = new ImplicitCollection<IImplicit<?>>();
	}

	@Override
	public boolean add(E arg0)
	{
		if( arg0 instanceof IImplicit)
			return implicits.add(( IImplicit<?> )arg0 );
		return super.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0)
	{
		boolean result = true;
		for( E item: arg0 ){
			if( item instanceof IImplicit)
				result &= implicits.add(( IImplicit<?> )item );
			else
				result &= super.add( item );
		}
		return result;
	}

	@Override
	public void clear()
	{
		this.implicits.clear();
		super.clear();
		
	}

	@Override
	public boolean contains(Object arg0)
	{
		if( this.implicits.contains( arg0 ))
			return true;
		return super.contains( arg0 );
	}

	@Override
	public boolean containsAll(Collection<?> arg0)
	{
		for( Object item: arg0 ){
			if( ! implicits.contains( item ) && !super.contains( item ))
				return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty()
	{
		return ( this.implicits.isEmpty() && super.isEmpty() );
	}

	@Override
	public Iterator<E> iterator()
	{
		return this.getCollection().iterator();
	}

	@Override
	public boolean remove(Object arg0)
	{
		if( this.implicits.remove( arg0))
			return true;
		return super.remove( arg0 );
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		boolean result = true;
		for( Object item: arg0 ){
			if( item instanceof IImplicit)
				result &= implicits.remove( item );
			else
				result &= super.remove( item );
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		return this.getCollection().retainAll(arg0);
	}

	@Override
	public int size()
	{
		return ( this.implicits.size() + super.size() );
	}

	@Override
	public Object[] toArray()
	{
		return this.getCollection().toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0)
	{
		return this.getCollection().toArray(arg0);
	}
	
	@SuppressWarnings("unchecked")
	protected Collection<E> getCollection(){
		Collection<E> total = new TreeSet<E>();
		Iterator<E> iterator = super.iterator();
		while( iterator.hasNext() )
			total.add(iterator.next());
		for(IImplicit<?> implicit: implicits ){
			if( implicit instanceof IDescribable)
				total.add(( E )implicit);
		}
		return total;
	}

}
