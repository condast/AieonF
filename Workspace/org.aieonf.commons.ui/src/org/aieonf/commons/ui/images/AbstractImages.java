/*******************************************************************************
 * Copyright (c) 2014, 2016 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.ui.images;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.strings.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

public abstract class AbstractImages implements IImageProvider {

	public static final String S_RESOURCES = "/resources/";

	private static final String S_ERR_INVALID_BUNDLE_NAME = "The bundle name is invalid: ";

	public enum ImageSize{
		DEFAULT,
		TINY,
		SMALL,
		MEDIUM,
		NORMAL,
		LARGE,
		BIG,
		HUGE;
		
		public int getSize(){
			return getSize( this );
		}
		
		public static String getFolder( ImageSize isize ) {
			String result=  "/";
			switch( isize ) {
			case DEFAULT:
				break;
			default:
				result = isize.name().toLowerCase() + result;
			}
			return result;
		}
		
		public static int getSize( ImageSize isize){
			int size = 0;
			switch( isize ){
			case TINY:
				size = 16;
				break;
			case SMALL:
				size = 24;
				break;
			case MEDIUM:
				size = 32;
				break;
			case LARGE:
				size = 48;
				break;
			case BIG:
				size = 64;
				break;
			case HUGE:
				size = 128;
				break;
			
			case DEFAULT://Fallthrough
			default:
				size = 32;//is NORMAL
				break;
			}
			return size;
		}
		
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static ImageSize getImageSize( int size ) {
			ImageSize imageSize = null;
			for( ImageSize iSize : ImageSize.values() ) {
				if( size == iSize.getSize() )
					imageSize = iSize;
			}
			return imageSize;
		}
		
		public static String getLocation( String imageName, ImageSize size ){
			String name = StringStyler.xmlStyleString(imageName);
			String folder = ImageSize.DEFAULT.equals(size)?"":size.name().toLowerCase();
			String size_suffix = ImageSize.DEFAULT.equals(size)?"": String.valueOf( size.getSize());
			return folder + "/" + name + "-" + size_suffix + ".png";
		}
	}
	
	private Map<String, ImageStore> images;
	private String path;

	private String bundleName;
	private ImageSize size;

	protected AbstractImages( String bundleName ) {
		this( S_RESOURCES, bundleName, ImageSize.NORMAL );
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

	/* (non-Javadoc)
	 * @see org.condast.commons.ui.image.IImageProvider#getImageFromName(java.lang.String)
	 */
	@Override
	public Image getImageFromName( String identifier ){
		
		ImageStore data = images.get( identifier );
		if( data != null )
			return data.image;
		
		data = setImage( identifier );
		return ( data == null )? null: data.image;
	}
	
	protected String getSizedLocation( Enum<?> identifier ){
		return ImageSize.getLocation(identifier.name().toLowerCase(), size );
	}
	
	/* (non-Javadoc)
	 * @see org.condast.commons.ui.image.IImageProvider#getSizedImage(java.lang.Enum)
	 */
	@Override
	public Image getSizedImage( Enum<?> identifier ){
		String path = getSizedLocation(identifier);//ImageSize.getLocation(size, identifier.name().toLowerCase());
		return getImageFromName(path);
	}
	
	protected abstract void initialise();
	
	/* (non-Javadoc)
	 * @see org.condast.commons.ui.image.IImageProvider#dispose()
	 */
	@Override
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
		if( StringUtils.isEmpty( this.bundleName )){
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