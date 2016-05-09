package org.aieonf.commons.net;

import java.net.URL;
import java.util.Date;

public interface IWebResourceProvider
{
	/**
	 * Get the provider name of the URL
	 * @return
	 */
	public String getProvider();
	
	/**
	 * Get the URL
	 * @return
	 */
	public URL getURL();
	
	/**
	 * Get the date of selection
	 * @return
	 */
	public Date getDate();
}
