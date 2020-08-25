package org.aieonf.commons.implicit;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class ImplicitCollection<T extends IImplicit<T>> implements Collection<T>
{
	private Collection<T> collection;
	
	public ImplicitCollection()
	{
		collection = new TreeSet<T>();
	}

	@Override
	public boolean add( T arg0)
	{
		for( IImplicit<T> implicit: collection)
			if( implicit.test( arg0 ))
				return false;
		return collection.add( arg0 );
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0)
	{
		Collection<T> temp = new TreeSet<T>();
		for( IImplicit<T> implicit: collection)
			for( T arg: arg0 ){
				if( implicit.test( arg ))
					continue;
				temp.add( arg );
			}
		return collection.addAll( temp );
	}

	@Override
	public void clear()
	{
		this.collection.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains( Object o)
	{
		if( !( o instanceof IImplicit ))
			return false;
		IImplicit<T> impl = ( IImplicit<T> )o;
		for( IImplicit<T> implicit: collection)
			if( implicit.test( (T) impl ))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for( Object o: c)
			if( !contains( o ))
					return false;
		return true;
	}

	@Override
	public boolean isEmpty()
	{
		return this.collection.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return this.collection.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o)
	{
		if( !( o instanceof IImplicit ))
			return collection.remove(o);
		IImplicit<T> impl = ( IImplicit<T> )o;
		for( IImplicit<T> implicit: collection)
			if( implicit.test( (T) impl ))
				return collection.remove( implicit );
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean retVal = true;
		for( Object o: c)
			retVal &= remove( o );
		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c)
	{
		Collection<T> temp = new TreeSet<T>();
		boolean retVal = true;
		for( Object o: c){
			if( !( o instanceof IImplicit )){
				retVal = false;
				continue;
			}
			if( contains( o )){
				T impl = ( T )o;
				retVal &= temp.add( impl );
			}
		}
		collection.clear();
		collection.addAll( temp );
		return retVal;
	}

	@Override
	public int size()
	{
		return this.collection.size();
	}

	@Override
	public Object[] toArray()
	{
		return this.collection.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.collection.toArray(a);
	}

}