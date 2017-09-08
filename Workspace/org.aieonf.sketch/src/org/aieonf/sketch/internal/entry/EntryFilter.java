package org.aieonf.sketch.internal.entry;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EntryFilter implements Filter {

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("incoming request");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
