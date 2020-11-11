package org.aieonf.model.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;

public class ModelDelegate<M extends IDescriptor> implements IModelDelegate<M> 
{
	private Collection<IModelProvider<IDescriptor, M>> providers;
	private Collection<IModelListener<M>> listeners;
	
	private IModelListener<M> listener = new IModelListener<M>() {
		
		@Override
		public void notifyChange(ModelEvent<M> event) {
			notifyEventChange(event);
		}
	};
	
	public ModelDelegate()
	{
		providers = new ArrayList<IModelProvider<IDescriptor,M>>();
		listeners = new ArrayList<IModelListener<M>>();
	}

	
	public void addProvider( IModelProvider<IDescriptor, M> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<IDescriptor, M> provider ){
		this.providers.remove( provider );
	}

	@Override
	public void addListener(IModelListener<M> listener) {
		this.listeners.add( listener );	
	}

	@Override
	public void removeListener(IModelListener<M> listener) {
		this.listeners.remove( listener );
	}

	protected void notifyEventChange( ModelEvent<M> event ) {
		for( IModelListener<M> mbl: listeners )
			mbl.notifyChange( new ModelEvent<M>( this, event.getModel() ));
	}

	@Override
	public void open( IDescriptor domain) {
		for( IModelProvider<IDescriptor, M> provider: this.providers ){
			provider.open( domain );
		}
	}

	@Override
	public boolean isOpen() {
		for( IModelProvider<IDescriptor, M> provider: this.providers ){
			if( provider.isOpen())
				return true;
		}
		return false;
	}

	@Override
	public void close( ) {
		for( IModelProvider<IDescriptor, M> provider: this.providers ){
			provider.close();
		}
	}

	@Override
	public void contains( M descriptor) {
		for( IModelProvider<IDescriptor, M> provider: this.providers ){
			if( provider.contains( descriptor )){
				listener.notifyChange( new ModelEvent<M>( provider, descriptor ));
			}
		}
		listener.notifyChange( new ModelEvent<M>( this ));
	}

	protected void onGet( IModelProvider<IDescriptor, M> provider, IDescriptor descriptor ){
		try {
			provider.get(descriptor);
			listener.notifyChange( new ModelEvent<M>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelEvent<M>( provider ));
		}
	}
	
	@Override
	public synchronized void get(IDescriptor descriptor)
			throws ParseException {
		for( IModelProvider<IDescriptor,M> provider: this.providers ){
			this.onGet(provider, descriptor);
		}
	}

	@SuppressWarnings("unchecked")
	protected void onSearch( IModelProvider<IDescriptor,M> provider, IModelFilter<IModelLeaf<IDescriptor>> filter ){
		try {
			provider.search(  (IModelFilter<M>) filter );
			listener.notifyChange( new ModelEvent<M>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelEvent<M>( provider ));
		}
	}
	@Override
	public synchronized void search( IModelFilter<IModelLeaf<IDescriptor>> filter) throws ParseException {
		for( IModelProvider<IDescriptor, M> provider: this.providers ){
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