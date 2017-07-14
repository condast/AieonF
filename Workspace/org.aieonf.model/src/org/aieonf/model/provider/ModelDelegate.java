package org.aieonf.model.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public class ModelDelegate<T extends IDescriptor> implements IModelDelegate<T> 
{
	private Collection<IModelProvider<IDomainAieon, T>> providers;
	private Collection<IModelBuilderListener<T>> listeners;
	
	private IModelBuilderListener<T> listener = new IModelBuilderListener<T>() {
		
		@Override
		public void notifyChange(ModelBuilderEvent<T> event) {
			notifyEventChange(event);
		}
	};
	
	public ModelDelegate()
	{
		providers = new ArrayList<IModelProvider<IDomainAieon,T>>();
		listeners = new ArrayList<IModelBuilderListener<T>>();
	}

	
	public void addProvider( IModelProvider<IDomainAieon, T> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<IDomainAieon, T> provider ){
		this.providers.remove( provider );
	}

	@Override
	public void addListener(IModelBuilderListener<T> listener) {
		this.listeners.add( listener );	
	}

	@Override
	public void removeListener(IModelBuilderListener<T> listener) {
		this.listeners.remove( listener );
	}

	protected void notifyEventChange( ModelBuilderEvent<T> event ) {
		for( IModelBuilderListener<T> mbl: listeners )
			mbl.notifyChange( new ModelBuilderEvent<T>( this, event.getModel() ));
	}

	public void open( IDomainAieon domain) {
		for( IModelProvider<IDomainAieon, T> provider: this.providers ){
			provider.open( domain );
		}
	}

	public boolean isOpen() {
		for( IModelProvider<IDomainAieon, T> provider: this.providers ){
			if( provider.isOpen())
				return true;
		}
		return false;
	}

	@Override
	public void close( ) {
		for( IModelProvider<IDomainAieon, T> provider: this.providers ){
			provider.close();
		}
	}

	@Override
	public void contains( T descriptor) {
		for( IModelProvider<IDomainAieon, T> provider: this.providers ){
			if( provider.contains( descriptor )){
				listener.notifyChange( new ModelBuilderEvent<T>( provider, descriptor ));
			}
		}
		listener.notifyChange( new ModelBuilderEvent<T>( this ));
	}

	protected void onGet( IModelProvider<IDomainAieon, T> provider, IDescriptor descriptor ){
		try {
			provider.get(descriptor);
			listener.notifyChange( new ModelBuilderEvent<T>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelBuilderEvent<T>( provider ));
		}
	}
	
	@Override
	public synchronized void get(IDescriptor descriptor)
			throws ParseException {
		for( IModelProvider<IDomainAieon,T> provider: this.providers ){
			this.onGet(provider, descriptor);
		}
	}

	protected void onSearch( IModelProvider<IDomainAieon,T> provider, IModelFilter<IDescriptor> filter ){
		try {
			provider.search( filter );
			listener.notifyChange( new ModelBuilderEvent<T>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelBuilderEvent<T>( provider ));
		}
	}
	@Override
	public synchronized void search( IModelFilter<IDescriptor> filter) throws ParseException {
		for( IModelProvider<IDomainAieon, T> provider: this.providers ){
			this.onSearch(provider, filter);
		}
	}
	
	/*	
	
	@Override
	public boolean hasFunction(String function) {
		for( IModelProvider<D, T> provider: this.providers ){
			if( provider.hasFunction(function))
				return true;
		}
		return false;
	}

	@Override
	public IModelProvider<D, T> getFunction(String function) {
		for( IModelProvider<D, T> provider: this.providers ){
			if( provider.hasFunction(function))
				return provider;
		}
		return null;
	}


	@Override
	public void deactivate() {
		for( IModelProvider<U> provider: this.providers ){
			provider.deactivate();
		}
	}
	*/
}