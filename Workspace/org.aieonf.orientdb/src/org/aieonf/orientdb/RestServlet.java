package org.aieonf.orientdb;

import javax.servlet.Servlet;
import javax.ws.rs.ApplicationPath;

import org.aieonf.orientdb.log.DebugExceptionMapper;
import org.aieonf.orientdb.rest.ModelRestService;
import org.condast.commons.messaging.http.AbstractServletWrapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServlet extends AbstractServletWrapper {

	//Same as alias in plugin.xml
	public static final String S_CONTEXT_PATH = "aieonf/orientdb";

	public RestServlet() {
		super( S_CONTEXT_PATH );
	}
	
	@Override
	protected Servlet onCreateServlet(String contextPath) {
		RestApplication resourceConfig = new RestApplication();
		return new ServletContainer(resourceConfig);
	}

	@ApplicationPath(S_CONTEXT_PATH)
	private class RestApplication extends ResourceConfig {

		//Loading classes is the safest way...
		//in equinox the scanning of packages may not work
		private RestApplication() {
			register( ModelRestService.class );
			register( DebugExceptionMapper.class);
		}
	}
}
