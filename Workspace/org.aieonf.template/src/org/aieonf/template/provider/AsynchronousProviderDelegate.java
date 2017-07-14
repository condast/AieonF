package org.aieonf.template.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.commons.parser.ParseException;
import org.aieonf.concept.IDescriptor;
import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.model.builder.ModelBuilderEvent;
import org.aieonf.model.filter.IModelFilter;
import org.aieonf.model.provider.IModelProvider;
import org.aieonf.model.provider.ModelDelegate;

public class AsynchronousProviderDelegate<U extends IDescriptor> extends ModelDelegate<U> 
{
	private ExecutorService service;
	private IDomainAieon domain;
	
	public AsynchronousProviderDelegate( IDomainAieon domain)
	{
		service = Executors.newCachedThreadPool();
		this.domain=  domain;
	}	
	
	
	@Override
	protected void onGet(IModelProvider<IDomainAieon, U> provider, IDescriptor descriptor) {
		Runnable runnable = new GetRunnable( provider, descriptor );
		service.execute( runnable);
	}

	@Override
	protected void onSearch(IModelProvider<IDomainAieon, U> provider, IModelFilter<IDescriptor> filter) {
		Runnable runnable = new SearchRunnable( provider, filter );
		service.execute( runnable);
	}



	private class GetRunnable implements Runnable{

		private IModelProvider<IDomainAieon,U> provider;
		private IDescriptor descriptor;
		
		GetRunnable( IModelProvider<IDomainAieon,U> provider, IDescriptor descriptor ) {
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

		private IModelProvider<IDomainAieon,U> provider;
		private IModelFilter<IDescriptor> filter;
		
		SearchRunnable( IModelProvider<IDomainAieon,U> provider, IModelFilter<IDescriptor> filter ) {
			super();
			this.provider = provider;
			this.filter = filter;
		}

		@Override
		public void run(){
			try {
				provider.open( domain );
				if( provider.isOpen() ){
					provider.search( filter );
					notifyEventChange( new ModelBuilderEvent<U>( provider ));
				}
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