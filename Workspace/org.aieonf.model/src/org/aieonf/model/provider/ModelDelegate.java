package org.aieonf.model.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.core.IModelLeaf;
import org.aieonf.model.core.IModelListener;
import org.aieonf.model.core.ModelEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;

public class ModelDelegate<T extends IDescriptor> implements IModelDelegate<T> 
{
	private Collection<IModelProvider<IDomainAieon, T>> providers;
	private Collection<IModelListener<T>> listeners;
	
	private IModelListener<T> listener = new IModelListener<T>() {
		
		@Override
		public void notifyChange(ModelEvent<T> event) {
			notifyEventChange(event);
		}
	};
	
	public ModelDelegate()
	{
		providers = new ArrayList<IModelProvider<IDomainAieon,T>>();
		listeners = new ArrayList<IModelListener<T>>();
	}

	
	public void addProvider( IModelProvider<IDomainAieon, T> provider ){
		this.providers.add( provider );
	}

	public void removeProvider( IModelProvider<IDomainAieon, T> provider ){
		this.providers.remove( provider );
	}

	@Override
	public void addListener(IModelListener<T> listener) {
		this.listeners.add( listener );	
	}

	@Override
	public void removeListener(IModelListener<T> listener) {
		this.listeners.remove( listener );
	}

	protected void notifyEventChange( ModelEvent<T> event ) {
		for( IModelListener<T> mbl: listeners )
			mbl.notifyChange( new ModelEvent<T>( this, event.getModel() ));
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
				listener.notifyChange( new ModelEvent<T>( provider, descriptor ));
			}
		}
		listener.notifyChange( new ModelEvent<T>( this ));
	}

	protected void onGet( IModelProvider<IDomainAieon, T> provider, IDescriptor descriptor ){
		try {
			provider.get(descriptor);
			listener.notifyChange( new ModelEvent<T>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelEvent<T>( provider ));
		}
	}
	
	@Override
	public synchronized void get(IDescriptor descriptor)
			throws ParseException {
		for( IModelProvider<IDomainAieon,T> provider: this.providers ){
			this.onGet(provider, descriptor);
		}
	}

	@SuppressWarnings("unchecked")
	protected void onSearch( IModelProvider<IDomainAieon,T> provider, IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter ){
		try {
			provider.search(  (IModelFilter<IDescriptor, T>) filter );
			listener.notifyChange( new ModelEvent<T>( provider, null, true ));
		} catch (ParseException e) {
			e.printStackTrace();
			listener.notifyChange( new ModelEvent<T>( provider ));
		}
	}
	@Override
	public synchronized void search( IModelFilter<IDescriptor, IModelLeaf<IDescriptor>> filter) throws ParseException {
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