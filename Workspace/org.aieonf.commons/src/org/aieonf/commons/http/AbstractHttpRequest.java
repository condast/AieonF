package org.aieonf.commons.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.ws.rs.core.Response;

import org.aieonf.commons.Utils;
import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.strings.StringStyler;


/**
 * @see: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 * @author Kees
 *
 */
public abstract class AbstractHttpRequest<R, D extends Object> implements IHttpRequest<R,D> {

	private final String USER_AGENT = "Mozilla/5.0";

	private static final String S_ERR_RESPONSE = "EXCEPTION FOR REQUEST: ";
	private static final String S_ERR_RESPONSE_FAIL1 ="Invalid Response (request: ";
	private String contextPath;
	private int responseCode;
	
	private Collection<IHttpClientListener<R,D>> listeners;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	protected AbstractHttpRequest() {
		this( null );
	}

	protected AbstractHttpRequest( String path ) {
		this.contextPath = path;
		this.listeners = new ArrayList<>();
	}

	protected String getContextPath() {
		return contextPath;
	}

	protected void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * Set the request and returns the path
	 * @param request
	 * @return
	 */
	protected String setRequest( R request){
		String addition = request.toString();
		if( request instanceof Enum<?>) {
			Enum<?> enm = (Enum<?>) request;
			addition = enm.name();
		}
		String path = this.contextPath + "/"+ StringStyler.xmlStyleString( addition );
		return path;
	}

	public Map<String, String> getParameters(){
		return new HashMap<String, String>();
	}

	public int getResponseCode() {
		return responseCode;
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.http.IHttpRequest#addListener(org.condast.commons.http.IHttpClientListener)
	 */
	@Override
	public void addListener( IHttpClientListener<R,D> listener ) {
		this.listeners.add( listener);
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.http.IHttpRequest#removeListener(org.condast.commons.http.IHttpClientListener)
	 */
	@Override
	public void removeListener( IHttpClientListener<R,D> listener ) {
		this.listeners.remove( listener);
	}
	
	protected void notifyListeners( ResponseEvent<R,D> event ) {
		Collection<IHttpClientListener<R,D>> temp = new ArrayList<>( this.listeners);
		for( IHttpClientListener<R,D> listener: temp )
			listener.notifyResponse(event);
	}

	// HTTP GET request
	protected void sendGet() throws Exception {
		URL url = new URL( this.contextPath );
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// optional default is GET
		con.setRequestMethod(HTTP.GET.name());
		con.setRequestProperty("User-Agent", USER_AGENT);

		//add request header
		String path = this.contextPath;
		handleResponse( path, null, null, con, null );
	}

	// HTTP GET request
	/* (non-Javadoc)
	 * @see org.condast.commons.http.IHttpRequest#sendGet(java.lang.String, java.util.Map)
	 */
	protected void sendGet( R request) throws Exception {
		String path = setRequest( request );
		send( HTTP.GET, path );
	}

	protected void sendGet( R request, Map<String, String> parameters) throws Exception {
		sendGet( request, parameters, null );
	}

	protected void sendGet( R request, Map<String, String> parameters, D data) throws Exception {
		String url = setRequest( request );
		String path = toAttributerString( url, parameters );
		send( HTTP.GET, path, request, parameters, null, data );
	}
	
	@Override
	public void sendGet( String path, Map<String, String> parameters) throws Exception {
		send( HTTP.GET, path, parameters );
	}

	protected void sendPost( R request, Map<String, String> parameters, String post) throws Exception {
		sendPost( request, parameters, post, null );
	}

	protected void sendPost( R request, Map<String, String> parameters, String post, D data) throws Exception {
		String url = setRequest( request );
		String path = toAttributerString( url, parameters );
		send( HTTP.POST, path, request, parameters, post, data );
	}

	@Override
	public void sendPost( String url, Map<String, String> args, String post ) throws Exception {
		String request = toAttributerString( url, args );
		send( HTTP.POST, request, args, post );
	}

	protected void sendPut( R request) throws Exception {
		String path = setRequest( request );
		send( HTTP.PUT, path );
	}

	@Override
	public void sendPut( String url, Map<String, String> args, String post) throws Exception {
		send( HTTP.PUT, url, args, post, null );
	}

	public void sendPut( R request, Map<String, String> args, String post, D data) throws Exception {
		String url = setRequest( request );
		String path = toAttributerString( url, args );
		send( HTTP.PUT, path, request, args, post, data );
	}

	@Override
	public void sendDelete( String url, Map<String, String> args) throws Exception {
		sendDelete( url, args, null );
	}

	public void sendDelete( String url, Map<String, String> args, D data) throws Exception {
		send( HTTP.DELETE, url, args, null, data );
	}

	protected void sendDelete( R request, Map<String, String> parameters, D data) throws Exception {
		String url = setRequest( request );
		String path = toAttributerString( url, parameters );
		send( HTTP.DELETE, path, request, parameters, null, data );
	}


	protected void send( HTTP http, String url ) throws Exception {
		send( http, url, null );
	}

	protected void send( HTTP http, String request, Map<String, String> args ) throws Exception {
		send( http, request, args, null );
	}

	protected void send( HTTP http, String url, Map<String, String> args, String post ) throws Exception {
		send( http, url, args, post, null );
	}

	protected void send( HTTP http, String url, Map<String, String> args, String post, D data ) throws Exception {
		send( http, url, (R)null, args, post, data );
	}

	protected void send( HTTP http, String url, R request, Map<String, String> args, String post, D data ) throws Exception {
		String charset = "UTF-8"; 
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

		//add request header
		conn.setRequestMethod( http.name());
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setReadTimeout(10000);
		switch( http ) {
		case POST:
			conn.setRequestProperty("Accept", "application/json");
		case PUT://FALL THROUGH
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setRequestProperty("Content-Type", "application/json; charset=" + charset);
			conn.setRequestProperty( "Content-Length", String.valueOf( post.length()));
			conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			try{
				wr.writeBytes( post );
			}
			finally{
				wr.flush();
				wr.close();
			}
			break;
		default:
			break;
			
		}
		handleResponse( url, request, args, conn, data);
		int responseCode = conn.getResponseCode();
		if( responseCode != IHttpRequest.HttpStatus.OK.getStatus())
			logger.fine( request + "\n\tResponse Code : " + getHttpStatus( responseCode ).name() + 
				"(" +responseCode + ")");
		conn.disconnect();
	}

	/**
	 * Handle a succesful response
	 * @param url
	 * @param responseCode
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	protected abstract String onHandleResponse( ResponseEvent<R,D> event, D data ) throws IOException;

	/**
	 * Handle a failed response
	 * @param url
	 * @param responseCode
	 * @param reader
	 * @throws IOException
	 */
	protected void onHandleResponseFail( HttpStatus status, ResponseEvent<R,D> event ) throws IOException{
		responseFailMessage(responseCode, event);
	}

	protected ResponseEvent<R,D> handleResponse( String url, R request, Map<String, String> params, HttpURLConnection con, D data ){
		BufferedReader in = null;
		String str = null;
		ResponseEvent<R,D> event = null;
		try{
			this.responseCode = con.getResponseCode();
			if( responseCode == Response.Status.OK.getStatusCode()) {
				in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
				event = new ResponseEvent<R,D>( this, url, request, params, data, in );
				str = onHandleResponse( event, data );
			}else {
				event = new ResponseEvent<R,D>( this, url, request, params, data, str, responseCode );
				onHandleResponseFail( getHttpStatus(responseCode), event );
			}
			notifyListeners( event);
		}
		catch( Exception ex ){
			logger.severe( S_ERR_RESPONSE + request);
			ex.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly( in );
		}
		return event;
	}

	// HTTP GET request
	/* (non-Javadoc)
	 * @see org.condast.commons.http.IHttpRequest#sendGet(java.lang.String, java.util.Map)
	 */
	public String toAttributerString( String path, Map<String, String> parameters) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append( path );
		if( !Utils.assertNull(parameters )){
			Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
			boolean first = true;
			while( iterator.hasNext() ){
				buffer.append( first? "?": "&");
				first = false;
				Map.Entry<String, String> entry = iterator.next();
				buffer.append(entry.getKey());
				buffer.append("=");
				buffer.append( URLEncoder.encode( entry.getValue(), "UTF-8"));
			}
		}
		return buffer.toString();
	}

	/**
	 * Handle a failed response
	 * @param url
	 * @param responseCode
	 * @param reader
	 * @throws IOException
	 */
	protected static <R, D extends Object>void responseFailMessage( int responseCode, ResponseEvent<R,D> event ){
		Logger logger = Logger.getLogger( AbstractHttpRequest.class.getName());
		logger.warning( S_ERR_RESPONSE_FAIL1 + event.getRequest() + "): " + responseCode + ": " + HttpStatus.getMessage( responseCode));
	}

	/**
	 * Parse the request from the given url 
	 * @param url
	 * @return
	 */
	protected static String parseRequest( String path ){
		String[] split = path.split("[?]");
		split = split[0].split("[/]");
		return split[ split.length-1];
	}

	/**
	 * Parse the request from the given url 
	 * @param url
	 * @return
	 */
	protected static String parseRequest( URL url ){
		return parseRequest( url.getPath());
	}

	protected static String transform( Reader reader ) {
		StringBuffer buffer = new StringBuffer();
		Scanner scanner = new Scanner( reader );
		try {
			while( scanner.hasNextLine() )
			buffer.append( scanner.nextLine());
		}
		finally {
			scanner.close();
		}
		return buffer.toString();
	}
	
	public static IHttpRequest.HttpStatus getHttpStatus( int responseCode ) {
		return IHttpRequest.HttpStatus.getHttpStatus( responseCode);
	}

	/**
	 * Get a data string from the given input stream
	 * @param in
	 * @return
	 */
	protected static String getDataString( InputStream in) {
		StringBuffer buffer = new StringBuffer();
		Scanner scanner = new Scanner( in );
		try {
			while( scanner.hasNextLine() ) {
				buffer.append( scanner.nextLine());
			}
		}
		finally {
			scanner.close();
		}
		return buffer.toString();
	}

	/**
	 * Remove HTML from the body
	 * @param str
	 * @return
	 */
	public static String unescapeHtml3( String str ) {
	    try {
	        HTMLDocument doc = new HTMLDocument();
	        new HTMLEditorKit().read( new StringReader( str ), doc, 0 );
	        return doc.getText( 1, doc.getLength() );
	    } catch( Exception ex ) {
	        return str;
	    }
	}

}
