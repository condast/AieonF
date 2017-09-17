/*******************************************************************************
 * Copyright (c) 2014, 2016 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.osgi.images;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.Utils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

public abstract class AbstractImages {

	private static final String S_ERR_INVALID_BUNDLE_NAME = "The bundle name is invalid: ";

	public enum ImageSize{
		SMALL,
		NORMAL,
		LARGE,
		TILE;
		
		public int getSize(){
			int size = 32;
			switch( this ){
			case SMALL:
				size = 16;
				break;
			case LARGE:
				size = 64;
				break;
			case TILE:
				size = 128;
			default:
				break;
			}
			return size;
		}
		
		public static String getLocation( ImageSize size, String imageName ){
			return size.name().toLowerCase() + "/" + imageName + "-" + size.getSize() + ".png";
		}
	}
	
	private Map<String, ImageStore> images;
	private String path;

	private String bundleName;
	private ImageSize size;

	protected AbstractImages( String path ) {
		this( path, null, ImageSize.NORMAL );
	}

	protected AbstractImages( String path, String bundleName ) {
		this( path, bundleName, ImageSize.NORMAL );
	}
	
	protected AbstractImages( String path, String bundleName, ImageSize size ) {
		Bundle bundle = Platform.getBundle( bundleName );
		if( bundle == null )
			throw new NullPointerException( S_ERR_INVALID_BUNDLE_NAME + bundleName);
		this.size = size;
		images = new HashMap<String, ImageStore>();
		this.path = path;
		this.bundleName = bundleName;
		this.initialise();
	}
	
	protected ImageStore setImage( String name ){
		ImageStore data = new ImageStore();
		data.location = path + name;
		data.descriptor = getImageDescriptor( data.location ); 
		data.image = data.descriptor.createImage();
		images.put( name, data );	
		return data;
	}

	/**
	 * Get the image with the given 
	 * @param identifier
	 * @return
	 */
	public Image getImageFromName( String identifier ){
		
		ImageStore data = images.get( identifier );
		if( data != null )
			return data.image;
		
		data = setImage( identifier );
		return ( data == null )? null: data.image;
	}
	
	protected String getSizedLocation( Enum<?> identifier ){
		return ImageSize.getLocation(size, identifier.name().toLowerCase());
	}
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public Image getSizedImage( Enum<?> identifier ){
		String path = getSizedLocation(identifier);//ImageSize.getLocation(size, identifier.name().toLowerCase());
		return getImageFromName(path);
	}
	
	protected abstract void initialise();
	
	public void dispose(){
		for( ImageStore data: images.values() )
			data.image.dispose();
		images.clear();
	}

	/**
	 * Get the URL where the image is located
	 * @param location
	 * @return
	 */
	protected URL getImageURL( String location ){
		URL url = null;
		if( Utils.assertNull( this.bundleName )){
			if( !location.startsWith("/"))
				location = "/" + location;
			url = this.getClass().getResource( location );
			if( url == null )
				url = AbstractImages.class.getResource( location );
		}else{
			Bundle bundle = Platform.getBundle( this.bundleName ); 
			url = bundle.getResource( location );      	 
		}
		return url;
	}
	
	/**
	 * Get the image descriptor
	 * @param location
	 * @return
	 */
    protected ImageDescriptor getImageDescriptor(String location ) {
       URL url = this.getImageURL(location);
       return ImageDescriptor.createFromURL(url);
    }

    private class ImageStore{	
    	String location;
    	ImageDescriptor descriptor;
    	Image image;
    }
  
    public static Image getImageFromResource( Display display, Class<?> clss, String resource ){
    	return new Image( display, clss.getResourceAsStream( resource));
    }
}