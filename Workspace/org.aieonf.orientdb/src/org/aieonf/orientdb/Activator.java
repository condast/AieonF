package org.aieonf.orientdb;

import org.aieonf.orientdb.core.DatabaseActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	private DatabaseActivator dba = new DatabaseActivator();
	
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		dba.startup(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		dba.shutdown(bundleContext);
		Activator.context = null;
	}

}
