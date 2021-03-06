package org.aieonf.commons.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.EventObject;
import java.util.Map;

import javax.ws.rs.core.Response;

public class ResponseEvent<R extends Object, D extends Object > extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private R request;
	private Map<String, String> params;
	
	private String response;
	
	private D data;
	
	private int responseCode;

	public ResponseEvent(Object source, String url, R request, Map<String, String> params, D data, Reader reader ) {
		this( source, url, request, params, data, printResponse(reader), Response.Status.OK.getStatusCode() );
	}

	public ResponseEvent(Object source, String url, R request, Map<String, String> params, D data, Reader reader, int responseCode ) {
		this( source, url, request, params, data, printResponse(reader), responseCode );
	}
	
	public ResponseEvent(Object source, String url, R request, Map<String, String> params, D data, String response, int responseCode ) {
		super(source);
		this.request = request;
		this.params = params;
		this.response = response;
		this.data = data;
		this.url = url;
		this.responseCode = responseCode;
	}

	public String getURL() {
		return url;
	}

	public R getRequest() {
		return request;
	}

	public Map<String, String> getParameters() {
		return params;
	}

	public D getData() {
		return data;
	}

	public String getResponse() {
		return response;
	}
	
	
	public int getResponseCode() {
		return responseCode;
	}

	public static String printResponse( Reader reader ) {
		BufferedReader in = new BufferedReader(reader);
		String line = null;
		StringBuilder rslt = new StringBuilder();
		try {
			while ((line = in.readLine()) != null) {
			    rslt.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rslt.toString();
	}

}
