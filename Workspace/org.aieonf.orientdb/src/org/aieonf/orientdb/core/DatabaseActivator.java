package org.aieonf.orientdb.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.BundleContext;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class DatabaseActivator {

	private static final String S_ORIENT_CFG = "/config/orient-server.config";

	protected static final String S_STARTUP_CODE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
			   + "<orient-server>"
			   + "<network>"
			   + "<protocols>"
			   + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
			   + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
			   + "</protocols>"
			   + "<listeners>"
			   + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
			   + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
			   + "</listeners>"
			   + "</network>"
			   + "<users>"
			   + "<user name=\"saight\" password=\"aurora_borealis\" resources=\"*\"/>"
			   + "</users>"
			   + "<properties>"
			   + "<entry name=\"orientdb.www.path\" value=\"./1.0rc1-SNAPSHOT/www/\"/>"
			   + "<entry name=\"orientdb.config.file\" value=\"./config/orientdb-server-config.xml\"/>"
			   + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
			   + "<entry name=\"log.console.level\" value=\"info\"/>"
			   + "<entry name=\"log.file.level\" value=\"fine\"/>"
			   //The following is required to eliminate an error or warning "Error on resolving property: ORIENTDB_HOME"
			   + "<entry name=\"plugin.dynamic\" value=\"false\"/>"
			   + "</properties>" + "</orient-server>";

	private ExecutorService service;
	private OServer server;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				server = OServerMain.create();
				//server.startup( S_STARTUP_CODE );
				server.startup(getClass().getResourceAsStream( S_ORIENT_CFG));
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
		service = Executors.newSingleThreadExecutor();
		service.execute(runnable);		
	}
	
	public void shutdown( BundleContext context ){
		server.shutdown();	
		service.shutdown();
	}
}
