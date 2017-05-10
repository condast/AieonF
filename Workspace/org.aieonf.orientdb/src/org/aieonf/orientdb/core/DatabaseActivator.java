package org.aieonf.orientdb.core;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.orientdb.factory.OrientDBFactory;
import org.osgi.framework.BundleContext;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class DatabaseActivator {

	private ExecutorService service;
	private OServer server;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				OrientDBFactory factory = OrientDBFactory.getInstance();
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
		Properties props = System.getProperties();
		props.setProperty("-dXX:MaxDirectMemorySize", "512g");
		
		service = Executors.newSingleThreadExecutor();
		service.execute(runnable);		
	}
	
	public void shutdown( BundleContext context ){
		server.shutdown();	
		service.shutdown();
	}
}
