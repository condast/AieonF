package test.aieonf.serialise;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import test.aieonf.serialise.suite.TestSuite;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private TestSuite suite = TestSuite.getInstance();

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		try{
			Activator.context = bundleContext;
			suite.performTests();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
