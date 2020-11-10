/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.aieonf.commons.ui.images;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.ui.Activator;
import org.eclipse.swt.graphics.Image;

public class DashboardImages extends AbstractImages {

	public enum Images{
		ADD,
		DELETE,
		DELETE_SMALL,
		DONTSHOW,
		SETTINGSGREEN,
		DOSSIER,
		FIELD,
		LOCATE,
		QUESTION,
		QUESTION_MARK,
		HOME,
		CONDAST,
		INFO,
		HELP,
		WORLD,
		MAP,
		COMPARE,
		EXPAND,
		COLLAPSE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static String getResource( Images image ){
			return getResource(image, ImageSize.NORMAL);
		}
		
		public static String getResource( Images image, ImageSize size ){
			StringBuilder builder = new StringBuilder();
			switch( image ){
			case CONDAST:
				builder.append(ImageSize.getFolder(ImageSize.DEFAULT));
				builder.append(".png" );	
				break;
			case DOSSIER:
				builder.append(ImageSize.getFolder(ImageSize.TINY));
				builder.append( "dossier-16");
				break;
			case HOME:
				builder.append(ImageSize.getFolder(ImageSize.TINY));
				builder.append( "home-16");
				break;
			case QUESTION_MARK:
				builder.append(ImageSize.getFolder(ImageSize.TINY));
				builder.append( "question_mark-16");
				break;
			default:
				builder.append(ImageSize.getFolder(size));
				builder.append(image.name().toLowerCase());
				builder.append("-" );
				builder.append( size.getSize() );
				builder.append(".png" );	
				break;
			}
			return builder.toString();
		}
		
		/**
		 * 
		 * @param image, enumimages like Images.ADD, DELETE and so on
		 * @param imageSize, 
		 * @return
		 */
		public static String getFileName( Images image, ImageSize imageSize ){
			String str = ImageSize.getLocation( image.name(), imageSize );//get location, filename and size from super
			return str;
		}

	}
	
	private static DashboardImages images = new DashboardImages();
	
	private DashboardImages() {
		super( S_RESOURCES, Activator.BUNDLE_ID );
	}

	/**
	 * Get an instance of this map
	 * @return
	 */
	public static DashboardImages getInstance(){
		return images;
	}
	
	@Override
	public void initialise(){
		for( Images image: Images.values())
			setImage( image.toString() );
	}

	public static Image getImage( Images image, ImageSize size ){
		return getInstance().getImageFromName( Images.getResource( image ));
	}

	public static Image getImage( Images image ){
		return getInstance().getImageFromName( Images.getResource( image ));
	}

	protected void setImage( Images image ){
		super.setImage( Images.getResource(image));
	}

}