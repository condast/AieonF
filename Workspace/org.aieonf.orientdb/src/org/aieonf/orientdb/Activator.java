package org.aieonf.orientdb;

import org.aieonf.orientdb.db.DatabasePersistenceService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	private static BundleContext context;

	private static DatabasePersistenceService graph = DatabasePersistenceService.getInstance();

	static BundleContext getContext() {
		return context;
	}

	public static DatabasePersistenceService getGraphService() {
		return graph;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		try {
			Activator.context = bundleContext;
			graph.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		graph.disconnect();
	}
}
