package org.aieonf.template.provider;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.provider.ModelProviderDelegate;

public class AsynchronousProviderDelegate<U extends Object> extends ModelProviderDelegate<U> 
{
	private ExecutorService service;
	
	public AsynchronousProviderDelegate()
	{
		service = Executors.newCachedThreadPool();
	}	
	

	@Override
	public void onGet( IModelProvider<U> provider, IDescriptor descriptor){
		Runnable runnable = new GetRunnable( provider, descriptor );
		service.execute( runnable);
	}

	@Override
	public void onSearch( IModelProvider<U> provider, IModelFilter<IDescriptor> filter){
		Runnable runnable = new SearchRunnable( provider, filter );
		service.execute( runnable);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		service.shutdown();
	}

	private class GetRunnable implements Runnable{

		private IModelProvider<U> provider;
		private IDescriptor descriptor;
		
		GetRunnable( IModelProvider<U> provider, IDescriptor descriptor ) {
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

		private IModelProvider<U> provider;
		private IModelFilter<IDescriptor> filter;
		
		SearchRunnable( IModelProvider<U> provider, IModelFilter<IDescriptor> filter ) {
			super();
			this.provider = provider;
			this.filter = filter;
		}

		@Override
		public void run(){
			try {
				provider.open();
				Collection<U> models = provider.search( filter );
				notifyEventChange( new ModelBuilderEvent<U>( provider, models ));
			} catch (ParseException e) {
				e.printStackTrace();
				notifyEventChange( new ModelBuilderEvent<U>( provider ));
			}
			finally{
				provider.close();
			}
		}
	}

}