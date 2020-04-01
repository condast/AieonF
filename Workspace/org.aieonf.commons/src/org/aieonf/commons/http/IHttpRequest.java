package org.aieonf.commons.http;

import java.util.Map;

public interface IHttpRequest<R extends Object, D extends Object> {

	public enum HTTP{
		GET,
		POST,
		DELETE,
		PUT;
	}


	public enum Responses{
		OK,
		BAD;
	}
	
	public enum HttpStatus{
		UNKNOWN(0),
		OK(200),
		URL_NOT_FOUND(302),
		NOT_MODIFIED(304),
		BAD_REQUEST(400),
		UNAUTHORISED(401),
		NO_CONTENT(204),
		NOT_FOUND(404),
		SERVER_ERROR(500);
		
		private int status;
		
		private HttpStatus( int index ) {
			this.status = index;
		}
	
		public int getStatus() {
			return status;
		}
		
		public static HttpStatus getHttpStatus( int status ) {
			for( HttpStatus hs: values()) {
				if( hs.getStatus() == status )
					return hs;
			}
			return HttpStatus.UNKNOWN;
		}
	
		public static HttpStatus getStatus( int status ) {
			for( HttpStatus hs: values() ) {
				if( hs.getStatus()== status )
					return hs;
			}
			return HttpStatus.UNKNOWN;
		}
	
		public static String getMessage( HttpStatus status ) {
			String result = status.name();
			switch( status ) {
			case NOT_FOUND:
				result = "The header does not correspond with the URL";
				break;
			case SERVER_ERROR:
				result = "Check the log files";
				break;
			default:
				break;
			}
			return result;
		}
	
		public static String getMessage( int status ) {
			return getMessage( getStatus( status ));
		}
	}

	void addListener(IHttpClientListener<R,D> listener);

	void removeListener(IHttpClientListener<R,D> listener);

	// HTTP GET request
	void sendGet(String path, Map<String, String> parameters) throws Exception;

	// HTTP POST request
	void sendPost(String url, Map<String, String> args, String data) throws Exception;

	// HTTP PUT request
	void sendPut(String url, Map<String, String> args, String data) throws Exception;

	// HTTP DELETE request
	void sendDelete(String url, Map<String, String> args) throws Exception;


}