/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.aieonf.commons.ui.images;

import org.aieonf.commons.strings.StringStyler;
import org.aieonf.commons.ui.Activator;
import org.eclipse.swt.graphics.Image;

public class LabelProviderImages extends AbstractImages{

	public static final String S_ICON_PATH = "/labels/";

	public static final String S_CHECKED_ICON  = "checked.png";
	public static final String S_UNCHECKED_ICON  = "unchecked.png";
	public static final String S_DIS_CHECKED_ICON  = "dis-checked.png";
	public static final String S_DIS_UNCHECKED_ICON  = "dis-unchecked.png";
	public static final String S_QUESTION_MARK_ICON  = "question.png";
	public static final String S_ERROR_ICON  = "error.png";
	public static final String S_WRITABLE_ICON  = "writable.png";
	public static final String S_NON_WRITABLE_ICON  = "non-writable.png";
	public static final String S_NON_EDITABLE_ICON  = "non-editable-16.png";
	
	public enum Images{
		CHECKED,
		UNCHECKED,
		QUESTION_MARK,
		WRITABLE,
		NON_WRITABLE,
		NON_EDITABLE,
		ERROR;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	public LabelProviderImages() {
		super( S_ICON_PATH, Activator.BUNDLE_ID );
	}
	
	@Override
	protected void initialise(){
		setImage( S_CHECKED_ICON );
		setImage( S_UNCHECKED_ICON );
		setImage( S_DIS_CHECKED_ICON );
		setImage( S_DIS_UNCHECKED_ICON );
		setImage( S_QUESTION_MARK_ICON );
		setImage( S_ERROR_ICON );
		setImage( S_WRITABLE_ICON );
		setImage( S_NON_WRITABLE_ICON );
		setImage( S_NON_EDITABLE_ICON );
	}

	/**
	 * Get the image
	 * @param desc
	 * @return
	 */
	public Image getImage( Images desc, boolean enabled ){
		if( enabled )
			return getImage( desc );
		return getDisableImage(desc);	
	}

	public Image getChecked( boolean checked ) {
		return checked?getImage(Images.CHECKED): getImage(Images.UNCHECKED);
	}

	/**
	 * Get the image
	 * @param desc
	 * @return
	 */
	public Image getImage( Images desc ){
		switch( desc ){
		case CHECKED:
			return getImageFromName( S_CHECKED_ICON );
		case UNCHECKED:
			return getImageFromName( S_UNCHECKED_ICON );
		case ERROR:
			return this.getImageFromName( S_ERROR_ICON );
		case WRITABLE:
			return this.getImageFromName( S_WRITABLE_ICON );
		case NON_WRITABLE:
			return this.getImageFromName( S_NON_WRITABLE_ICON );
		case NON_EDITABLE:
			return this.getImageFromName( S_NON_EDITABLE_ICON );
		default:
			return getImageFromName( S_QUESTION_MARK_ICON );				
		}
	}

	/**
	 * Get the image
	 * @param desc
	 * @return
	 */
	public Image getDisableImage( Images desc ){
		switch( desc ){
		case CHECKED:
			return getImageFromName( S_DIS_CHECKED_ICON );
		case UNCHECKED:
			return getImageFromName( S_DIS_UNCHECKED_ICON );
		case ERROR:
			return this.getImageFromName( S_ERROR_ICON );
		case WRITABLE:
			return this.getImageFromName( S_WRITABLE_ICON );
		case NON_WRITABLE:
			return this.getImageFromName( S_NON_WRITABLE_ICON );
		case NON_EDITABLE:
			return this.getImageFromName( S_NON_EDITABLE_ICON );
		default:
			return getImageFromName( S_QUESTION_MARK_ICON );				
		}
	}
}