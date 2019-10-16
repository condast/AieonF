package org.aieonf.orientdb;

import org.aieonf.orientdb.core.CachePersistenceService;
import org.aieonf.orientdb.db.DatabasePersistenceService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
	private static BundleContext context;

	private static CachePersistenceService cache = CachePersistenceService.getInstance();

	private static DatabasePersistenceService graph = DatabasePersistenceService.getInstance();

	static BundleContext getContext() {
		return context;
	}

	public static CachePersistenceService getCacheService() {
		return cache;
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
		Activator.context = bundleContext;
		cache.connect();
		graph.connect();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		graph.disconnect();
		cache.disconnect();
	}
}
