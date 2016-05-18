package org.aieonf.osgi.service;

import org.aieonf.osgi.selection.ISelectionBean;
import org.aieonf.osgi.swt.IViewFactory;

public interface IAieonFService<T extends Object> {

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
