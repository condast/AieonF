package org.aieonf.commons.messaging;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AbstractServletWrapper implements Servlet {

	public static final String S_CONTEXT_PATH = "rest";
	
	Servlet servletContainer;

	protected AbstractServletWrapper() {
		this( S_CONTEXT_PATH );
	}

	/**
	 * The context path should be the same as the alias in plugin.xml.
	 * In that case the servlet will start in:
	 * http://{url}:{port}/{context-path}/...
	 * @param contextPath
	 */
	protected AbstractServletWrapper( String contextPath ) {
		servletContainer = this.onCreateServlet(contextPath);
	}

	protected abstract Servlet onCreateServlet( String contextPath );
	
	@Override
	public void destroy() {
		servletContainer.destroy();
	}

	@Override
	public ServletConfig getServletConfig() {
		return servletContainer.getServletConfig();
	}

	@Override
	public String getServletInfo() {
		return servletContainer.getServletInfo();
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		servletContainer.init(arg0);
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		servletContainer.service(arg0, arg1);
	}
}
