package org.aieonf.browsersupport.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.aieonf.commons.net.IWebResourceProvider;

public class ActiveLinkData implements IWebResourceProvider
{
	private String provider;
	private URL url;
	private Date date;
	
	public ActiveLinkData( String provider, URL url, Date date )
	{
		this.provider = provider;
		this.url = url;
		this.date = date;
	}

	public ActiveLinkData( String provider, String url, Date date ) throws MalformedURLException
	{
		this.provider = provider;
		this.url = new URL( url );
		this.date =date;
	}
	
	@Override
	public String getProvider()
	{
		return this.provider;
	}

	@Override
	public URL getURL()
	{
		return this.url;
	}

	@Override
	public Date getDate()
	{
		return this.date;
	}

}
