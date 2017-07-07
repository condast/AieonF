package org.aieonf.orientdb.core;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.orientdb.factory.OrientDBFactory;
import org.osgi.framework.BundleContext;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

/**
 * Set VM Argumements in launch configuration: -XX:MaxDirectMemorySize=4008m
 *  (exactly in this way...no -D first!)
 * @author Kees
 *
 */
public class DatabaseActivator {

	//Default JVM setting for OrientDB. Is handled in lauch configuration
	public static final String S_XX_PROPERTY = "-D-XX:MaxDirectMemorySize=512g";
	public static final String S_ORIENTDB_HOME = "ORIENTDB_HOME";
	public static final String S_ORIENTDB_ROOT_PASSWORD = "ORIENTDB_ROOT_PASSWORD";
	
	private ExecutorService service;
	private OServer server;
	private OrientDBFactory factory;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				server = OServerMain.create();
				server.startup( factory.getConfigFile() );
				server.activate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public DatabaseActivator() {
		super();
	}

	public void startup( BundleContext context ){
		factory = OrientDBFactory.getInstance();
		Properties props = System.getProperties();
		props.setProperty( S_ORIENTDB_HOME, factory.getOrientDBRoot().getAbsolutePath());
		props.setProperty( S_ORIENTDB_ROOT_PASSWORD, "BLANK" );//auto generate a root password (launch config)
		//props.setProperty("-XX:MaxDirectMemorySize", "4008m");
		
		service = Executors.newSingleThreadExecutor();
		service.execute(runnable);		
	}
	
	public void shutdown( BundleContext context ){
		server.shutdown();	
		service.shutdown();
	}
}
