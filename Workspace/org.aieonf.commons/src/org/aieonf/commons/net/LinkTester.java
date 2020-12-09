package org.aieonf.commons.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.aieonf.commons.io.IOUtils;
import org.aieonf.commons.strings.StringStyler;

public class LinkTester {

	public enum URLStatus{
		INIT,
		TESTING,
		DOES_NOT_EXIST,
		TIMEOUT,
		MALFORMED_ERROR,
		IO_ERROR,
		PARSED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	private URLStatus status;
	private int hash;
	private Date lastTested;
	
	private String url;
	
	public LinkTester( String url, Date lastTested ) {
		this.url = url;
		this.lastTested = lastTested;
		status = URLStatus.INIT;
	}

	/**
	 * Get the status
	 * @return
	 */
	public URLStatus getStats() {
		return status;
	}
	
	/**
	 * Get the hash for this web link
	 * @return
	 */
	public int getHash() {
		return hash;
	}

	
	public Date getLastModified() {
		return lastTested;
	}

	/**
	 * Test to see if the link was modified since a prior date
	 * @return
	 */
	public boolean testModified(){
		this.status = URLStatus.TESTING;
		URL link;
		try {
			link = new URL( url );
			if( link.getProtocol() == null ){
				this.status = URLStatus.IO_ERROR;
				return false;
			}
			URLConnection connection = link.openConnection();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis( connection.getLastModified() );
			Date modified = calendar.getTime();
			this.status = URLStatus.PARSED;	
			
			boolean retval = ( modified.compareTo( this.lastTested ) > 0 );
			this.lastTested = modified;
			return retval;
		} catch (MalformedURLException e) {
			this.status = URLStatus.MALFORMED_ERROR;
			return false;
		} catch (IOException e) {
			this.status = URLStatus.IO_ERROR;
			return false;
		}
		catch (IllegalArgumentException e) {
			this.status = URLStatus.IO_ERROR;
			return false;
		}
	}

	public boolean testLink(){
		InputStream in = null;
		try {
			URL link = new URL( url );
			in = link.openStream();
			if( in == null ){
				this.status = URLStatus.DOES_NOT_EXIST;
				return false;
			}
			byte[] buffer = new byte[1024];
			@SuppressWarnings("unused")
			int i = 0;
			StringBuffer strbuf = new StringBuffer();
			while ((i = in.read(buffer)) != -1) {
				strbuf.append( String.valueOf(buffer ));
			} 
			this.status = URLStatus.PARSED;			
			hash = strbuf.hashCode();
			return true;
		}catch (MalformedURLException e) {
			e.printStackTrace();
			this.status = URLStatus.MALFORMED_ERROR;
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			this.status = URLStatus.IO_ERROR;
			return false;
		}
		finally{
			IOUtils.closeQuietly(in);
		}
	}
}