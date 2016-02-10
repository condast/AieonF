package org.aieonf.concept.util;

import java.util.Collection;

import org.aieonf.concept.loader.ILoaderAieon;
import org.aieonf.concept.loader.LoaderAieon;

public class DescribableSet<T extends Object> implements IDescribableSet<ILoaderAieon>
{
	private Collection<T> collection;
	
	public DescribableSet( Collection<T> collection )
	{
		this.collection = collection;
	}

	@Override
	public void prepare(ILoaderAieon loader)
	{
	}

	@Override
	public ILoaderAieon getLoader()
	{
		return new LoaderAieon();
	}

	@Override
	public boolean containsID(String ID)
	{
		for( Object obj: collection ){
			if( !( obj instanceof String ))
				continue;
			String str = ( String )obj;
			if( str.equals( ID ))
				return true;
		}
		return false;
	}

}
