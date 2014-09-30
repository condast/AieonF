package org.aieonf.template.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.IModelProvider;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.util.parser.ParseException;

public class CombinedProvider<T extends IDescriptor, U extends Object> implements IModelProvider<T, U> 
{
	private Collection<IModelProvider<T, U>> providers;
	
	public CombinedProvider()
	{
		providers = new ArrayList<IModelProvider<T, U>>();
	}

	public void addProvider( IModelProvider<T, U> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<T, U> provider ){
		this.providers.remove( provider );
	}

	
	@Override
	public void open() {
		for( IModelProvider<T, U> provider: this.providers ){
			provider.open();
		}
	}

	@Override
	public boolean isOpen() {
		for( IModelProvider<T, U> provider: this.providers ){
			if( provider.isOpen())
				return true;
		}
		return false;
	}


	@Override
	public void close() {
		for( IModelProvider<T, U> provider: this.providers ){
			provider.close();
		}
	}

	@Override
	public void addListener(IModelBuilderListener listener) {
		for( IModelProvider<T, U> provider: this.providers ){
			provider.addListener(listener);
		}
	}

	@Override
	public void removeListener(IModelBuilderListener listener) {
		for( IModelProvider<T, U> provider: this.providers ){
			provider.removeListener(listener);
		}
	}

	@Override
	public boolean contains(IModelLeaf<? extends IDescriptor> leaf) {
		for( IModelProvider<T, U> provider: this.providers ){
			if( provider.contains(leaf))
				return true;
		}
		return false;
	}

	@Override
	public Collection<U> get(IDescriptor descriptor)
			throws ParseException {
		Collection<U> results = new ArrayList<U>();
		for( IModelProvider<T, U> provider: this.providers ){
			Collection<U> temp = provider.get(descriptor);
			if(( temp != null ) && ( !temp.isEmpty() ))
				results.addAll( (Collection<? extends U>) temp );
		}
		return results;
	}

	@Override
	public Collection<U> search(
			IModelFilter<IDescriptor> filter) throws ParseException {
		Collection<U> results = new ArrayList<U>();
		for( IModelProvider<T, U> provider: this.providers ){
			try{
				if( !provider.isOpen())
					continue;
				Collection<U> temp = provider.search( filter );
				if(( temp != null ) && ( !temp.isEmpty() ))
					results.addAll( (Collection<? extends U>) temp );
			}
			catch( ParseException e ){
				e.printStackTrace();
			}
		}
		return results;
	}
	
	@Override
	public String printDatabase() {
		StringBuffer buffer = new StringBuffer();
		for( IModelProvider<T, U> provider: this.providers ){
			buffer.append( provider.printDatabase() + "\n\n" );
		}
		return buffer.toString();
	}

	@Override
	public void deactivate() {
		for( IModelProvider<T, U> provider: this.providers ){
			provider.deactivate();
		}
	}
}
