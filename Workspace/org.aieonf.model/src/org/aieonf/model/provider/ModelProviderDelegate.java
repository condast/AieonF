package org.aieonf.model.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.IModelLeaf;
import org.aieonf.model.builder.IModelBuilderListener;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public class ModelProviderDelegate<U extends Object> implements IModelDelegate<U> 
{
	private Collection<IModelProvider<U>> providers;
	private Collection<IModelBuilderListener<U>> listeners;
	
	private IModelBuilderListener<U> listener = new IModelBuilderListener<U>() {
		
		@Override
		public void notifyChange(ModelBuilderEvent<U> event) {
			notifyEventChange(event);;
		}
	};
	
	public ModelProviderDelegate()
	{
		providers = new ArrayList<IModelProvider<U>>();
		listeners = new ArrayList<IModelBuilderListener<U>>();
	}

	public void addProvider( IModelProvider<U> provider ){
		provider.addListener(listener);
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<U> provider ){
		provider.removeListener(listener);
		this.providers.remove( provider );
	}

	protected void notifyEventChange( ModelBuilderEvent<U> event ) {
		for( IModelBuilderListener<U> mbl: listeners )
			mbl.notifyChange( new ModelBuilderEvent<U>( this, event.getModel() ));
	}

	@Override
	public void open() {
		for( IModelProvider<U> provider: this.providers ){
			provider.open();
		}
	}

	@Override
	public boolean isOpen() {
		for( IModelProvider<U> provider: this.providers ){
			if( provider.isOpen())
				return true;
		}
		return false;
	}


	@Override
	public void close() {
		for( IModelProvider<U> provider: this.providers ){
			provider.close();
		}
	}

	@Override
	public void addListener(IModelBuilderListener<U> listener) {
		this.listeners.add( listener );	
	}

	@Override
	public void removeListener(IModelBuilderListener<U> listener) {
		this.listeners.remove( listener );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void contains(IModelLeaf<? extends IDescriptor> leaf) {
		for( IModelProvider<U> provider: this.providers ){
			if( provider.contains(leaf)){
				listener.notifyChange( new ModelBuilderEvent<U>( provider, (U) leaf ));
			}
		}
		listener.notifyChange( new ModelBuilderEvent<U>( this ));
	}

	protected void onGet( IModelProvider<U> provider, IDescriptor descriptor ){
		try {
			provider.get(descriptor);
			listener.notifyChange( new ModelBuilderEvent<U>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelBuilderEvent<U>( provider ));
		}
	}
	
	@Override
	public synchronized void get(IDescriptor descriptor)
			throws ParseException {
		for( IModelProvider<U> provider: this.providers ){
			this.onGet(provider, descriptor);
		}
	}

	protected void onSearch( IModelProvider<U> provider, IModelFilter<IDescriptor> filter ){
		try {
			provider.search( filter );
			listener.notifyChange( new ModelBuilderEvent<U>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelBuilderEvent<U>( provider ));
		}
	}
	@Override
	public synchronized void search( IModelFilter<IDescriptor> filter) throws ParseException {
		for( IModelProvider<U> provider: this.providers ){
			this.onSearch(provider, filter);
		}
	}
	
	@Override
	public void deactivate() {
		for( IModelProvider<U> provider: this.providers ){
			provider.deactivate();
		}
	}
}