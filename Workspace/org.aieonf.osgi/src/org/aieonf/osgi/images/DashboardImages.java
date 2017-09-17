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
package org.aieonf.osgi.images;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.osgi.Activator;
import org.eclipse.swt.graphics.Image;

public class DashboardImages extends AbstractImages {

	public static final String S_ICON_PATH = "/resources/";
	
	public enum Images{
		ADD,
		DELETE,
		SETTINGS,
		SETTINGS_16,
		DOSSIER,
		QUESTION_MARK,
		HOME,
		CONDAST,
		INFO,
		HELP,
		WORLD,
		MAP,
		COMPARE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static String getFileName( Images image ){
			String str = null;
			switch( image ){
			case ADD:
				str = "Add-32.png";
				break;
			case CONDAST:
				str = "condast.png";
				break;
			case DOSSIER:
				str = "dossier-16.png";
				break;
			case HOME:
				str = "home-16.png";
				break;
			case QUESTION_MARK:
				str = "question_mark-16.png";
				break;
			case SETTINGS:
				str = "Settings-32.png";
				break;
			case SETTINGS_16:
				str = "Settings-16.png";
				break;
			case WORLD:
				str = "world-32.png";
				break;
			default:
				str = image.name().toLowerCase() + "-32.png";
				break;
			}
			return str;
		}
	}
	
	private static DashboardImages images = new DashboardImages();
	
	private DashboardImages() {
		super( S_ICON_PATH, Activator.BUNDLE_ID );
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
		for( Images img: Images.values() )
			setImage( Images.getFileName( img ));
	}

	/**
	 * Get the image
	 * @param desc
	 * @return
	 */
	public Image getImage( Images desc ){
			return getImageFromName( Images.getFileName(desc));				
	}
}