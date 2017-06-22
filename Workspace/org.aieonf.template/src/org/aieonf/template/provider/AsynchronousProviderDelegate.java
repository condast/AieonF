package org.aieonf.template.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescribable;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.provider.ModelDelegate;

public class AsynchronousProviderDelegate<D extends IDescribable<? extends IDescriptor>, U extends IDescriptor> extends ModelDelegate<D, U> 
{
	private ExecutorService service;
	
	public AsynchronousProviderDelegate()
	{
		service = Executors.newCachedThreadPool();
	}	
		
	@Override
	protected void onGet(IModelProvider<D, U> provider, IDescriptor descriptor) {
		Runnable runnable = new GetRunnable( provider, descriptor );
		service.execute( runnable);
	}

	@Override
	protected void onSearch(IModelProvider<D, U> provider, IModelFilter<IDescriptor> filter) {
		Runnable runnable = new SearchRunnable( provider, filter );
		service.execute( runnable);
	}



	private class GetRunnable implements Runnable{

		private IModelProvider<D,U> provider;
		private IDescriptor descriptor;
		
		GetRunnable( IModelProvider<D,U> provider, IDescriptor descriptor ) {
			super();
			this.provider = provider;
			this.descriptor = descriptor;
		}

		@Override
		public void run(){
			try {
				provider.get( descriptor );
				notifyEventChange( new ModelBuilderEvent<U>( provider, null, true));
			} catch (ParseException e) {
				e.printStackTrace();
				notifyEventChange( new ModelBuilderEvent<U>( provider ));
			}
		}
	}

	private class SearchRunnable implements Runnable{

		private IModelProvider<D, U> provider;
		private IModelFilter<IDescriptor> filter;
		
		SearchRunnable( IModelProvider<D,U> provider, IModelFilter<IDescriptor> filter ) {
			super();
			this.provider = provider;
			this.filter = filter;
		}

		@Override
		public void run(){
			try {
				provider.open( null );
				if( provider.isOpen( null ) ){
					provider.search( filter );
					notifyEventChange( new ModelBuilderEvent<U>( provider ));
				}
			} catch (ParseException e) {
				e.printStackTrace();
				notifyEventChange( new ModelBuilderEvent<U>( provider ));
			}
			finally{
				provider.close( null );
			}
		}
	}

}