package org.aieonf.orientdb.core;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aieonf.orientdb.factory.OrientDBFactory;

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
	public static final String S_ORIENTDB_PERSISTENCE = "/META-INF/persistence.xml";
	public static final String S_ORIENTDB_ROOT_PASSWORD = "ORIENTDB_ROOT_PASSWORD";
	
	private ExecutorService service;
	private OServer server;
	private OrientDBFactory factory;
	private boolean activated;
	
	private static DatabaseActivator activator = new DatabaseActivator();
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				server = OServerMain.create();
				server.startup( DatabaseActivator.class.getResourceAsStream( S_ORIENTDB_PERSISTENCE ));
				server.activate();
				activated = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	
	private DatabaseActivator() {
		super();
		this.activated = false;
	}

	public static DatabaseActivator getInstance() {
		return activator;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void startup( ){
		factory = OrientDBFactory.getInstance();
		Properties props = System.getProperties();
		props.setProperty( S_ORIENTDB_HOME, factory.getOrientDBRoot().getAbsolutePath());
		props.setProperty( S_ORIENTDB_ROOT_PASSWORD, "BLANK" );//auto generate a root password (launch config)
		props.setProperty("-XX:MaxDirectMemorySize", "4008m");
		
		service = Executors.newSingleThreadExecutor();
		service.execute(runnable);		
	}
	
	public void shutdown( ){
		activated = false;
		server.shutdown();	
		service.shutdown();
	}
}
