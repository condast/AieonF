package org.aieonf.osgi.service;

import org.aieonf.concept.domain.IDomainAieon;
import org.aieonf.osgi.selection.ISelectionBean;
import org.aieonf.osgi.swt.IViewFactory;

public interface IAieonFService<T extends Object> {

	/**
	 * Set the active domain
	 * @param domain
	 */
	public void setActiveDomain( IDomainAieon domain );
	
	/**
	 * The selection beans are used to communicate the available application bundles
	 * to the system.
	 * @return
	 */
	public ISelectionBean[] getSelectionBeans();
	
	/**
	 * A view factory can provide views for a front end
	 * @param id
	 * @return
	 */
	public IViewFactory<T, T> getCompositeFactory( String id );
}
