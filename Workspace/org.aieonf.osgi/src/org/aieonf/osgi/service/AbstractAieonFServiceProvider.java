package org.aieonf.osgi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.selection.ISelectionBean;

public abstract class AbstractAieonFServiceProvider<T extends Object> implements IAieonFService<T>{

	private IDomainAieon domain;
	private Collection<ISelectionBean> selections;
	
	private ExecutorService executor;	
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	protected AbstractAieonFServiceProvider() {
		selections = new ArrayList<ISelectionBean>();
		executor = Executors.newCachedThreadPool(); 
	}

	/**
	 * Initialise the service. typically this means retrieving the domain aieon
	 * and setting the corresponding bean
	 */
	protected abstract void initSelection();
	
	public void activate(){ 
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				initSelection();
			}
			
		};
		executor.execute(runnable);
	};
	
	public void deactivate(){ 
		executor.shutdown(); 
	}

	
	public synchronized IDomainAieon getDomain() {
		return domain;
	}

	@Override
	public ISelectionBean[] getSelectionBeans() {
		return selections.toArray( new ISelectionBean[selections.size()]);
	}

	@Override
	public void setActiveDomain(IDomainAieon domain) {
		this.domain = domain;
	}

	public void unsetActiveDomain(IDomainAieon domain) {
		this.domain = null;
	}

	/**
	 * Create a bean for the given domain
	 * @param domain
	 * @return
	 */
	protected ISelectionBean createBean( IDomainAieon domain  ){
		ISelectionBean bean = new SelectionBean( domain );
		this.selections.add(bean);
		logger.info("Selection added: " + domain.getDomain());
		return bean;
	}
	
	private static class SelectionBean implements ISelectionBean {

		private IDomainAieon domain;
		private boolean active;
		
		public SelectionBean( IDomainAieon domain) {
			this.domain = domain;
		}

		@Override
		public IDomainAieon getDomain() {
			return domain;
		}

		@Override
		public String getName() {
			return domain.getShortName();
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void setActive(boolean choice) {
			this.active = choice;
		}
	}

}
