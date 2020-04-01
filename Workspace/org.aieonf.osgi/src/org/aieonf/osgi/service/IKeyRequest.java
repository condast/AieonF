package org.aieonf.osgi.service;

import java.util.Map;

@FunctionalInterface
public interface IKeyRequest {

	/**
	 * Implement a response to an incoming request
	 * @param name
	 * @param params
	 */
	public void request( String name, Map<String, String> params ); 
}
